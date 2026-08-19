package com.gonakli.railradar.Permissions;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.gonakli.railradar.ExtraOptions.NearBy_Station_Activity;
import com.gonakli.railradar.R;

public class Location_Permissions {
    Activity activity;
    public static final int REQ_CODE = 10;
    Dialog dialog;
    String[] permissionsList ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public Location_Permissions(Activity activity){
        this.activity = activity;
    }

    public boolean checkPermission() {
        boolean isPermissionGranted = true;
        for (String permission : permissionsList) {
            if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                isPermissionGranted = false;
                break;
            }
        }
        if (isPermissionGranted) {
            return true;
        }
        ActivityCompat.requestPermissions(activity, permissionsList, REQ_CODE);
        return false;
    }

    public void showSettingDialog(){
            dialog = new Dialog(activity);
            dialog.setContentView(R.layout.location_permission_dialog);
            dialog.setCanceledOnTouchOutside(false);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
            ImageButton crossIcon = dialog.findViewById(R.id.locationPermissionDialogueCross);
            Button enablePermissionBtn = dialog.findViewById(R.id.locationPermissionDialogueEnableLocation);
            crossIcon.setOnClickListener(v -> {
                dialog.dismiss();
                Toast.makeText(activity, "Location Permission denied !!", Toast.LENGTH_SHORT).show();

            });
            enablePermissionBtn.setOnClickListener(v -> {
                Intent iSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                iSettings.setData(Uri.fromParts("package", activity.getPackageName(), null));
                activity.startActivity(iSettings);
                dialog.dismiss();
            });
            dialog.show();
    }

    public void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        Log.d("locatPerm", "handlePermissionResult: " + requestCode);
        if(requestCode == REQ_CODE){
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    showSettingDialog();
                    break;
                }
            }


        }
    }


}