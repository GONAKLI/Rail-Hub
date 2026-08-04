package com.gonakli.railradar.Structure_Class;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

public class Train_Tracking_Live_Structure_Class {

    private static final float ARRIVED_THRESHOLD = 200f;
    private static final float APPROACHING_THRESHOLD = 1000f;
    private static final float DEPARTED_THRESHOLD = 1000f;
    private static final float SEGMENT_TOLERANCE = 500f;
    private static final float OFF_ROUTE_THRESHOLD = 3000f;
    private static final String TAG = "myTrackedUser";

    private final double userLat, userLng;
    private final ArrayList<Train_Schedule_Station_Structure> arrStationsList;

    private final ArrayList<NearBy_Station_Structure> arrNearbyStationsList = new ArrayList<>();
    private final ArrayList<Train_Schedule_Station_Structure> arrValidStationsList = new ArrayList<>();

    private String statusMessage;
    private boolean isAtStation, isOnRoute, segmentFound;
    private int stationCoveredPercentage, totalJourneyCovered;

    private Train_Schedule_Station_Structure previousStation, nextStation, currentStation;

    public Train_Tracking_Live_Structure_Class(double userLat, double userLng,
                                               ArrayList<Train_Schedule_Station_Structure> arrStationsList) {
        this.userLat = userLat;
        this.userLng = userLng;
        this.arrStationsList = arrStationsList;
    }

    public void trackMyUserTrain() {
        segmentFound = false;

        if (arrStationsList == null || arrStationsList.isEmpty()) {
            isOnRoute = false;
            isAtStation = false;
            statusMessage = "No station data available";
            return;
        }

        buildValidAndNearbyStationLists();
        sortNearbyStationsByDistance();

        if (!hasEnoughStationsToTrack()) {
            return;
        }

        NearBy_Station_Structure nearestStation = arrNearbyStationsList.get(0);

        if (nearestStation.getStationDistance() <= ARRIVED_THRESHOLD) {
            for (int i=0; i<arrValidStationsList.size(); i++){
                if(arrValidStationsList.get(i).getStationCode() == nearestStation.getStationCode()){
                    currentStation = arrValidStationsList.get(i);
                    if(i >0 && arrValidStationsList.get(i-1) !=  null){
                        previousStation = arrValidStationsList.get(i-1);
                    }
                    if(i<arrValidStationsList.size() -1 ){
                        nextStation = arrValidStationsList.get(i+1);
                    }
                    break;
                }
            }
            isAtStation = true;
            isOnRoute = true;
            statusMessage = "Arrived at: " + nearestStation.getStationName();
            logCurrentState();
            return;
        }

        isAtStation = false;
        findCurrentSegmentAndStatus();
        logCurrentState();
    }

    private void buildValidAndNearbyStationLists() {
        arrNearbyStationsList.clear();
        arrValidStationsList.clear();
        try {
            for (Train_Schedule_Station_Structure st : arrStationsList) {
                String stLatStr = st.getStnLat();
                String stLngStr = st.getStnLng();
                if (stLatStr == null || stLngStr == null
                        || stLatStr.trim().isEmpty() || stLngStr.trim().isEmpty()) {
                    continue;
                }
                double stLat = Double.parseDouble(stLatStr);
                double stLng = Double.parseDouble(stLngStr);
                if (stLat == 0.0 && stLng == 0.0) {
                    continue;
                }

                arrValidStationsList.add(st);

                float[] resultValue = new float[1];
                Location.distanceBetween(userLat, userLng, stLat, stLng, resultValue);
                arrNearbyStationsList.add(new NearBy_Station_Structure(
                        st.getStationName(), st.getStationCode(), stLatStr, stLngStr, resultValue[0]));
            }
        } catch (NumberFormatException e) {
            Log.w(TAG, "Skipping station with invalid coordinate format", e);
        }
    }

    private void sortNearbyStationsByDistance() {
        arrNearbyStationsList.sort((s1, s2) -> Double.compare(s1.getStationDistance(), s2.getStationDistance()));
    }

    private boolean hasEnoughStationsToTrack() {
        if (arrNearbyStationsList.size() < 2) {
            isOnRoute = false;
            statusMessage = "Insufficient station location data";
            return false;
        }
        return true;
    }

