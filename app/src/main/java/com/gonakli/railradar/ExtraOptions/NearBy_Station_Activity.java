package com.gonakli.railradar.ExtraOptions;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.QueryLocationException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.gonakli.railradar.ADAPTERS.NearBy_Station_ListView_Adapter;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.HomeActivity.Home_Screen_Activity;
import com.gonakli.railradar.Permissions.GPS_Req;
import com.gonakli.railradar.Permissions.Location_Permissions;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.NearBy_Station_Structure;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class NearBy_Station_Activity extends AppCompatActivity {

    ArrayList<NearBy_Station_Structure> arrNearByStations = new ArrayList<>();
    Toolbar toolbar;
    ListView nearbyStationListView;
    NearBy_Station_ListView_Adapter adapter;
    Button nearbyListRefreshBtn;
    ProgressBar nearbyStationProgressBar;
    Dialog dialog, gpsDialog ;
    Location_Permissions locationPermissionsObj;
    GPS_Req gpsReqObj;
    double latitude, longitude;
    LinearLayout no_NearBy_Station_Found_Container, nearbyListHeadingContainer;
    TextView nearbyListTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_stations_layout);
        find_all_id();
        set_toolbar();
        action_on_refresh_btn();
        permissionCheckpoint();

    }

    private void permissionCheckpoint() {
    locationPermissionsObj = new Location_Permissions(NearBy_Station_Activity.this);
    gpsReqObj = new GPS_Req(NearBy_Station_Activity.this);
    boolean isPermissionGiven = locationPermissionsObj.checkPermission();
    if(!isPermissionGiven) return;
    if(!gpsReqObj.gpsChecker()) return;
    final_location_fetch();

    }

    private void action_on_refresh_btn() {
        nearbyListRefreshBtn.setOnClickListener(v -> {
            arrNearByStations.clear();
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
            permissionCheckpoint();

        });
    }


    private void displayNearByStations() {
        Collections.sort(arrNearByStations, (s1,s2) -> Double.compare(s1.getStationDistance(), s2.getStationDistance()) );
        if(!arrNearByStations.isEmpty()){
            adapter = new NearBy_Station_ListView_Adapter(NearBy_Station_Activity.this, arrNearByStations);
            nearbyStationListView.setAdapter(adapter);
            no_NearBy_Station_Found_Container.setVisibility(View.GONE);
            nearbyListHeadingContainer.setVisibility(View.VISIBLE);
            nearbyListTitle.setVisibility(View.VISIBLE);
        }else{
            no_NearBy_Station_Found_Container.setVisibility(View.VISIBLE);
            nearbyListHeadingContainer.setVisibility(View.GONE);
            nearbyListTitle.setVisibility(View.GONE);
        }
        nearbyStationProgressBar.setVisibility(View.GONE);

        nearbyListRefreshBtn.setEnabled(true);
        nearbyListRefreshBtn.setBackgroundResource(android.R.drawable.btn_default);
        nearbyListRefreshBtn.setText("Refresh Stations");


    }

    private void findNearByStations() {
        arrNearByStations.clear();
        Station_List_DB_Helper dbHelper = new Station_List_DB_Helper(NearBy_Station_Activity.this);
        ArrayList<Station_List_Structure> arrStations = dbHelper.getStationList();
        dbHelper.close();
        if(latitude == 0.0d && longitude == 0.0d){
            permissionCheckpoint();
            return;
        }
        Location startPoint = new Location("startPoint");
        startPoint.setLatitude(latitude);
        startPoint.setLongitude(longitude);
        for(Station_List_Structure item : arrStations){
            Location endPoint = new Location("endPoint");
            double latD = Double.parseDouble(item.getStation_Lat());
            double lngD = Double.parseDouble(item.getStation_Lng());
            endPoint.setLatitude(latD);
            endPoint.setLongitude(lngD);

            double distanceInMeteres = startPoint.distanceTo(endPoint);
            double distanceInKm = distanceInMeteres/1000;

            if(distanceInKm <= 100){
                String StName = item.getStation_Name();
                String StCode = item.getStation_Code();
                String StLat = item.getStation_Lat();
                String StLng = item.getStation_Lng();
                double StDistance = distanceInKm;
                arrNearByStations.add(new NearBy_Station_Structure(StName,StCode,StLat,StLng,StDistance));
                Log.d("log2", "findNearByStations: " + distanceInKm + " : " + item.getStation_Name());
            }

        }
        displayNearByStations();


    }

    private void set_toolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Nearby Stations");
        toolbar.setBackgroundColor(Color.GRAY);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void find_all_id() {
        toolbar = findViewById(R.id.application_custom_toolbar);
        nearbyStationListView = findViewById(R.id.nearbyStationListView);
        nearbyListRefreshBtn = findViewById(R.id.nearbyListRefreshBtn);
        nearbyStationProgressBar = findViewById(R.id.nearbyStationProgressBar);
        no_NearBy_Station_Found_Container = findViewById(R.id.no_NearBy_Station_Found_Container);
        nearbyListHeadingContainer = findViewById(R.id.nearbyListHeadingContainer);
        nearbyListTitle = findViewById(R.id.nearbyListTitle);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    public void final_location_fetch(){
        nearbyListRefreshBtn.setEnabled(false);
        nearbyListRefreshBtn.setBackgroundColor(Color.RED);
        nearbyListRefreshBtn.setText("Loading ...");

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                nearbyStationProgressBar.setVisibility(View.VISIBLE);
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    try{
                    locationManager.getCurrentLocation(
                            LocationManager.GPS_PROVIDER,
                            null,
                            getMainExecutor(),
                            location -> {
                                if(location != null){
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    findNearByStations();
                                }else{
                                    Toast.makeText(this, "GPS signals Weak, Please Wait", Toast.LENGTH_SHORT).show();
                                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                                    fusedLocationProviderClient.getLastLocation()
                                            .addOnSuccessListener(location1 -> {
                                                if(location1 != null){
                                                    latitude = location1.getLatitude();
                                                    longitude = location1.getLongitude();
                                                    findNearByStations();
                                                }else{
                                                    nearbyStationProgressBar.setVisibility(View.GONE);
                                                    nearbyListRefreshBtn.setEnabled(true);
                                                    nearbyListRefreshBtn.setBackgroundResource(android.R.drawable.btn_default);
                                                    nearbyListRefreshBtn.setText("Refresh Stations");
                                                }

                                            });

                                }

                            }
                    );
                    }catch (Exception e){
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "! Unexpected error, try again !!", Snackbar.ANIMATION_MODE_SLIDE);
                        snackbar.setDuration(Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.setTextColor(Color.BLUE);
                        snackbar.show();
                        nearbyStationProgressBar.setVisibility(View.GONE);
                        nearbyListRefreshBtn.setEnabled(true);
                        nearbyListRefreshBtn.setBackgroundResource(android.R.drawable.btn_default);
                        nearbyListRefreshBtn.setText("Refresh Stations");
                    }

                }
            }else {
                try {


                    FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                findNearByStations();

                            }else{
                                nearbyStationProgressBar.setVisibility(View.GONE);
                                nearbyListRefreshBtn.setEnabled(true);
                                nearbyListRefreshBtn.setBackgroundResource(android.R.drawable.btn_default);
                                nearbyListRefreshBtn.setText("Refresh Stations");
                            }

                        }
                    });
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "! Unexpected error, try again !!", Snackbar.ANIMATION_MODE_SLIDE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.BLUE);
                    snackbar.show();
                    nearbyStationProgressBar.setVisibility(View.GONE);
                    nearbyListRefreshBtn.setEnabled(true);
                    nearbyListRefreshBtn.setBackgroundResource(android.R.drawable.btn_default);
                    nearbyListRefreshBtn.setText("Refresh Stations");
                }
                }

    }

      @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_Req.REQ_CODE) {
            if(resultCode == Activity.RESULT_OK){
                final_location_fetch();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("locatPerm", "handlePermissionResult: in train: " + requestCode);
        if (requestCode == Location_Permissions.REQ_CODE) {
            if (locationPermissionsObj != null) {
                locationPermissionsObj.handlePermissionResult(requestCode, permissions, grantResults);
            }
        }
    }




}
