package com.gonakli.railradar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railradar.ADAPTERS.Stations_Dropdown_Adapter;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.gonakli.railradar.DB_WORK.Station_List_DB_Helper;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;

public class Combined_Home_Screen_Dashboard extends Fragment {
    View combined_Home_Screen;
    MaterialAutoCompleteTextView fromStation, toStation;
    TextView fromStationCodeBadge, toStationCodeBadge;
    Button btnFindTrain;

    ImageButton btn_swap_stations;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    combined_Home_Screen = inflater.inflate(R.layout.combined_home_screen_dashboard, container, false);
    find_all_id();
    add_Predictive_Text();
    from_station_to_station_fields();
    Action_On_Swap_Button();
   return combined_Home_Screen;
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

            toStation.setText(fromStationValue);
            toStationCodeBadge.setText(fromStationCodeBadgeValue);
            fromStation.setText(toStationValue);
            fromStationCodeBadge.setText(toStationCodeBadgeValue);
        });
    }

    private void add_Predictive_Text() {
        // custom adapter needed for proper functioning
        ArrayList<Station_List_Structure> arrStationList = new Station_List_DB_Helper(getContext()).getStationList();
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

    }

    private void find_all_id() {
        fromStation = combined_Home_Screen.findViewById(R.id.from_station);
        toStation = combined_Home_Screen.findViewById(R.id.to_station);
        btnFindTrain = combined_Home_Screen.findViewById(R.id.btn_find_train);
        fromStationCodeBadge = combined_Home_Screen.findViewById(R.id.from_station_code_badge);
        toStationCodeBadge = combined_Home_Screen.findViewById(R.id.to_station_code_badge);
        btn_swap_stations = combined_Home_Screen.findViewById(R.id.btn_swap_stations);
    }

    private void from_station_to_station_fields() {
        btnFindTrain.setOnClickListener(v ->{
            String fromStation_value = fromStation.getText().toString().trim();
            String toStation_value = toStation.getText().toString().trim();

            if(fromStation_value.isBlank() ){
                Toast.makeText(getActivity(), "Enter a valid source station", Toast.LENGTH_SHORT)
                        .show();
                return;
            }else{
                Intent iUserRouteTrainList = new Intent(getActivity(), User_Route_Train_List.class);
                iUserRouteTrainList.putExtra("fromStation", fromStation_value);
                iUserRouteTrainList.putExtra("toStation", toStation_value);
                startActivity(iUserRouteTrainList);
            }

        });


    }
}
