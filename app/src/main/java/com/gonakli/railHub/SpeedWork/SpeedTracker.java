package com.gonakli.railHub.SpeedWork;

import android.location.Location;

public class SpeedTracker {
    private static double startLat;
    private static double startLng;
    private static double endLat;
    private static double endLng;
    private static long startTime;
    private static long endTime;
    private long duration;

    public int getSpeed(double currentLat, double currentLng){
        if(startLat == 0.0 && startLng == 0.0){
            startLat = currentLat;
            startLng = currentLng;
            endLat = currentLat;
            endLng = currentLng;
            startTime = System.currentTimeMillis();
            return 0;
        }
            startLat = endLat;
            startLng = endLng;
            endLat = currentLat;
            endLng = currentLng;
            endTime = System.currentTimeMillis();
            duration = (endTime - startTime);
            startTime = endTime;
            if(duration <=0){
                return 0;
            }

        float[] results = new float[3];
        Location.distanceBetween(startLat,startLng,endLat,endLng,results);
        float currentSpeed = ((results[0] * 3600 ) / (duration));
        return (int) currentSpeed;
    }

    public void clearState(){
        startLat = 0.0;
        startLng = 0.0;
        endLat = 0.0;
        endLng = 0.0;
        startTime = 0;
        endTime = 0;
    }
}
