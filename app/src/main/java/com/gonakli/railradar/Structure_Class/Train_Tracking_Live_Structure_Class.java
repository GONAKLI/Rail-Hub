package com.gonakli.railradar.Structure_Class;

import android.content.Intent;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

public class Train_Tracking_Live_Structure_Class {

    private static final float ARRIVED_THRESHOLD = 300f;       // Inside station radius (200m)
    private static final float DEPARTED_THRESHOLD = 1000f;      // Departed window (1 KM)
    private static final float APPROACHING_THRESHOLD = 1000f;   // Approaching window (1 KM)
    private static final float OFF_ROUTE_THRESHOLD = 40000f;     // Max tolerance from polyline track (1.5 KM)
    private static final String TAG = "myTrackedUser";

    private final double userLat, userLng;
    private final ArrayList<Train_Schedule_Station_Structure> arrStationsList;
    private final ArrayList<Track_Polyline_Point_Structure> arrPolylinePoints;

    private final ArrayList<Train_Schedule_Station_Structure> arrValidStationsList = new ArrayList<>();

    private String statusMessage;
    private boolean isAtStation, isOnRoute, segmentFound;
    private int stationCoveredPercentage, totalJourneyCovered;
    private final String totalTrainJourney;
    private final String sourceStation, destinationStation;

    private Train_Schedule_Station_Structure previousStation, nextStation, currentStation;

    public Train_Tracking_Live_Structure_Class(double userLat, double userLng,
                                               ArrayList<Train_Schedule_Station_Structure> arrStationsList,
                                               ArrayList<Track_Polyline_Point_Structure> arrPolylinePoints) {
        this.userLat = userLat;
        this.userLng = userLng;
        this.arrStationsList = arrStationsList;
        this.arrPolylinePoints = arrPolylinePoints;
        this.sourceStation = arrStationsList.get(0).getStationName();
        this.destinationStation = arrStationsList.get(arrStationsList.size()-1).getStationName();
        this.totalTrainJourney = arrStationsList.get(arrStationsList.size()-1).getDistance();
    }

    public void trackMyUserTrain() {
        // Step 0: State Reset
        segmentFound = false;
        isAtStation = false;
        isOnRoute = false;
        previousStation = null;
        nextStation = null;
        currentStation = null;
        stationCoveredPercentage = 0;
        totalJourneyCovered = 0;
        statusMessage = "";

        if (arrStationsList == null || arrStationsList.isEmpty()) {
            isOnRoute = false;
            statusMessage = "No station data available";
            return;
        }

        buildValidStationsList();

        if (arrValidStationsList.size() < 2) {
            isOnRoute = false;
            statusMessage = "Insufficient station location data";
            return;
        }

        // STEP 1: POLYLINE PAR CHECK KAREIN KI USER TRACK PAR HAI YA NAHI (On-Route Validation)
        boolean userOnTrack = checkIsUserOnPolylineRoute();
        if (!userOnTrack) {
            isOnRoute = false;
            isAtStation = false;
            statusMessage = "We detected, You are not inside train";
            logCurrentState();
            return;
        }

        isOnRoute = true;

        // STEP 2: DIRECT STATION GPS BASED ZERO-LAG TRACKING & STATUS
        trackUserByDirectStationCoordinates();

        logCurrentState();
    }

    private void buildValidStationsList() {
        arrValidStationsList.clear();
        for (Train_Schedule_Station_Structure st : arrStationsList) {
            if (st == null) continue;
            String stLatStr = st.getStnLat();
            String stLngStr = st.getStnLng();
            if (stLatStr == null || stLngStr == null || stLatStr.trim().isEmpty() || stLngStr.trim().isEmpty()) {
                continue;
            }
            double stLat = parseDoubleSafe(stLatStr);
            double stLng = parseDoubleSafe(stLngStr);
            if (stLat == 0.0 && stLng == 0.0) continue;

            arrValidStationsList.add(st);
        }
    }

