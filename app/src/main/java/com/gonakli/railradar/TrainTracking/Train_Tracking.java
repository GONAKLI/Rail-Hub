package com.gonakli.railradar.TrainTracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.gonakli.railradar.Services.API_Call.Train_Tracking_API_Call;
import com.gonakli.railradar.Services.LocationService.myLocationServiceClass;
import com.gonakli.railradar.Structure_Class.Track_Polyline_Point_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railradar.Structure_Class.Train_Tracking_Live_Structure_Class;
import com.gonakli.railradar.Structure_Class.Train_Tracking_Structure;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class Train_Tracking extends AppCompatActivity {
    String trainNumber, trainName, fromStationCode, toStationCode;
    ExtendedFloatingActionButton insideTrainBtn;
    Toolbar toolbar;
    TextView tvStatusMessage, tvUpdatedTime,tvNextStopName,tvNextStopMeta;
    TextView tvDestinationName,tvDestinationMeta,tvDelayStatus;
    TextView tvFromStation,tvToStation;
    TextView trackingHeaderPreviousStation, trackingHeaderCurrentStation, trackingHeaderNextStation,trackingHeaderStatusInfo;
    ImageButton refreshButton;
    Train_Tracking_Recycler_View_Adapter adapter;

    double lat, lng;
    Train_Schedule_Structure myTrainData;
    RecyclerView live_train_tracking_recycler_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_train_tracking_combined_main);

        find_all_id();
        get_intent_data();
        set_custom_toolbar();
        train_finder();
        set_insideTrainBtn_action();

        test_API_Service();

       // set_BottomSheet_Layout();



       
    }

    private void test_API_Service() {
    Intent intent = new Intent(Train_Tracking.this, Train_Tracking_API_Call.class);
    intent.putExtra("trainNumber", trainNumber);
    startService(intent);
    }

    private void tracking_upper_header(Train_Tracking_Structure trainLocationData) {
        String prSt,crSt,nxSt, infoMsg;
        if(trainLocationData != null){
            if(trainLocationData.isOnRoute() && trainLocationData.getPreviousStation() != null){
                prSt = trainLocationData.getPreviousStation().getStationName();
                trackingHeaderPreviousStation.setText(prSt);
            }else{
                trackingHeaderPreviousStation.setText("--");
            }

            if(trainLocationData.isOnRoute() && trainLocationData.isAtStation() && trainLocationData.getCurrentStation() != null){
                crSt = trainLocationData.getCurrentStation().getStationName();
                trackingHeaderCurrentStation.setText(crSt);
            }else{
                trackingHeaderCurrentStation.setText("--");
            }

            if(trainLocationData.isOnRoute() && trainLocationData.getNextStation() != null){
                nxSt = trainLocationData.getNextStation().getStationName();
                trackingHeaderNextStation.setText(nxSt);
            }else {
                trackingHeaderNextStation.setText("--");
            }

            if(trainLocationData.getStatusMessage() != null){
                infoMsg = trainLocationData.getStatusMessage().trim();
                trackingHeaderStatusInfo.setText(infoMsg);
            }


        }
    }

//    private void set_BottomSheet_Layout_Content(Train_Tracking_Live_Structure_Class st) {
//        // upper textview status message set
//        if(st.isAtStation() && st.getCurrentStation() != null){
//            String msg = "Arrived " + st.getCurrentStation();
//            tvStatusMessage.setText(msg);
//        }else if (!st.isAtStation()){
//            String msg = st.getStatusMessage();
//            tvStatusMessage.setText(msg);
//        }else {
//            tvStatusMessage.setText("--");
//        }
//
//    // next stop container ie name and info
//        if(st.getNextStation() != null){
//           String nextStop = st.getNextStation().getStationName();
//            String nextStopArrival = st.getNextStation().getArrivalTime();
//            String nextTrainStop = String.valueOf(Integer.parseInt(st.getNextStation().getDistance()) - st.getTotalJourneyCovered());
//            tvNextStopName.setText(nextStop);
//            tvNextStopMeta.setText(nextTrainStop);
//        }
//
//        if(st.getSourceStation() != null && st.getDestinationStation() != null){
//            String srStation = st.getSourceStation();
//            String destStation = st.getDestinationStation();
//            tvFromStation.setText(srStation);
//            tvToStation.setText(destStation);
//        }
//
//    }

