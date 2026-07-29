package com.gonakli.railradar.ExtraOptions;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.gonakli.railradar.ADAPTERS.NearBy_Station_ListView_Adapter;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.HomeActivity.Home_Screen_Activity;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.NearBy_Station_Structure;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

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
    double latitude, longitude;
    boolean isGpsEnabled;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_stations_layout);
        find_all_id();
        set_toolbar();
        get_Location();
        action_on_refresh_btn();

    }

    private void action_on_refresh_btn() {
        nearbyListRefreshBtn.setOnClickListener(v -> {
            arrNearByStations.clear();
            adapter.notifyDataSetChanged();
            get_Location();

        });
    }


    private void displayNearByStations() {
        Collections.sort(arrNearByStations, (s1,s2) -> Double.compare(s1.getStationDistance(), s2.getStationDistance()) );
        if(!arrNearByStations.isEmpty()){
            adapter = new NearBy_Station_ListView_Adapter(NearBy_Station_Activity.this, arrNearByStations);
            nearbyStationListView.setAdapter(adapter);
        }
        nearbyStationProgressBar.setVisibility(View.GONE);


    }

    private void findNearByStations() {
        Station_List_DB_Helper dbHelper = new Station_List_DB_Helper(NearBy_Station_Activity.this);
        ArrayList<Station_List_Structure> arrStations = dbHelper.getStationList();
        dbHelper.close();
        if(latitude == 0.0d && longitude == 0.0d){
            get_Location();
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

    private void get_Location() {
        AlertDialog alertDialog = new AlertDialog.Builder(NearBy_Station_Activity.this)
                    .setTitle("Location Permission Required")
                    .setMessage("To find Nearby Station, app require your current Location to provide this functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NearBy_Station_Activity.this,"Turn on Location permission", Toast.LENGTH_LONG).show();
                        Intent iSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        iSettings.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(iSettings);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(NearBy_Station_Activity.this, "Location Permission denied !!", Toast.LENGTH_SHORT).show();
//                        Intent iHome = new Intent(NearBy_Station_Activity.this, Home_Screen_Activity.class);
//                        startActivity(iHome);
                        finish();

                    }
                }).create();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                alertDialog.show();
                return;
            }else{
                ActivityCompat.requestPermissions(
                        this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},50);
            }

        }
        isGpsEnabled = isGPS_Enabled();
        if(isGpsEnabled){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            nearbyStationProgressBar.setVisibility(View.VISIBLE);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
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
                           fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,  null)
                                   .addOnSuccessListener(location1 -> {
                                       latitude = location1.getLatitude();
                                       longitude = location1.getLongitude();
                                       findNearByStations();
                                   });

                       }

                    }
            );
            }
        }else{
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                        findNearByStations();

                    }

                }
            });
        }
        }
    }

    private void set_toolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Nearby Stations");
        toolbar.setBackgroundColor(Color.parseColor("#B7AA9D"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void find_all_id() {
        toolbar = findViewById(R.id.application_custom_toolbar);
        nearbyStationListView = findViewById(R.id.nearbyStationListView);
        nearbyListRefreshBtn = findViewById(R.id.nearbyListRefreshBtn);
        nearbyStationProgressBar = findViewById(R.id.nearbyStationProgressBar);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {

        if(requestCode == 50){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                Toast toast = new Toast(getApplicationContext());
                toast.setText("Location Permission Required ");
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();

            }else{

            }
        }

    }

    public boolean isGPS_Enabled(){
        LocationManager locationManager =(LocationManager) getSystemService(NearBy_Station_Activity.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled){
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS is disabled")
                    .setMessage("Please enable GPS service to use this feature")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent iLocation = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                           startActivity(iLocation);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(NearBy_Station_Activity.this, "Enable GPS to use this feature", Toast.LENGTH_SHORT).show();
//                            Intent iHome = new Intent(NearBy_Station_Activity.this, Home_Screen_Activity.class);
//                            startActivity(iHome);
                            finish();
                        }
                    }).create();
            alertDialog.show();
        }
        return isGpsEnabled;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }


}