    /**
     * Polyline Track Check: Verifies if user is within 1.5 KM of the curved track polyline.
     */
    private boolean checkIsUserOnPolylineRoute() {
        if (arrPolylinePoints == null || arrPolylinePoints.size() < 2) {
            // Fallback: Agar Polyline points na ho toh true maan lein
            return true;
        }

        double minDistanceMeters = Double.MAX_VALUE;

        for (int i = 0; i < arrPolylinePoints.size() - 1; i++) {
            Track_Polyline_Point_Structure p1 = arrPolylinePoints.get(i);
            Track_Polyline_Point_Structure p2 = arrPolylinePoints.get(i + 1);

            double[] proj = pointToSegmentProjection(userLat, userLng, p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
            if (proj[0] < minDistanceMeters) {
                minDistanceMeters = proj[0];
            }
        }

        return minDistanceMeters <= OFF_ROUTE_THRESHOLD;
    }

    /**
     * Direct Station GPS Matching: Instant Segment & Status Determination.
     */
    private void trackUserByDirectStationCoordinates() {
        double minPerpendicularDistance = Double.MAX_VALUE;
        int bestSegmentIdx = -1;
        double bestFraction = 0;

        // Find best station pair (A -> B) using User GPS Projection
        for (int i = 0; i < arrValidStationsList.size() - 1; i++) {
            Train_Schedule_Station_Structure stA = arrValidStationsList.get(i);
            Train_Schedule_Station_Structure stB = arrValidStationsList.get(i + 1);

            double aLat = parseDoubleSafe(stA.getStnLat());
            double aLng = parseDoubleSafe(stA.getStnLng());
            double bLat = parseDoubleSafe(stB.getStnLat());
            double bLng = parseDoubleSafe(stB.getStnLng());

            double[] proj = pointToSegmentProjection(userLat, userLng, aLat, aLng, bLat, bLng);
            double perpDistMeters = proj[0];
            double fraction = proj[1];

            // Allow slight boundary overrun (-0.05 to 1.05)
            if (fraction >= -0.05 && fraction <= 1.05) {
                if (perpDistMeters < minPerpendicularDistance) {
                    minPerpendicularDistance = perpDistMeters;
                    bestSegmentIdx = i;
                    bestFraction = Math.max(0.0, Math.min(1.0, fraction));
                }
            }
        }

        if (bestSegmentIdx == -1) {
            // Edge fallback
            bestSegmentIdx = 0;
            bestFraction = 0.0;
        }

        Train_Schedule_Station_Structure stationA = arrValidStationsList.get(bestSegmentIdx);
        Train_Schedule_Station_Structure stationB = arrValidStationsList.get(bestSegmentIdx + 1);

        float[] distToA = new float[1];
        Location.distanceBetween(userLat, userLng, parseDoubleSafe(stationA.getStnLat()), parseDoubleSafe(stationA.getStnLng()), distToA);

        float[] distToB = new float[1];
        Location.distanceBetween(userLat, userLng, parseDoubleSafe(stationB.getStnLat()), parseDoubleSafe(stationB.getStnLng()), distToB);

        // Journey Calculations
        double aKm = parseDoubleSafe(stationA.getDistance());
        double bKm = parseDoubleSafe(stationB.getDistance());
        double currentJourneyKm = aKm + bestFraction * (bKm - aKm);

        previousStation = stationA;
        nextStation = stationB;
        segmentFound = true;

        stationCoveredPercentage = (int) Math.round(bestFraction * 100);
        totalJourneyCovered = (int) Math.round(currentJourneyKm);

        // --- STATUS DECISION LOGIC (DIRECT GROUND GPS) ---

        // 1. ARRIVED AT STATION B (Inside 200m)
        if (distToB[0] <= ARRIVED_THRESHOLD) {
            isAtStation = true;
            currentStation = stationB;
            statusMessage = "Arrived at: " + stationB.getStationName();
            stationCoveredPercentage = 100;
            totalJourneyCovered = (int) Math.round(bKm);
            return;
        }

        // 2. ARRIVED AT STATION A (Inside 200m)
        if (distToA[0] <= ARRIVED_THRESHOLD) {
            isAtStation = true;
            currentStation = stationA;
            statusMessage = "Arrived at: " + stationA.getStationName();
            stationCoveredPercentage = 0;
            totalJourneyCovered = (int) Math.round(aKm);
            return;
        }

        isAtStation = false;
        currentStation = null;

        // 3. DEPARTED FROM STATION A (Crossed A by > 100m AND within 1 KM of A)
        if (distToA[0] > 100f && distToA[0] <= DEPARTED_THRESHOLD) {
            statusMessage = "Departed from " + stationA.getStationName();
        }
        // 4. AWAY FROM NEXT STATION B (Within 1 KM of B)
        else if (distToB[0] <= APPROACHING_THRESHOLD) {
            statusMessage = Math.round(distToB[0]) + " m away from " + stationB.getStationName();
        }
        // 5. IN TRANSIT (Midway)
        else {
            // statusMessage = "In Transit";
            double diff = Double.parseDouble(nextStation.getDistance()) - totalJourneyCovered;
            statusMessage = diff + " Km away from " + nextStation.getStationName();
        }
    }

    private double[] pointToSegmentProjection(double pLat, double pLng, double lat1, double lng1, double lat2, double lng2) {
        double latMid = Math.toRadians((lat1 + lat2) / 2.0);
        double metersPerDegreeLat = 111000.0;
        double metersPerDegreeLng = 111000.0 * Math.cos(latMid);

        double dx = (lng2 - lng1) * metersPerDegreeLng;
        double dy = (lat2 - lat1) * metersPerDegreeLat;

        double px = (pLng - lng1) * metersPerDegreeLng;
        double py = (pLat - lat1) * metersPerDegreeLat;

        double lenSq = dx * dx + dy * dy;
        double fraction = (lenSq != 0) ? (px * dx + py * dy) / lenSq : 0.0;

        double closestLat = lat1 + Math.max(0.0, Math.min(1.0, fraction)) * (lat2 - lat1);
        double closestLng = lng1 + Math.max(0.0, Math.min(1.0, fraction)) * (lng2 - lng1);

        float[] distResult = new float[1];
        Location.distanceBetween(pLat, pLng, closestLat, closestLng, distResult);

        return new double[]{distResult[0], fraction};
    }

    private double parseDoubleSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(val.trim());
        } catch (Exception e) {
            Log.w(TAG, "Error parsing value: " + val, e);
            return 0.0;
        }
    }

    private void logCurrentState() {
        Log.d(TAG, "trackMyUserTrain: \n"
                + "isOnRoute: " + isOnRoute + "\nisAtStation: " + isAtStation
                + "\nstatusMessage: " + statusMessage
                + "\npreviousStation: " + (previousStation != null ? previousStation.getStationName() : "null")
                + "\nnextStation: " + (nextStation != null ? nextStation.getStationName() : "null")
                + "\ncurrentStation: " + (currentStation != null ? currentStation.getStationName() : "null")
                + "\nprogress: " + stationCoveredPercentage + "% | journeyKm: " + totalJourneyCovered);
    }

    public String getStatusMessage() { return statusMessage; }
    public boolean isAtStation() { return isAtStation; }
    public boolean isOnRoute() { return isOnRoute; }
    public Train_Schedule_Station_Structure getPreviousStation() { return previousStation; }
    public Train_Schedule_Station_Structure getNextStation() { return nextStation; }
    public Train_Schedule_Station_Structure getCurrentStation() { return currentStation; }
    public int getStationCoveredPercentage() { return stationCoveredPercentage; }
    public int getTotalJourneyCovered() { return totalJourneyCovered; }

    public int getTotalJourneyCoveredPercentage(){ return ((totalJourneyCovered/ Integer.parseInt(totalTrainJourney)) *100);}
    public int getTotalTrainJourney() { return Integer.parseInt(totalTrainJourney);}
    public String getDestinationStation() {
        return destinationStation;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public Train_Tracking_Structure getReport(){
        return new Train_Tracking_Structure(
                getPreviousStation(), getCurrentStation(), getNextStation(),
                getStatusMessage(),isOnRoute(),isAtStation(),getStationCoveredPercentage(),
                getTotalJourneyCovered(), getTotalJourneyCoveredPercentage(),getTotalTrainJourney(),
                getSourceStation(),getDestinationStation()
                );
    }
}