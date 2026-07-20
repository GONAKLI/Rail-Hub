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
    private List<Station_List_Structure> originalList;     // full list
    private List<Station_List_Structure> filteredList;     // filtered list

    public Stations_Dropdown_Adapter(@NonNull Context context, List<Station_List_Structure> station_List) {
        super(context, 0, station_List);
        this.originalList = new ArrayList<>(station_List);
        this.filteredList = new ArrayList<>(station_List);
    }

    @Override
    public int getCount() {
        return Math.min(filteredList == null ? 0 : filteredList.size(), 5);
    }

    @Nullable
    @Override
    public Station_List_Structure getItem(int position) {
        if (filteredList != null && position >= 0 && position < filteredList.size()) {
            return filteredList.get(position);
        }
        return null;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originalList;
                    results.count = originalList.size();
                } else {
                    List<Station_List_Structure> filtered = new ArrayList<>();
                    for (Station_List_Structure s : originalList) {
                        if (s.getStation_Name().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                s.getStation_Code().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filtered.add(s);
                        }
                    }
                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Station_List_Structure>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
