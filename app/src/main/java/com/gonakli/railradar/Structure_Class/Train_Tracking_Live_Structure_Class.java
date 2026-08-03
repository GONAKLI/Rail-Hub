//package com.gonakli.railradar.Structure_Class;
//
//import android.location.Location;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//public class Train_Tracking_Live_Structure_Class {
//    double userPrevLat, userPrevLng;
//    double userCurrentLat, userCurrentLng;
//    boolean isAtStation;
//    String statusMessage;
//
//    ArrayList<Train_Schedule_Station_Structure> arrStation;
//    ArrayList<NearBy_Station_Structure> arrNearbyStations = new ArrayList<>();
//    NearBy_Station_Structure nearestStation;
//    Train_Schedule_Station_Structure  previousStation, currentStation, nextStation;
//
//    public Train_Tracking_Live_Structure_Class(double userCurrentLat, double userCurrentLng, ArrayList<Train_Schedule_Station_Structure> arrStation) {
//        this.userCurrentLat = userCurrentLat;
//        this.userCurrentLng = userCurrentLng;
//        this.arrStation = arrStation;
//    }
//
//
//   public void trackUser(){
//        arrNearbyStations.clear();
//        Location userLocation = new Location("userLocation");
//        userLocation.setLatitude(userCurrentLat);
//        userLocation.setLongitude(userCurrentLng);
//
//        // 1 - finding nearest station according to user location
//
//        for(Train_Schedule_Station_Structure st : arrStation){
//            Location stationLocation = new Location("stationLocation");
//            stationLocation.setLatitude(Double.parseDouble(st.getStnLat()));
//            stationLocation.setLongitude(Double.parseDouble(st.getStnLng()));
//
//            String stName = st.getStationName();
//            String stCode = st.getStationCode();
//            String stlat = st.getStnLat();
//            String stLng = st.getStnLng();
//            double distance = userLocation.distanceTo(stationLocation);
//            arrNearbyStations.add(new NearBy_Station_Structure(stName,stCode,stlat,stLng,distance));
//        }
//
//        // 2 - Sorting near by station array on the basis of nearest station to Farest station
//
//        Collections.sort(arrNearbyStations, (p1,p2) -> {
//            return Double.compare(p1.getStationDistance(), p2.getStationDistance());
//        });
//
//        // 3 - Getting first nearest station
//       nearestStation = arrNearbyStations.get(0);
//
//        if(nearestStation.getStationDistance()<=500){
//            // 4 -  Checking if train is present at station or not
//            isAtStation = true;
//            statusMessage = "Arrived " + nearestStation.getStationName();
////            Log.d("testtrackus", "isAtStation: " + isAtStation + ", Station Name: " + statusMessage);
//            return;
//        }else{
//            isAtStation = false;
//            int index = -1;
//            // 5 - finding nearest station index from the Main arrStation List array
//            for(int i=0; i<arrStation.size(); i++){
//               if (arrStation.get(i).getStationCode().equals(nearestStation.getStationCode())){
//                    index = i;
//                    break;
//               }
//           }
//
//           if(index >= 0 && index < arrStation.size()-2){
//                currentStation = arrStation.get(index);
//                nextStation = arrStation.get(index + 1);
//                double currentStationLat = Double.parseDouble(currentStation.getStnLat());
//                double currentStationLng = Double.parseDouble(currentStation.getStnLng());
//                double nextStationLat = Double.parseDouble(nextStation.getStnLat());
//                double nextStationLng = Double.parseDouble(nextStation.getStnLng());
//                // Finding distance between current station and the next upcoming station
//
//                float[] totalDistance = new float[1];
//                Location.distanceBetween(currentStationLat,currentStationLng,nextStationLat,nextStationLng,totalDistance);
//
//                // finding user distance from current passed station and her location
//
//                float[] userPointA = new float[1];
//                Location.distanceBetween(currentStationLat,currentStationLng,userCurrentLat,userCurrentLng,userPointA);
//
//                // finding user distance from her location to the upcoming station
//
//                float[] userPointB = new float[1];
//                Location.distanceBetween(userCurrentLat,userCurrentLng,nextStationLat,nextStationLng,userPointB);
//
//                // checking if user is near next station
//                if(userPointA[0] + userPointB[0] < (totalDistance[0]+500) && userPointA[0] + userPointB[0] >= totalDistance[0] -1500){
//                    statusMessage = (totalDistance[0] - userPointA[0] + userPointB[0]) + "Meters Away from reaching " + nextStation;
//                } else if (userPointA[0] < 1000) {
//                    statusMessage = "Departed from " + currentStation.getStationName();
//                }
//
//           }
//        }
//       Log.d("testtrackus", "isAtStation: " + isAtStation + ", msg: " + statusMessage);
//    }
//
//
//}



