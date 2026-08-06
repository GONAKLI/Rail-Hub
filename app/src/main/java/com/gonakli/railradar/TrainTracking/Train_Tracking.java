package com.gonakli.railradar.TrainTracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.ADAPTERS.Train_Tracking_Recycler_View_Adapter;
import com.gonakli.railradar.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Services.LocationService.myLocationServiceClass;
import com.gonakli.railradar.Structure_Class.NearBy_Station_Structure;
import com.gonakli.railradar.Structure_Class.Track_Polyline_Point_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railradar.Structure_Class.Train_Tracking_Live_Structure_Class;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Train_Tracking extends AppCompatActivity {
    String trainNumber, trainName, fromStationCode, toStationCode;
    ExtendedFloatingActionButton insideTrainBtn;
    Toolbar toolbar;

    double lat, lng;
    Train_Schedule_Structure myTrainData;
    RecyclerView live_train_tracking_recycler_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_train_trackin_combined_main);

        find_all_id();
        get_intent_data();
        set_custom_toolbar();
        train_finder();
        set_recycler_view();
        set_insideTrainBtn_action();
        set_BottomSheet_Layout();



       
    }

    private void set_BottomSheet_Layout() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Train_Tracking.this);
        bottomSheetDialog.setContentView(R.layout.train_tracking_bottomsheet);
        View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        bottomSheetDialog.show();
    }

    private void track_user() {
        ArrayList<Train_Schedule_Station_Structure> arrTrainStations = myTrainData.getStationList();
        ArrayList<Track_Polyline_Point_Structure> arrPolylinePoints = myTrainData.getPolylinePoints();
        Train_Tracking_Live_Structure_Class st = new Train_Tracking_Live_Structure_Class(lat,lng,arrTrainStations, arrPolylinePoints);
        st.trackMyUserTrain();
        String statusMessage = st.getStatusMessage();
        int stationCoveredPercentage = st.getStationCoveredPercentage();
        int totalJourneyCovered = st.getTotalJourneyCovered();
        if(st.getPreviousStation() !=null){
            Train_Schedule_Station_Structure previousStation = st.getPreviousStation();
        }
        if(st.getCurrentStation() !=null){
            Train_Schedule_Station_Structure currentStation = st.getCurrentStation();
        }
        if(st.getNextStation() != null){
            Train_Schedule_Station_Structure nextStation = st.getNextStation();
        }
        boolean isAtStation = st.isAtStation();
        boolean isOnRoute = st.isOnRoute();


    }

    private void set_insideTrainBtn_action() {
        insideTrainBtn.setOnClickListener(v ->
        {
            Intent intent = new Intent(Train_Tracking.this, myLocationServiceClass.class);
            startService(intent);
        });

    }

    private void set_custom_toolbar() {
        setSupportActionBar(toolbar);
        String toolBarTitle = String.format("%s - %s", trainNumber, trainName);
        Objects.requireNonNull(getSupportActionBar()).setTitle(toolBarTitle);
        toolbar.setBackgroundColor(Color.parseColor("#B7AA9D"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void set_recycler_view() {
        Train_Tracking_Recycler_View_Adapter adapter;
        if(fromStationCode != null && toStationCode != null){
            adapter = new Train_Tracking_Recycler_View_Adapter(Train_Tracking.this, myTrainData, fromStationCode, toStationCode);
        }else{
            adapter = new Train_Tracking_Recycler_View_Adapter(Train_Tracking.this, myTrainData);
        }

       live_train_tracking_recycler_view.setLayoutManager(new LinearLayoutManager(Train_Tracking.this));
        live_train_tracking_recycler_view.setAdapter(adapter);

    }

    private void train_finder() {
        Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Train_Tracking.this);
        myTrainData = dbHelper.getTrainDataByTrainNumber(trainNumber);
        dbHelper.close();
    }

    private void get_intent_data() {
        Intent iTrack = getIntent();
        trainNumber = iTrack.getStringExtra("trainNumber");
        trainName = iTrack.getStringExtra("trainName");
        fromStationCode = iTrack.getStringExtra("fromStationCode");
        toStationCode = iTrack.getStringExtra("toStationCode");
    }

    private void find_all_id() {
        live_train_tracking_recycler_view = findViewById(R.id.live_train_tracking_recycler_view);
        toolbar = findViewById(R.id.application_custom_toolbar);
        insideTrainBtn = findViewById(R.id.insideTrainBtn);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return  true;
    }


    // broadcast receiver

    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           lat = intent.getDoubleExtra("lat", 0);
           lng = intent.getDoubleExtra("lng", 0);
            Log.d("Serviceclass", "onReceive: receiver h" + lat);
            Toast.makeText(Train_Tracking.this, "Lat: " + lat + "\nLng: " + lng, Toast.LENGTH_SHORT).show();
            track_user();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver, new IntentFilter(myLocationServiceClass.ACTION_LOCATION_UPDATE),  Context.RECEIVER_NOT_EXPORTED );
        }

    }

 //   @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(locationReceiver);
//    }
}
