package com.gonakli.railradar.TrainTracking;

import android.Manifest;
import android.app.Activity;
import android.app.ComponentCaller;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.ADAPTERS.Train_Tracking_Recycler_View_Adapter;
import com.gonakli.railradar.API_Limit.Train_Finder_Api_Limit;
import com.gonakli.railradar.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railradar.Permissions.GPS_Req;
import com.gonakli.railradar.Permissions.Location_Permissions;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Services.API_Call.Train_Tracking_API_Call;
import com.gonakli.railradar.Services.LocationService.myLocationServiceClass;
import com.gonakli.railradar.Structure_Class.API_Response_Train_Tracking;
import com.gonakli.railradar.Structure_Class.Track_Polyline_Point_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railradar.Structure_Class.Train_Tracking_Live_Structure_Class;
import com.gonakli.railradar.Structure_Class.Train_Tracking_Structure;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Train_Tracking extends AppCompatActivity {
    String trainNumber, trainName, fromStationCode, toStationCode;
    ExtendedFloatingActionButton insideTrainBtn;
    Toolbar toolbar;
    TextView tvStatusMessage, tvUpdatedTime, tvNextStopName, tvNextStopMeta;
    TextView tvDestinationName, tvDestinationMeta, tvDelayStatus;
    TextView tvFromStation, tvToStation;
    TextView trackingHeaderPreviousStation, trackingHeaderCurrentStation, trackingHeaderNextStation, trackingHeaderStatusInfo;
    ImageButton refreshButton, btnRefreshLiveTracking;
    Location_Permissions obj;
    Intent iLocationService, iTrainApiService;

    Train_Tracking_Recycler_View_Adapter adapter;

    double lat, lng;
    Long selectedDateInLong;
    boolean isInsideTrain = false;
    String selectedDate;
    String trainApiStatusMsg;
    Train_Schedule_Structure myTrainData;
    RecyclerView live_train_tracking_recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_train_tracking_combined_main);
        obj = new Location_Permissions(Train_Tracking.this);
        find_all_id();
        get_intent_data();
        set_custom_toolbar();
        train_finder();
        set_insideTrainBtn_action();
        call_API_Service();
        Refresh_Live_Tracking();

    }

    private void Refresh_Live_Tracking() {

        btnRefreshLiveTracking.setOnClickListener(v -> {
            if (Train_Finder_Api_Limit.canCallFindTrainAPI(trainNumber)) {
                btnRefreshLiveTracking.startAnimation(AnimationUtils.loadAnimation(this, R.anim.train_location_refresh_btn));
                Toast.makeText(this, "Refreshing ...", Toast.LENGTH_SHORT).show();
                call_API_Service();
            } else {
                btnRefreshLiveTracking.startAnimation(AnimationUtils.loadAnimation(this, R.anim.train_location_refresh_btn));
                Toast.makeText(this, "updated a few seconds ago", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void call_API_Service() {
        iTrainApiService = new Intent(Train_Tracking.this, Train_Tracking_API_Call.class);
        iTrainApiService.putExtra("trainNumber", trainNumber);
        if (selectedDate != null) {
            iTrainApiService.putExtra("startDate", selectedDate);
        }
        startService(iTrainApiService);
    }

    private void tracking_upper_header(Train_Tracking_Structure trainLocationData) {
        Log.d("checkStatusmsg", "tracking_upper_header: in header function");
        String prSt, crSt, nxSt, infoMsg;
        if (trainLocationData != null) {
            if (trainLocationData.isOnRoute() && trainLocationData.getPreviousStation() != null) {
                prSt = trainLocationData.getPreviousStation().getStationName();
                trackingHeaderPreviousStation.setText(prSt);
            } else {
                trackingHeaderPreviousStation.setText("--");
            }

            if (trainLocationData.isOnRoute() && trainLocationData.isAtStation() && trainLocationData.getCurrentStation() != null) {
                crSt = trainLocationData.getCurrentStation().getStationName();
                trackingHeaderCurrentStation.setText(crSt);
            } else {
                trackingHeaderCurrentStation.setText("--");
            }

            if (trainLocationData.isOnRoute() && trainLocationData.getNextStation() != null) {
                nxSt = trainLocationData.getNextStation().getStationName();
                trackingHeaderNextStation.setText(nxSt);
            } else {
                trackingHeaderNextStation.setText("--");
            }

            if (trainLocationData.getStatusMessage() != null) {
                Log.d("checkStatusmsg", "tracking_upper_header: inside if block enter");
                if (trainApiStatusMsg != null && !trainApiStatusMsg.isEmpty()) {

                    if (trainApiStatusMsg.contains("Train is not running today")) {
                        Log.d("checkStatusmsg", "tracking_upper_header: 1");
                        trackingHeaderStatusInfo.setText(trainApiStatusMsg);
                        trackingHeaderStatusInfo.setTextColor(Color.WHITE);
                        trackingHeaderStatusInfo.setBackgroundColor(Color.RED);
                    } else if (trainApiStatusMsg.contains("Train not started yet")) {

                        Log.d("checkStatusmsg", "tracking_upper_header: 2");
                        trackingHeaderStatusInfo.setText(trainApiStatusMsg);
                    } else if (trainApiStatusMsg.contains("Train journey Already ended")) {
                        Log.d("checkStatusmsg", "tracking_upper_header: 3");
                        trackingHeaderStatusInfo.setText(trainApiStatusMsg);
                        trackingHeaderStatusInfo.setTextColor(Color.RED);
                    } else if (trainApiStatusMsg.contains("is cancelled")) {
                        trackingHeaderStatusInfo.setText(trainApiStatusMsg);
                        trackingHeaderStatusInfo.setTextColor(Color.RED);
                    }
                    Log.d("checkStatusmsg", "tracking_upper_header: msg value : " + trainApiStatusMsg);
                } else {
                    Log.d("checkStatusmsg", "tracking_upper_header: inside else block enter");
                    infoMsg = trainLocationData.getStatusMessage().trim();
                    trackingHeaderStatusInfo.setText(infoMsg);
                }

            }


        }
    }

    private void track_user() {
        String stData = "";
        ArrayList<Train_Schedule_Station_Structure> arrTrainStations = myTrainData.getStationList();
        ArrayList<Track_Polyline_Point_Structure> arrPolylinePoints = myTrainData.getPolylinePoints();
        Train_Tracking_Live_Structure_Class st = new Train_Tracking_Live_Structure_Class(lat, lng, arrTrainStations, arrPolylinePoints);
        st.trackMyUserTrain();
        Train_Tracking_Structure trainLocationData = st.getReport();
        if (adapter != null) {
            adapter.updateAdapter(trainLocationData);
        }

        if (trainLocationData.getCurrentStation() != null) {
            stData = trainLocationData.getCurrentStation().getStationCode();
        } else if (trainLocationData.getPreviousStation() != null) {
            stData = trainLocationData.getPreviousStation().getStationCode();
        } else if (trainLocationData.getNextStation() != null) {
            stData = trainLocationData.getNextStation().getStationCode();
        }

        Log.d("scroll_testing", "track_user: " + stData);
        for (int i = 0; i < arrTrainStations.size(); i++) {
            if (arrTrainStations.get(i).getStationCode().equals(stData)) {
                int finalI = i;
                live_train_tracking_recycler_view.post(() -> {
                    LinearLayoutManager lm = (LinearLayoutManager) live_train_tracking_recycler_view.getLayoutManager();
                    lm.scrollToPositionWithOffset(finalI, 0);
                    //live_train_tracking_recycler_view.smoothScrollToPosition(finalI);
                });
                break;
            }
            Log.d("scroll_testing", "track_user: arrTrainStations.get(i).getStationCode(): " + arrTrainStations.get(i).getStationCode());
            Log.d("scroll_testing", "track_user: val of stData: " + stData);
            Log.d("scroll_testing", "track_user: val of i: " + i);
        }

        tracking_upper_header(trainLocationData);


    }


    private boolean permissionCheckPoint() {
        boolean isGranted = obj.checkPermission();
        if (!isGranted) return false;
        GPS_Req isGPSEnabled = new GPS_Req(Train_Tracking.this);
        return isGPSEnabled.gpsChecker();
    }

    private void set_insideTrainBtn_action() {
        insideTrainBtn.setOnClickListener(v -> {
            if (permissionCheckPoint()) {
                final_inside_task();
            }
        });
    }

    private void final_inside_task() {
        if (!permissionCheckPoint()) return;
        // Toggle State (ON -> OFF / OFF -> ON)
        isInsideTrain = !isInsideTrain;

        iLocationService = new Intent(Train_Tracking.this, myLocationServiceClass.class);

        if (isInsideTrain) {

            btnRefreshLiveTracking.setVisibility(View.GONE);
            trainApiStatusMsg = null;
            // ================= STATE 1: INSIDE TRAIN (ACTIVE / ON) =================
            insideTrainBtn.setText("Stop, I'm Outside");
            insideTrainBtn.setIconResource(R.drawable.nearby_station_icon); // Ya aapka active icon
            insideTrainBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2E7D32"))); // Green Color
            insideTrainBtn.setTextColor(Color.WHITE);
            insideTrainBtn.setIconTint(android.content.res.ColorStateList.valueOf(Color.WHITE));

            // Start Foreground Service
            iLocationService.putExtra("trainNumber", trainNumber);
            startService(iLocationService);
            Toast.makeText(Train_Tracking.this, "Live Tracking Started", Toast.LENGTH_SHORT).show();

        } else {
            btnRefreshLiveTracking.setVisibility(View.VISIBLE);
            // ================= STATE 2: NOT IN TRAIN (INACTIVE / OFF) =================
            insideTrainBtn.setText("Inside Train ?");
            insideTrainBtn.setIconResource(R.drawable.nearby_station_icon);
            insideTrainBtn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#B7AA9D"))); // Default Neutral Color
            insideTrainBtn.setTextColor(Color.WHITE);
            insideTrainBtn.setIconTint(android.content.res.ColorStateList.valueOf(Color.WHITE));

            // Stop Service
            if (iLocationService != null) {
                stopService(iLocationService);
            }


            Toast.makeText(Train_Tracking.this, "Live Tracking Stopped", Toast.LENGTH_SHORT).show();
        }
    }


    private void set_custom_toolbar() {
        setSupportActionBar(toolbar);
        String toolBarTitle = String.format("%s - %s", trainNumber, trainName);
        Objects.requireNonNull(getSupportActionBar()).setTitle(toolBarTitle);
        toolbar.setBackgroundColor(Color.parseColor("#B7AA9D"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void set_recycler_view() {

        if (fromStationCode != null && toStationCode != null) {
            adapter = new Train_Tracking_Recycler_View_Adapter(Train_Tracking.this, myTrainData, fromStationCode, toStationCode);
        } else {
            adapter = new Train_Tracking_Recycler_View_Adapter(Train_Tracking.this, myTrainData);
        }

        live_train_tracking_recycler_view.setLayoutManager(new LinearLayoutManager(Train_Tracking.this));
        live_train_tracking_recycler_view.setAdapter(adapter);

    }

    private void train_finder() {
        new Thread(() -> {
            Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(Train_Tracking.this);
            myTrainData = dbHelper.getTrainDataByTrainNumber(trainNumber);
            dbHelper.close();
            this.runOnUiThread(() -> {
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
        btnRefreshLiveTracking = findViewById(R.id.btnRefreshLiveTracking);

        trackingHeaderPreviousStation = findViewById(R.id.trackingHeaderPreviousStation);
        trackingHeaderCurrentStation = findViewById(R.id.trackingHeaderCurrentStation);
        trackingHeaderNextStation = findViewById(R.id.trackingHeaderNextStation);
        trackingHeaderStatusInfo = findViewById(R.id.trackingHeaderStatusInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.train_tracking_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.trainTrackingCalender) {
            showCalender();

        }
        return true;
    }

    private void showCalender() {
        CalendarConstraints.Builder constraint = new CalendarConstraints.Builder();
        Calendar today = Calendar.getInstance();
        Calendar thirtyDayAgo = Calendar.getInstance();
        thirtyDayAgo.add(Calendar.DAY_OF_MONTH, -10);
        constraint.setStart(thirtyDayAgo.getTimeInMillis());
        constraint.setEnd(today.getTimeInMillis());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker
                .Builder.datePicker().setCalendarConstraints(constraint.build())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setSelection(selectedDateInLong == null ? today.getTimeInMillis() : selectedDateInLong)
                .setTitleText("Choose Train Journey Start Date").build();
        datePicker.show(getSupportFragmentManager(), "MY_CALENDER");
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long aLong) {
                selectedDateInLong = aLong;
                Date date = new Date(aLong);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = simpleDateFormat.format(date);
                call_API_Service();
            }
        });
    }


    // broadcast receiver

    private final BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            lat = intent.getDoubleExtra("lat", 0);
            lng = intent.getDoubleExtra("lng", 0);
            if(lat == 0 || lng == 0) return;
            Log.d("Serviceclass", "onReceive: receiver h" + lat);
            track_user();
        }
    };

    private final BroadcastReceiver apiTrainLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                lat = intent.getDoubleExtra("trainLat", 0);
                lng = intent.getDoubleExtra("trainLng", 0);
                if(lat == 0 || lng == 0) return;
                trainApiStatusMsg = intent.getStringExtra("trainApiStatusMsg");
                ArrayList<API_Response_Train_Tracking> apiData = (ArrayList<API_Response_Train_Tracking>) intent.getSerializableExtra("stationData");
                if (apiData != null) {
                    API_Train_Location(apiData);
                }

            }


        }
    };

    private void API_Train_Location(ArrayList<API_Response_Train_Tracking> apiData) {
        if (myTrainData == null) return;
        String stData = "";
        ArrayList<Train_Schedule_Station_Structure> arrTrainStations = myTrainData.getStationList();
        ArrayList<Track_Polyline_Point_Structure> arrPolylinePoints = myTrainData.getPolylinePoints();
        Train_Tracking_Live_Structure_Class st = new Train_Tracking_Live_Structure_Class(lat, lng, arrTrainStations, arrPolylinePoints);
        st.trackMyUserTrain();
        Train_Tracking_Structure trainLocationData = st.getReport();
        if (adapter != null) {
            adapter.APi_Adapter_Update(trainLocationData, apiData);
        }

        if (trainLocationData.getCurrentStation() != null) {
            stData = trainLocationData.getCurrentStation().getStationCode();
        } else if (trainLocationData.getPreviousStation() != null) {
            stData = trainLocationData.getPreviousStation().getStationCode();
        } else if (trainLocationData.getNextStation() != null) {
            stData = trainLocationData.getNextStation().getStationCode();
        }


        for (int i = 0; i < arrTrainStations.size(); i++) {
            Log.d("scroll_debug", "StationList code='" + arrTrainStations.get(i).getStationCode() + "'");
            if (arrTrainStations.get(i).getStationCode().trim().equalsIgnoreCase(stData.trim())) {
                int finalI = i;
                live_train_tracking_recycler_view.post(() -> {
                    live_train_tracking_recycler_view.scrollToPosition(finalI);
                });
                break;
            }
        }

        tracking_upper_header(trainLocationData);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(locationReceiver, new IntentFilter(myLocationServiceClass.ACTION_LOCATION_UPDATE), Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(apiTrainLocation, new IntentFilter(Train_Tracking_API_Call.API_TRAIN_DATA), Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(locationReceiver, new IntentFilter(myLocationServiceClass.ACTION_LOCATION_UPDATE), Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(apiTrainLocation, new IntentFilter(Train_Tracking_API_Call.API_TRAIN_DATA), Context.RECEIVER_NOT_EXPORTED);
        }

        if (isInsideTrain && iLocationService != null) {
            startService(iLocationService);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(locationReceiver);
            unregisterReceiver(apiTrainLocation);
        } catch (IllegalArgumentException e) {
            // work here
        }

        if (iLocationService != null) {
            stopService(iLocationService);
        }
        if (iTrainApiService != null) {
            stopService(iTrainApiService);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("locatPerm", "handlePermissionResult: in train: " + requestCode);
        if (requestCode == Location_Permissions.REQ_CODE) {
            if (grantResults.length > 0) {
                boolean isAllPermissionGranted = true;
                for (int res : grantResults) {
                    if (res == PackageManager.PERMISSION_DENIED) {
                        isAllPermissionGranted = false;
                        break;
                    }
                }
                if (isAllPermissionGranted) final_inside_task();
                else if (obj != null) {
                    obj.handlePermissionResult(requestCode, permissions, grantResults);
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_Req.REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final_inside_task();
            }
        }
    }


}
