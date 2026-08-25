package com.gonakli.railradar.HomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gonakli.railradar.ADAPTERS.Stations_Dropdown_Adapter;
import com.gonakli.railradar.DB_WORK.Input_Field_Last_Search_DB_Helper;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Input_Field_Last_Search_Structure;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.gonakli.railradar.UserRouteTrains.User_Route_Train_List;

import java.util.ArrayList;
import java.util.Objects;

public class Input_From_To_Station extends LinearLayout {
    Context context;
    AutoCompleteTextView fromStation, toStation;
    TextView fromStationCodeBadge, toStationCodeBadge;
    Button btnFindTrain;
    Stations_Dropdown_Adapter customStationAdapter;

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
        get_Recent_Field_Data_From_DB();

    }

    private void get_Recent_Field_Data_From_DB() {
        try(Input_Field_Last_Search_DB_Helper db = new Input_Field_Last_Search_DB_Helper(context)){
            Input_Field_Last_Search_Structure recentData = db.getRecentFieldData();
            if(recentData != null){
                fromStationCodeBadge.setText(recentData.getFromCode());
                fromStation.setText(recentData.getFromValue());
                fromStationCodeBadge.setVisibility(VISIBLE);

                toStationCodeBadge.setText(recentData.getToCode());
                toStation.setText(recentData.getToValue());
                toStationCodeBadge.setVisibility(VISIBLE);
            }
        }
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
            ArrayList<Station_List_Structure> arrStationList = new ArrayList<>(new Station_List_DB_Helper(getContext()).getDataForPredictiveTextFields(null));

            ((Activity) context).runOnUiThread(() -> {
                 customStationAdapter = new Stations_Dropdown_Adapter(getContext(), arrStationList);


                fromStation.setAdapter(customStationAdapter);
                fromStation.setThreshold(0);
                fromStation.setDropDownHeight(900);
                toStation.setAdapter(customStationAdapter);
                toStation.setThreshold(0);
                input_Field_Text_Change_Listeners(fromStation, toStation);

                fromStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Station_List_Structure selected = null;
                        if(view !=null && view.getTag() instanceof Station_List_Structure){
                            selected = (Station_List_Structure) view.getTag();
                        }
//                        else if (customStationAdapter !=null && position < customStationAdapter.getCount()) {
//                            selected = customStationAdapter.getItem(position);
//                        }
                        if(selected !=null){
                            String stCode = selected.getStation_Code();
                            String stName = selected.getStation_Name();
                            if(stCode != null){
                                fromStationCodeBadge.setText(stCode);
                                fromStationCodeBadge.setVisibility(View.VISIBLE);
                            }
                            if(stName !=null){
                                fromStation.setText(stName,false);
                            }
                            toStation.requestFocus();
                        }

//                        Station_List_Structure selected =(Station_List_Structure) parent.getItemAtPosition(position);
//                        fromStationCodeBadge.setText(selected.getStation_Code());
//                        fromStationCodeBadge.setVisibility(View.VISIBLE);
//                        fromStation.setText(selected.getStation_Name());
//                        toStation.requestFocus();
                    }
                });

                toStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Station_List_Structure selected = null;
                        if(view != null && view.getTag() instanceof Station_List_Structure){
                            selected = (Station_List_Structure) view.getTag();
                        }
                        if(selected != null){
                            String stCode = selected.getStation_Code();
                            String stName = selected.getStation_Name();
                            if(stCode !=null){
                                toStationCodeBadge.setText(stCode);
                                toStationCodeBadge.setVisibility(View.VISIBLE);
                            }
                            if(stName != null){
                                toStation.setText(stName);
                            }
                        }

//                        Station_List_Structure selected = (Station_List_Structure) parent.getItemAtPosition(position);
//                        toStationCodeBadge.setText(selected.getStation_Code());
//                        toStationCodeBadge.setVisibility(View.VISIBLE);
//
//                        toStation.setText(selected.getStation_Name());
                        InputMethodManager imm =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(toStation.getWindowToken(), 0);
                    }
                });
            });
        }).start();

    }

    private void input_Field_Text_Change_Listeners(AutoCompleteTextView fromStation, AutoCompleteTextView toStation) {
        fromStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try(Station_List_DB_Helper dbHelper = new Station_List_DB_Helper(context)) {
                    ArrayList<Station_List_Structure> myData = new ArrayList<>();
                    myData.clear();
                    myData.addAll( dbHelper.getDataForPredictiveTextFields(s.toString()));
                    if (customStationAdapter != null) {
                        customStationAdapter.updateData(myData);
                    }
                }
            }
        });

        toStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try(Station_List_DB_Helper dbHelper = new Station_List_DB_Helper(context)){
                    ArrayList<Station_List_Structure> myData = new ArrayList<>();
                    myData.clear();
                    myData.addAll(dbHelper.getDataForPredictiveTextFields(s.toString()));
                    if(customStationAdapter != null){
                        Log.d("customer", "onTextChanged: " + "called");
                        customStationAdapter.updateData(myData);
                    }
                }
            }
        });
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
                new Thread(() ->{
                    try(Input_Field_Last_Search_DB_Helper db = new Input_Field_Last_Search_DB_Helper(context);
                        Station_List_DB_Helper stDbHelper = new Station_List_DB_Helper(context)){
                        Input_Field_Last_Search_Structure structureObj = new Input_Field_Last_Search_Structure(
                                fromStationCode_value,stDbHelper.getStationNameByCode(fromStationCode_value),toStationCode_value,stDbHelper.getStationNameByCode(toStationCode_value)
                        );
                        db.insertRecentFieldDataInDB(structureObj);
                    }
                }).start();
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
