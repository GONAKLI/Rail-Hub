package com.gonakli.railHub.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonakli.railHub.Structure_Class.Station_List_Structure;
import com.gonakli.railHub.R;

import java.util.ArrayList;
import java.util.List;

public class Stations_Dropdown_Adapter extends ArrayAdapter<Station_List_Structure> {
    private final ArrayList<Station_List_Structure> arrStationData = new ArrayList<>();

    public Stations_Dropdown_Adapter(@NonNull Context context, List<Station_List_Structure> station_List) {
        super(context, 0);
        arrStationData.addAll(station_List);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.auto_complete_hint_layout_for_from_station_to_station_input_fields, parent, false);
        }

        Station_List_Structure station = arrStationData.get(position);
        TextView stationCode = convertView.findViewById(R.id.hint_for_station_code);
        TextView stationName = convertView.findViewById(R.id.hint_for_station_name);

        if (station != null) {
            stationCode.setText(station.getStation_Code());
            stationName.setText(station.getStation_Name());
        } else {
            stationCode.setText("");
            stationName.setText("");
        }

        convertView.setTag(station);
        return convertView;
    }

    public void updateData(ArrayList<Station_List_Structure> arrStationData) {
        this.arrStationData.clear();
        this.arrStationData.addAll(arrStationData);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.count = arrStationData.size();
                results.values = arrStationData;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return "";
            }
        };
    }

    @Override
    public int getCount() {
        return arrStationData.size();
    }

    @Nullable
    @Override
    public Station_List_Structure getItem(int position) {
        if (position >= 0 && position < arrStationData.size()) {
            return arrStationData.get(position);
        }
        return null;
    }
}