//    private void set_BottomSheet_Layout() {
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Train_Tracking.this);
//        bottomSheetDialog.setContentView(R.layout.train_tracking_bottomsheet);
//
//        Window window = bottomSheetDialog.getWindow();
//        if (window != null) {
//            // 1. Peeche ka Dark/Dim background hatayein
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//
//            // 2. Non-modal flag
//            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
//        }
//
//        View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
//        if (bottomSheet != null) {
//            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//
//            // DP to Pixels conversion (200dp)
//            int peekHeightPx = (int) (200 * getResources().getDisplayMetrics().density);
//            bottomSheetBehavior.setPeekHeight(peekHeightPx);
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
//
//        bottomSheetDialog.setCancelable(false);
//        bottomSheetDialog.setCanceledOnTouchOutside(false);
//
//// 3. MAIN FIX: Extra touch container ke touches seedhe Activity/Map ko pass karein
//        View touchOutside = bottomSheetDialog.findViewById(com.google.android.material.R.id.touch_outside);
//        if (touchOutside != null) {
//            touchOutside.setOnTouchListener((v, event) -> {
//                // Touch event ko piche Activity/Map par bhej do
//                Train_Tracking.this.dispatchTouchEvent(event);
//                return false;
//            });
//        }
//
//        bottomSheetDialog.show();
//    }

    private void track_user() {
        String stData = "";
        ArrayList<Train_Schedule_Station_Structure> arrTrainStations = myTrainData.getStationList();
        ArrayList<Track_Polyline_Point_Structure> arrPolylinePoints = myTrainData.getPolylinePoints();
        Train_Tracking_Live_Structure_Class st = new Train_Tracking_Live_Structure_Class(lat,lng,arrTrainStations, arrPolylinePoints);
        st.trackMyUserTrain();
        Train_Tracking_Structure trainLocationData = st.getReport();
        if(adapter != null){
            adapter.updateAdapter(trainLocationData);
        }

        if(trainLocationData.getCurrentStation() !=null){
            stData = trainLocationData.getCurrentStation().getStationCode();
        } else if (trainLocationData.getPreviousStation() != null) {
            stData = trainLocationData.getPreviousStation().getStationCode();
        }else if (trainLocationData.getNextStation() != null){
            stData = trainLocationData.getNextStation().getStationCode();
        }
        for(int i=0; i<arrTrainStations.size(); i++){
            if(arrTrainStations.get(i).getStationCode().equals(stData)){
                live_train_tracking_recycler_view.smoothScrollToPosition(i);
            }
        }
        tracking_upper_header(trainLocationData);


    }

    boolean isInsideTrain = false;
        private void set_insideTrainBtn_action() {
            insideTrainBtn.setOnClickListener(v -> {
                // Toggle State (ON -> OFF / OFF -> ON)
                isInsideTrain = !isInsideTrain;

                Intent intent = new Intent(Train_Tracking.this, myLocationServiceClass.class);

                if (isInsideTrain) {
                    // ================= STATE 1: INSIDE TRAIN (ACTIVE / ON) =================
                    insideTrainBtn.setText("Stop, I'm Outside");
                    insideTrainBtn.setIconResource(R.drawable.nearby_station_icon); // Ya aapka active icon
                    insideTrainBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2E7D32"))); // Green Color
                    insideTrainBtn.setTextColor(Color.WHITE);
                    insideTrainBtn.setIconTint(android.content.res.ColorStateList.valueOf(Color.WHITE));

                    // Start Foreground Service
                    intent.putExtra("trainNumber", trainNumber);
                    startService(intent);
                    Toast.makeText(Train_Tracking.this, "Live Tracking Started", Toast.LENGTH_SHORT).show();

                } else {
                    // ================= STATE 2: NOT IN TRAIN (INACTIVE / OFF) =================
                    insideTrainBtn.setText("Inside Train ?");
                    insideTrainBtn.setIconResource(R.drawable.nearby_station_icon);
                    insideTrainBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#B7AA9D"))); // Default Neutral Color
                    insideTrainBtn.setTextColor(Color.WHITE);
                    insideTrainBtn.setIconTint(android.content.res.ColorStateList.valueOf(Color.WHITE));

                    // Stop Service
                    stopService(intent);

                    Toast.makeText(Train_Tracking.this, "Live Tracking Stopped", Toast.LENGTH_SHORT).show();
                }
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

        if(fromStationCode != null && toStationCode != null){
            adapter = new Train_Tracking_Recycler_View_Adapter(Train_Tracking.this, myTrainData, fromStationCode, toStationCode);
        }else{
            adapter = new Train_Tracking_Recycler_View_Adapter(Train_Tracking.this, myTrainData);
        }

       live_train_tracking_recycler_view.setLayoutManager(new LinearLayoutManager(Train_Tracking.this));
        live_train_tracking_recycler_view.setAdapter(adapter);

    }

    private void train_finder() {
            new Thread(() ->{
                Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Train_Tracking.this);
                myTrainData = dbHelper.getTrainDataByTrainNumber(trainNumber);
                dbHelper.close();
                this.runOnUiThread(()->{
                    set_recycler_view();
                });

            }).start();

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

        tvStatusMessage = findViewById(R.id.tvStatusMessage);
        tvUpdatedTime = findViewById(R.id.tvUpdatedTime);
        tvNextStopName = findViewById(R.id.tvNextStopName);
        tvNextStopMeta = findViewById(R.id.tvNextStopMeta);
        tvDestinationName = findViewById(R.id.tvDestinationName);
        tvDestinationMeta = findViewById(R.id.tvDestinationMeta);
        tvDelayStatus = findViewById(R.id.tvDelayStatus);
        tvFromStation = findViewById(R.id.tvFromStation);
        tvToStation = findViewById(R.id.tvToStation);
        refreshButton = findViewById(R.id.refreshButton);

        trackingHeaderPreviousStation = findViewById(R.id.trackingHeaderPreviousStation);
        trackingHeaderCurrentStation = findViewById(R.id.trackingHeaderCurrentStation);
        trackingHeaderNextStation = findViewById(R.id.trackingHeaderNextStation);
        trackingHeaderStatusInfo = findViewById(R.id.trackingHeaderStatusInfo);
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