    private void findCurrentSegmentAndStatus() {
        float minPerpendicularGap = Float.MAX_VALUE;

        for (int i = 0; i < arrValidStationsList.size() - 1; i++) {
            Train_Schedule_Station_Structure stationA = arrValidStationsList.get(i);
            Train_Schedule_Station_Structure stationB = arrValidStationsList.get(i + 1);

            double aLat = Double.parseDouble(stationA.getStnLat());
            double aLng = Double.parseDouble(stationA.getStnLng());
            double bLat = Double.parseDouble(stationB.getStnLat());
            double bLng = Double.parseDouble(stationB.getStnLng());

            float[] totalDistance = new float[1];
            Location.distanceBetween(aLat, aLng, bLat, bLng, totalDistance);

            float[] userToA = new float[1];
            Location.distanceBetween(aLat, aLng, userLat, userLng, userToA);

            float[] userToB = new float[1];
            Location.distanceBetween(userLat, userLng, bLat, bLng, userToB);

            float pathSum = userToA[0] + userToB[0];
            float approxPerpendicularGap = pathSum - totalDistance[0];
            if (approxPerpendicularGap < minPerpendicularGap) {
                minPerpendicularGap = approxPerpendicularGap;
            }


            int actualDistanceInMeteres = (Integer.parseInt(stationB.getDistance()) - Integer.parseInt(stationA.getDistance())) * 1000;
            float dynamicTolerance = getDynamicTolerance(actualDistanceInMeteres, totalDistance);


            if (pathSum <= totalDistance[0] + SEGMENT_TOLERANCE + dynamicTolerance) {
                previousStation = stationA;
                nextStation = stationB;
                segmentFound = true;
                isOnRoute = true;
                stationCoveredPercentage = Math.round((pathSum/totalDistance[0]) * 100);
                totalJourneyCovered = Math.round(Integer.parseInt(previousStation.getDistance())+userToA[0]);

                if (userToA[0] <= DEPARTED_THRESHOLD) {
                    statusMessage = "Departed from " + previousStation.getStationName();
                } else if (userToB[0] <= APPROACHING_THRESHOLD) {
                    statusMessage = Math.round(userToB[0]) + " m away from " + nextStation.getStationName();
                } else {
                    statusMessage = "In Transit";
                }
                break;
            }
        }

        if (!segmentFound) {
            isOnRoute = false;
            statusMessage = "User is not in train";
        }
    }

    private static float getDynamicTolerance(int actualDistanceInMeteres, float[] totalDistance) {
        float dynamicTolerance = Float.MIN_VALUE;
        if(actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 30000){
            dynamicTolerance = 25000;
        }else if(actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 20000){
            dynamicTolerance = 15000;
        } else if (actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 10000) {
            dynamicTolerance =6500;
        }else if(actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 5000){
            dynamicTolerance = 3000;
        } else if (actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 3000) {
            dynamicTolerance = 1500;
        } else if (actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 1500) {
            dynamicTolerance = 700;
        } else if (actualDistanceInMeteres - (totalDistance[0] + SEGMENT_TOLERANCE) > 700) {
            dynamicTolerance = 200;
        }
        return dynamicTolerance;
    }

    private void logCurrentState() {
        Log.d(TAG, "trackMyUserTrain: \n"
                + "isOnRoute: " + isOnRoute + "\nisAtStation: " + isAtStation
                + "\nstatusMessage: " + statusMessage
                + "\npreviousStation: " + (previousStation != null ? previousStation.getStationName() : "null")
                + "\nnextStation: " + (nextStation != null ? nextStation.getStationName() : "null")
                + "\ncurrentStation: " + (currentStation !=null ? currentStation.getStationName() : "null") );
    }


    public String getStatusMessage() { return statusMessage; }
    public boolean isAtStation() { return isAtStation; }
    public boolean isOnRoute() { return isOnRoute; }
    public Train_Schedule_Station_Structure getPreviousStation() { return previousStation; }
    public Train_Schedule_Station_Structure getNextStation() { return nextStation; }
    public Train_Schedule_Station_Structure getCurrentStation(){return currentStation; }
    public int getStationCoveredPercentage(){ return stationCoveredPercentage; }
    public int getTotalJourneyCovered(){ return totalJourneyCovered; }


}

