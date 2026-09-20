package com.gonakli.railHub.StartupPermissions;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;

public class StartupLocationPermission {

    private final String[] permissionList = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int REQ_CODE = 420;

    public void getStartupLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissionList, REQ_CODE);
    }
}
