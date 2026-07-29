package com.gonakli.railradar.ADAPTERS;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.NearBy_Station_Structure;

import java.util.ArrayList;



public class NearBy_Station_ListView_Adapter extends ArrayAdapter<NearBy_Station_Structure> {
    ArrayList<NearBy_Station_Structure> arrNearByStations;

    public static class viewholder{
        TextView StCode;
        TextView StName;
        TextView StDistance;
        ImageView mapIcon;

    }
    public NearBy_Station_ListView_Adapter(@NonNull Context context, ArrayList<NearBy_Station_Structure> arrNearByStations) {
        super(context,0,arrNearByStations);
        this.arrNearByStations = arrNearByStations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
     viewholder holder;
      NearBy_Station_Structure item = getItem(position);
        Log.d("holdercome", "getView: " + position);

      if(convertView == null){
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.nearby_station_listview_adapter_layout, parent, false);
          holder = new viewholder();
          holder.StCode = convertView.findViewById(R.id.adapterStationCode);
          holder.StName = convertView.findViewById(R.id.adapterStationName);
          holder.StDistance = convertView.findViewById(R.id.adapterStationDistance);
          holder.mapIcon = convertView.findViewById(R.id.nearbyStationMapIcon);
          convertView.setTag(holder);
      }else {
          holder = (viewholder) convertView.getTag();
      }

      assert item != null;
        holder.StCode .setText(item.getStationCode());
        holder.StName.setText(item.getStationName());
        holder.StDistance.setText(String.format("%.2f KM",item.getStationDistance()));

        holder.mapIcon.setOnClickListener(v ->{
            String latitude = item.getStationLat();
            String longitude = item.getStationLng();
            try {
                String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
                Intent iMaps = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                iMaps.setPackage("com.google.android.apps.maps");
                getContext().startActivity(iMaps);
            } catch (ActivityNotFoundException e) {
                String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                Intent iBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(iBrowser);
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        if(arrNearByStations.size() >=20){
            return 20;
        }else{
            return  arrNearByStations.size();
        }
    }
}
