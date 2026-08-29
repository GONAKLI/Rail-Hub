package com.gonakli.railHub.Services.LocationService;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;

import android.location.Location;

import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;


public class myLocationServiceClass extends Service {
    public static final String ACTION_LOCATION_UPDATE = "LOCATION_UPDATE";
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private double latitude;
    private  double longitude;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       fetch_Location();
        Log.d("Serviceclass", "onStartCommand: come in service");
        return START_NOT_STICKY;
    }

    @SuppressLint("MissingPermission")
    public void fetch_Location(){
        Log.d("Serviceclass", "onStartCommand: come in fetch_location");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                10000
        )
                .setMinUpdateIntervalMillis(5000)
                .build();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
               locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                       for(Location location : locationResult.getLocations()){
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            sendLocationUpdate(latitude,longitude);
                        }
                       }
                    }
                },Looper.getMainLooper()
        );
    }



    // Broadcast Receiver

    public void sendLocationUpdate(double lat, double lng){

        Log.d("Serviceclass", "onStartCommand: come in send location update " + lat + lng );
        Intent intent = new Intent(ACTION_LOCATION_UPDATE);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fusedLocationProviderClient !=null && locationCallback != null){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}
