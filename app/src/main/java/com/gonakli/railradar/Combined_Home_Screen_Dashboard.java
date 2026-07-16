package com.gonakli.railradar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

public class Combined_Home_Screen_Dashboard extends Fragment {
    View combined_Home_Screen;
    AppCompatEditText fromStation, toStation;
    Button btnFindTrain;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    combined_Home_Screen = inflater.inflate(R.layout.combined_home_screen_dashboard, container, false);
    find_all_id();
    from_station_to_station_fields();
   return combined_Home_Screen;
    }

    private void find_all_id() {
        fromStation = combined_Home_Screen.findViewById(R.id.from_station);
        toStation = combined_Home_Screen.findViewById(R.id.to_station);
        btnFindTrain = combined_Home_Screen.findViewById(R.id.btn_find_train);
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
