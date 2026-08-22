package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.gonakli.railradar.R;

import java.util.ArrayList;
import java.util.List;

public class Stations_Dropdown_Adapter extends ArrayAdapter<Station_List_Structure> {
    public Stations_Dropdown_Adapter(@NonNull Context context, List<Station_List_Structure> station_List) {
        super(context, 0, station_List);

    }
    
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.auto_complete_hint_layout_for_from_station_to_station_input_fields, parent, false);
        }

        Station_List_Structure station = getItem(position);
        TextView stationCode = convertView.findViewById(R.id.hint_for_station_code);
        TextView stationName = convertView.findViewById(R.id.hint_for_station_name);

        if (station != null) {
            stationCode.setText(station.getStation_Code());
            stationName.setText(station.getStation_Name());
        } else {
            stationCode.setText("");
            stationName.setText("");
        }

        return convertView;
    }

    public void updateData(ArrayList<Station_List_Structure> arrStationData){
        clear();
        addAll(arrStationData);
        notifyDataSetChanged();

    }

}