// gemini code
package com.gonakli.railradar.Structure_Class;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Train_Tracking_Live_Structure_Class {

    // ===== Configurable thresholds (meters) =====
    private static final float ARRIVED_THRESHOLD = 200f;
    private static final float APPROACHING_THRESHOLD = 1000f;
    private static final float DEPARTED_THRESHOLD = 1000f;
    private static final float SEGMENT_TOLERANCE = 500f;      // GPS drift allowance for segment matching
    private static final float OFF_ROUTE_THRESHOLD = 3000f;   // isse zyada door hai toh "not in train"

    double userCurrentLat, userCurrentLng;
    boolean isAtStation;
    boolean isOnRoute;
    String statusMessage;

    ArrayList<Train_Schedule_Station_Structure> arrStation;          // original full list
    ArrayList<Train_Schedule_Station_Structure> validStations;       // sirf jinke lat/lng valid hain
    ArrayList<NearBy_Station_Structure> arrNearbyStations = new ArrayList<>();
    NearBy_Station_Structure nearestStation;
    Train_Schedule_Station_Structure currentStation, nextStation;

    public Train_Tracking_Live_Structure_Class(double userCurrentLat, double userCurrentLng, ArrayList<Train_Schedule_Station_Structure> arrStation) {
        this.userCurrentLat = userCurrentLat;
        this.userCurrentLng = userCurrentLng;
        this.arrStation = arrStation;
    }

    public void trackUser() {
        if (arrStation == null || arrStation.isEmpty()) {
            isAtStation = false;
            isOnRoute = false;
            statusMessage = "No station data available";
            return;
        }

        // Step 0: sirf wahi stations lo jinke lat/lng valid hain (CP, NCTP jaise missing-coord stations skip)
        validStations = filterValidStations(arrStation);

        if (validStations.size() < 2) {
            isOnRoute = false;
            statusMessage = "Insufficient station location data";
            return;
        }

        arrNearbyStations.clear();
        Location userLocation = new Location("userLocation");
        userLocation.setLatitude(userCurrentLat);
        userLocation.setLongitude(userCurrentLng);

        // Step 1: har valid station ka distance nikalo (sirf "arrived" check ke liye)
        for (Train_Schedule_Station_Structure st : validStations) {
            double lat = Double.parseDouble(st.getStnLat());
            double lng = Double.parseDouble(st.getStnLng());

            Location stationLocation = new Location("stationLocation");
            stationLocation.setLatitude(lat);
            stationLocation.setLongitude(lng);

            double distance = userLocation.distanceTo(stationLocation);
            arrNearbyStations.add(new NearBy_Station_Structure(
                    st.getStationName(), st.getStationCode(), st.getStnLat(), st.getStnLng(), distance));
        }

        Collections.sort(arrNearbyStations, (p1, p2) -> Double.compare(p1.getStationDistance(), p2.getStationDistance()));
        nearestStation = arrNearbyStations.get(0);

        // Step 2: Arrived check
        if (nearestStation.getStationDistance() <= ARRIVED_THRESHOLD) {
            isAtStation = true;
            isOnRoute = true;
            statusMessage = "Arrived at " + nearestStation.getStationName();
            Log.d("testtrackus", statusMessage);
            return;
        }

        isAtStation = false;

        // Step 3: Har consecutive valid-station pair (segment) check karo
        boolean segmentFound = false;
        float minPerpendicularDistance = Float.MAX_VALUE;

        for (int i = 0; i < validStations.size() - 1; i++) {
            Train_Schedule_Station_Structure stationA = validStations.get(i);
            Train_Schedule_Station_Structure stationB = validStations.get(i + 1);

            double aLat = Double.parseDouble(stationA.getStnLat());
            double aLng = Double.parseDouble(stationA.getStnLng());
            double bLat = Double.parseDouble(stationB.getStnLat());
            double bLng = Double.parseDouble(stationB.getStnLng());

            float[] totalDistance = new float[1];
            Location.distanceBetween(aLat, aLng, bLat, bLng, totalDistance);

            float[] userToA = new float[1];
            Location.distanceBetween(aLat, aLng, userCurrentLat, userCurrentLng, userToA);

            float[] userToB = new float[1];
            Location.distanceBetween(userCurrentLat, userCurrentLng, bLat, bLng, userToB);

            float pathSum = userToA[0] + userToB[0];

            // Route se off-route distance track karo (nearest segment tak ka approx gap)
            float approxPerpendicularGap = pathSum - totalDistance[0]; // 0 ke jitna kareeb utna route ke upar
            if (approxPerpendicularGap < minPerpendicularDistance) {
                minPerpendicularDistance = approxPerpendicularGap;
            }

            // User isi segment ke beech hai kya
            if (pathSum <= totalDistance[0] + SEGMENT_TOLERANCE) {
                currentStation = stationA;
                nextStation = stationB;
                segmentFound = true;

                if (userToA[0] <= DEPARTED_THRESHOLD) {
                    // Abhi station A se nikli hai (1000m tak "departed" dikhega)
                    isOnRoute = true;
                    statusMessage = "Departed from " + currentStation.getStationName();
                } else if (userToB[0] <= APPROACHING_THRESHOLD) {
                    // Station B ke 1000m ke andar approach kar rahi hai
                    isOnRoute = true;
                    statusMessage = Math.round(userToB[0]) + " m away from " + nextStation.getStationName();
                } else {
                    // Beech mein kahin hai, dono thresholds ke bahar
                    isOnRoute = true;
                    statusMessage = "In transit";
                }
                break;
            }
        }

        // Step 4: Off-route check — agar koi segment match hi nahi hua, ya perpendicular gap bahut zyada hai
        if (!segmentFound || minPerpendicularDistance > OFF_ROUTE_THRESHOLD) {
            isOnRoute = false;
            statusMessage = "User is not in the train";
        }

        Log.d("testtrackus", "isAtStation: " + isAtStation + ", isOnRoute: " + isOnRoute + ", msg: " + statusMessage);
    }

    /**
     * Sirf wo stations return karta hai jinke lat/lng valid parse ho sakte hain.
     * CP, NCTP jaise stations (missing coords) is list se bahar reh jaayenge.
     */
    private ArrayList<Train_Schedule_Station_Structure> filterValidStations(ArrayList<Train_Schedule_Station_Structure> input) {
        ArrayList<Train_Schedule_Station_Structure> result = new ArrayList<>();
        for (Train_Schedule_Station_Structure st : input) {
            try {
                String latStr = st.getStnLat();
                String lngStr = st.getStnLng();
                if (latStr == null || lngStr == null || latStr.trim().isEmpty() || lngStr.trim().isEmpty()) {
                    continue; // skip missing coords
                }
                double lat = Double.parseDouble(latStr);
                double lng = Double.parseDouble(lngStr);
                if (lat == 0.0 && lng == 0.0) {
                    continue; // skip 0,0 (invalid/default coords)
                }
                result.add(st);
            } catch (NumberFormatException e) {
                // skip unparseable coords
            }
        }
        return result;
    }
}