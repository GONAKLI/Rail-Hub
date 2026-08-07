package com.gonakli.railradar.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gonakli.railradar.ADAPTERS.Stations_Dropdown_Adapter;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.gonakli.railradar.UserRouteTrains.User_Route_Train_List;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;

public class Input_From_To_Station extends LinearLayout {
    Context context;
    MaterialAutoCompleteTextView fromStation, toStation;
    TextView fromStationCodeBadge, toStationCodeBadge;
    Button btnFindTrain;

    ImageButton btn_swap_stations;


    public Input_From_To_Station(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.input_field_from_station_to_station, this, true);
        find_all_id();
        add_Predictive_Text();
        from_station_to_station_fields();
        Action_On_Swap_Button();
        clearBadge();

    }
    private void Action_On_Swap_Button() {
        btn_swap_stations.setOnClickListener(v ->{
            fromStation.clearFocus();
            toStation.clearFocus();
            Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.swap_button_rotation);
            btn_swap_stations.startAnimation(rotation);


            String fromStationValue = fromStation.getText().toString();
            String fromStationCodeBadgeValue =   fromStationCodeBadge.getText().toString();
            String toStationValue =  toStation.getText().toString();
            String toStationCodeBadgeValue =  toStationCodeBadge.getText().toString();


            if(!fromStationCodeBadgeValue.isBlank()){
                toStation.setText(fromStationValue);
                toStationCodeBadge.setText(fromStationCodeBadgeValue);
                toStationCodeBadge.setVisibility(VISIBLE);
            }
            if(!toStationCodeBadgeValue.isBlank()){
                fromStation.setText(toStationValue);
                fromStationCodeBadge.setText(toStationCodeBadgeValue);
                fromStationCodeBadge.setVisibility(VISIBLE);
            }


        });
    }

    private void add_Predictive_Text() {
        new Thread(() -> {
            // custom adapter needed for proper functioning
            ArrayList<Station_List_Structure> arrStationList = new Station_List_DB_Helper(getContext()).getStationList();

            ((android.app.Activity) context).runOnUiThread(() -> {
                Stations_Dropdown_Adapter customStationAdapter = new Stations_Dropdown_Adapter(getContext(), arrStationList);


                fromStation.setAdapter(customStationAdapter);
                fromStation.setThreshold(0);
                fromStation.setDropDownHeight(900);
                toStation.setAdapter(customStationAdapter);
                toStation.setThreshold(0);

                fromStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Station_List_Structure selected = (Station_List_Structure) parent.getItemAtPosition(position);
                        fromStationCodeBadge.setText(selected.getStation_Code());
                        fromStationCodeBadge.setVisibility(View.VISIBLE);

                        fromStation.setText(selected.getStation_Name());
                        toStation.requestFocus();
                    }
                });

                toStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Station_List_Structure selected = (Station_List_Structure) parent.getItemAtPosition(position);
                        toStationCodeBadge.setText(selected.getStation_Code());
                        toStationCodeBadge.setVisibility(View.VISIBLE);

                        toStation.setText(selected.getStation_Name());
                        InputMethodManager imm =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(toStation.getWindowToken(), 0);
                    }
                });
            });
        }).start();

    }

    private void find_all_id() {
        fromStation = findViewById(R.id.from_station);
        toStation = findViewById(R.id.to_station);
        btnFindTrain = findViewById(R.id.btn_find_train);
        fromStationCodeBadge = findViewById(R.id.from_station_code_badge);
        toStationCodeBadge = findViewById(R.id.to_station_code_badge);
        btn_swap_stations = findViewById(R.id.btn_swap_stations);
    }

    private void from_station_to_station_fields() {

        btnFindTrain.setOnClickListener(v ->{
            String fromStationCode_value = fromStationCodeBadge.getText().toString().trim();
            String toStationCode_value = toStationCodeBadge.getText().toString().trim();

            if(fromStationCode_value.isBlank() || toStationCode_value.isBlank()){
                Toast.makeText(context, "Select a valid station", Toast.LENGTH_SHORT)
                        .show();
                return;
            }else{
                Intent iUserRouteTrainList = new Intent(context, User_Route_Train_List.class);
                iUserRouteTrainList.putExtra("fromStation", fromStationCode_value);
                iUserRouteTrainList.putExtra("toStation", toStationCode_value);
                context.startActivity(iUserRouteTrainList);
            }

        });


    }

    private void clearBadge(){
        fromStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isBlank()){
                    fromStationCodeBadge.setText("");
                    fromStationCodeBadge.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        toStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isBlank()){
                    toStationCodeBadge.setText("");
                    toStationCodeBadge.setVisibility(INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }


}
