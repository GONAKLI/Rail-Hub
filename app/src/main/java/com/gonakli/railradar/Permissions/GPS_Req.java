package com.gonakli.railradar.Permissions;

import android.app.Activity;
import android.app.LocaleManager;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;

public class GPS_Req {
    Activity activity;
    public static final  int REQ_CODE = 25;
    public GPS_Req(Activity activity){
        this.activity = activity;
    }

    public boolean gpsChecker(){
        LocationManager locationManager =(LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
        boolean isEnabled =locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if(isEnabled){
            return true;
        }else{
            try{
                LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build();
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                LocationServices.getSettingsClient(activity)
                        .checkLocationSettings(builder.build())
                        .addOnFailureListener(activity, e -> {
                            if (e instanceof ResolvableApiException) {
                                try {
                                    // Ye line screen par "OK / Cancel" wala pop-up khol degi
                                    ((ResolvableApiException) e).startResolutionForResult(activity, REQ_CODE);
                                } catch (IntentSender.SendIntentException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
