package com.gonakli.railradar.UserRouteTrains;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.ADAPTERS.User_Route_Train_Recycler_View_Adapter;
import com.gonakli.railradar.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class User_Route_Train_List extends AppCompatActivity {
    String from_Station_Value,to_Station_Value;
    TextView fromStation, toStation;
    RecyclerView recyclerView;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_route_train_list);
        find_all_id();
        set_Station_sName_In_Ui();
        setToolBar();
        recyclerViewSetup();
    }

    private void recyclerViewSetup() {
        new Thread(() ->{
            Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(getApplicationContext());
            ArrayList<Train_Schedule_Structure> arrSchedule = dbHelper.getTrainsBetweenStations(from_Station_Value,to_Station_Value);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            Collections.sort(arrSchedule, (t1, t2) -> {
                String time1 = t1.getStationList().get(0).getArrivalTime();
                if(time1 == null || time1.equalsIgnoreCase("--")){
                    time1 =  t1.getStationList().get(0).getDepartureTime();
                }
                String time2 = t2.getStationList().get(0).getArrivalTime();
                if(time2 == null || time2.equalsIgnoreCase("--")){
                    time2 =  t2.getStationList().get(0).getDepartureTime();
                }

                LocalTime lt1 = LocalTime.parse(time1, formatter);
                LocalTime lt2 = LocalTime.parse(time2, formatter);

                return lt1.compareTo(lt2);
            });
            User_Route_Train_Recycler_View_Adapter recycler_adapter = new User_Route_Train_Recycler_View_Adapter(User_Route_Train_List.this, arrSchedule, from_Station_Value, to_Station_Value);
        this.runOnUiThread(() ->{
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(recycler_adapter);
        });
        }).start();


    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("Search Results");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void set_Station_sName_In_Ui() {
        Intent intent = getIntent();
        from_Station_Value = intent.getStringExtra("fromStation");
        to_Station_Value = intent.getStringExtra("toStation");
        fromStation.setText(from_Station_Value);
        toStation.setText(to_Station_Value);
    }

    private void find_all_id() {
    fromStation = findViewById(R.id.user_route_from_station_textView);
    toStation = findViewById(R.id.user_route_to_station_textView);
    recyclerView = findViewById(R.id.trainScheduleRecyclerView);
    toolbar = findViewById(R.id.application_custom_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
