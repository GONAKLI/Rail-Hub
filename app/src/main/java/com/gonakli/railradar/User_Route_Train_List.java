package com.gonakli.railradar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class User_Route_Train_List extends AppCompatActivity {
    TextView fromStation, toStation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_route_train_list);
        find_all_id();
        set_Station_sName_In_Ui();
    }

    private void set_Station_sName_In_Ui() {
        Intent intent = getIntent();
        String from_Station_Value = intent.getStringExtra("fromStation");
        String to_Station_Value = intent.getStringExtra("toStation");
        fromStation.setText(from_Station_Value);
        toStation.setText(to_Station_Value);
    }

    private void find_all_id() {
    fromStation = findViewById(R.id.user_route_from_station_textView);
    toStation = findViewById(R.id.user_route_to_station_textView);
    }
}
