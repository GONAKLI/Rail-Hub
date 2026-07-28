package com.gonakli.railradar.ExtraOptions;

import android.Manifest;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.gonakli.railradar.ADAPTERS.NearBy_Station_ListView_Adapter;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.NearBy_Station_Structure;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class NearBy_Station_Activity extends AppCompatActivity {

    ArrayList<NearBy_Station_Structure> arrNearByStations = new ArrayList<>();
    Toolbar toolbar;
    ListView nearbyStationListView;
    double latitude, longitude;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_stations_layout);
        find_all_id();
        set_toolbar();
        get_Location();
//        findNearByStations();
//        displayNearByStations();

    }

    private void displayNearByStations() {
        Collections.sort(arrNearByStations, (s1,s2) -> Integer.compare(s1.getStationDistance(), s2.getStationDistance()) );
        if(arrNearByStations.size() >0){

        }
        NearBy_Station_ListView_Adapter adapter = new NearBy_Station_ListView_Adapter(NearBy_Station_Activity.this, arrNearByStations);
        nearbyStationListView.setAdapter(adapter);

    }

    private void findNearByStations() {
        Station_List_DB_Helper dbHelper = new Station_List_DB_Helper(NearBy_Station_Activity.this);
        ArrayList<Station_List_Structure> arrStations = dbHelper.getStationList();
        dbHelper.close();
        Location startPoint = new Location("startPoint");
        startPoint.setLatitude(latitude);
        startPoint.setLongitude(longitude);
        for(Station_List_Structure item : arrStations){
            Location endPoint = new Location("endPoint");
            double latD = Double.parseDouble(item.getStation_Lat());
            double lngD = Double.parseDouble(item.getStation_Lng());
            endPoint.setLatitude(latD);
            endPoint.setLongitude(lngD);

            float distanceInMeteres = startPoint.distanceTo(endPoint);
            int distanceInKm =(int) distanceInMeteres/1000;

            if(distanceInKm <= 100){
                String StName = item.getStation_Name();
                String StCode = item.getStation_Code();
                String StLat = item.getStation_Lat();
                String StLng = item.getStation_Lng();
                int StDistance = distanceInKm;
                arrNearByStations.add(new NearBy_Station_Structure(StName,StCode,StLat,StLng,StDistance));
                Log.d("log2", "findNearByStations: " + distanceInKm + " : " + item.getStation_Name());
            }

        }


    }

    private void get_Location() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    || !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                Toast.makeText(this,"Location Access is Requird", Toast.LENGTH_LONG).show();
                Intent iSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                iSettings.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(iSettings);
            }else{
                ActivityCompat.requestPermissions(
                        this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},50);
            }
            return;
        }


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
             locationManager.getCurrentLocation(
                    LocationManager.GPS_PROVIDER,
                    null,
                    getMainExecutor(),
                    location -> {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        findNearByStations();
                        displayNearByStations();
                    }
            );}
        }else{
//            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if(location != null){
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//                    }
//
//                }
//            });
        }
    }

    private void set_toolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Stations Near You");
        toolbar.setBackgroundColor(Color.GREEN);

    }

    private void find_all_id() {
        toolbar = findViewById(R.id.application_custom_toolbar);
        nearbyStationListView = findViewById(R.id.nearbyStationListView);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {

        if(requestCode == 50){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED){
                Toast toast = new Toast(getApplicationContext());
                toast.setText("Location Permission Required ");
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();

            };
        }

    }
}
