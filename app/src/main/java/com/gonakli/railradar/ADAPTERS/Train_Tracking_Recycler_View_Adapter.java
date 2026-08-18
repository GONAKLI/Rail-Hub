package com.gonakli.railradar.ADAPTERS;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.API_Response_Train_Tracking;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railradar.Structure_Class.Train_Tracking_Structure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Train_Tracking_Recycler_View_Adapter
        extends RecyclerView.Adapter<Train_Tracking_Recycler_View_Adapter.myViewHolder> {

    Context context;
    String fromStationCode, toStationCode;

    Train_Schedule_Structure trainData;
    Train_Tracking_Structure trainLocationData;
    ArrayList<Train_Schedule_Station_Structure> arrTrainStations;

    public Train_Tracking_Recycler_View_Adapter(Context context, Train_Schedule_Structure trainData,
            String fromStationCode, String toStationCode) {
        this.context = context;
        this.trainData = trainData;
        arrTrainStations = trainData.getStationList();
        this.fromStationCode = fromStationCode;
        this.toStationCode = toStationCode;
    }

    public Train_Tracking_Recycler_View_Adapter(Context context, Train_Schedule_Structure trainData) {
        this.context = context;
        this.trainData = trainData;
        arrTrainStations = trainData.getStationList();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_train_tracking_recycler_view_layout, parent,
                false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String fromStCode = arrTrainStations.get(position).getStationCode();
        String toStCode = arrTrainStations.get(position).getStationCode();
        holder.trainStationName.setTextColor(ContextCompat.getColor(context, R.color.card_text_primary));
        if (fromStationCode != null && fromStationCode.equalsIgnoreCase(fromStCode)) {
            holder.trainStationName.setTextColor(Color.GREEN);
        }
        if (toStationCode != null && toStationCode.equalsIgnoreCase(toStCode)) {
            holder.trainStationName.setTextColor(Color.RED);
        }

        set_Train_Icon(holder, position);
        String stName = arrTrainStations.get(position).getStationName();

        String arrAt = arrTrainStations.get(position).getArrivalTime();
        String depAt = arrTrainStations.get(position).getDepartureTime();
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a");
        if (!arrAt.equals("--")) {
            Date date = null;
            try {
                date = inputFormat.parse(arrAt);
            } catch (ParseException e) {

            }
            if (date != null) {
                arrAt = outputFormat.format(date);
            }

        }
        if (!depAt.equals("--")) {
            Date date = null;
            try {
                date = inputFormat.parse(depAt);
            } catch (ParseException e) {

            }
            if (date != null) {
                depAt = outputFormat.format(date);
            }

        }
        String actualArrAt;
        String actualDepAt;
        String distanceTrav = arrTrainStations.get(position).getDistance();
        String platformAt = null;
        if (arrTrainStations.get(position).getPlatform() != null) {
            platformAt = arrTrainStations.get(position).getPlatform();
        }
        String stLat = arrTrainStations.get(position).getStnLat();
        String stLng = arrTrainStations.get(position).getStnLng();

        Log.d("testingRecycler", "onBindViewHolder: " + stName + " " + stLat + " " + stLng + " " + distanceTrav);
        holder.trainStationName.setText(stName);
        holder.trainArrivalAt.setText(arrAt);
        holder.trainDepartureAt.setText(depAt);
        holder.trainDistanceTravelled.setText(distanceTrav);
        if (platformAt != null) {
            holder.trainPlatformNo.setText("PF - " + platformAt);
        }

        holder.stationOnMap.setOnClickListener(v -> {
            try {
                String uri = "geo:" + stLat + "," + stLng + "?q=" + stLat + "," + stLng;
                Intent iMaps = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                iMaps.setPackage("com.google.android.apps.maps");
                context.startActivity(iMaps);
            } catch (ActivityNotFoundException e) {
                String uri = "https://www.google.com/maps/search/?api=1&query=" + stLat + "," + stLng;
                Intent iBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(iBrowser);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrTrainStations.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView trainStationName, trainArrivalAt, trainDepartureAt;
        TextView trainActualArrivalAt, trainActualDepartureAt, trainDistanceTravelled, trainPlatformNo;
        ImageButton stationOnMap;
        ImageView liveTrainIcon;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            trainStationName = itemView.findViewById(R.id.trainStationName);
            trainArrivalAt = itemView.findViewById(R.id.trainArrivalAt);
            trainDepartureAt = itemView.findViewById(R.id.trainDepartureAt);
            trainActualArrivalAt = itemView.findViewById(R.id.trainActualArrivalAt);
            trainActualDepartureAt = itemView.findViewById(R.id.trainActualDepartureAt);
            trainDistanceTravelled = itemView.findViewById(R.id.trainDistanceTravelled);
            trainPlatformNo = itemView.findViewById(R.id.trainPlatformNo);
            stationOnMap = itemView.findViewById(R.id.stationOnMap);
            liveTrainIcon = itemView.findViewById(R.id.liveTrainIcon);
        }
    }

    private void set_Train_Icon(myViewHolder holder, int position) {
        if (trainLocationData != null) {

            Log.d("bbc", "set_Train_Icon: " + trainLocationData.getStatusMessage());
            if (trainLocationData.getCurrentStation() != null) {
                Log.d("bbc", "set_Train_Icon: " + trainLocationData.getCurrentStation().getStationName());
                if (arrTrainStations.get(position).getStationCode()
                        .equals(trainLocationData.getCurrentStation().getStationCode())) {
                    holder.liveTrainIcon.setVisibility(View.VISIBLE);
                } else {
                    holder.liveTrainIcon.setVisibility(View.GONE);
                }
            } else if (trainLocationData.isOnRoute() && trainLocationData.getPreviousStation() != null) {
                if (arrTrainStations.get(position).getStationCode()
                        .equals(trainLocationData.getPreviousStation().getStationCode())) {
                    holder.liveTrainIcon.setVisibility(View.VISIBLE);
                    int rowHeight = holder.itemView.getHeight();
                    float fraction = trainLocationData.getStationCoveredPercentage() / 100.0f;
                    holder.liveTrainIcon.setTranslationY(fraction * rowHeight);
                } else {
                    holder.liveTrainIcon.setVisibility(View.GONE);
                    holder.liveTrainIcon.setTranslationY(0f);

                }
            }
        } else {
            if (arrTrainStations != null && !arrTrainStations.isEmpty() && position == 0) {
                holder.liveTrainIcon.setVisibility(View.VISIBLE);
                holder.liveTrainIcon.setTranslationY(0f);
            } else {
                holder.liveTrainIcon.setVisibility(View.GONE);
            }
        }
    }

    public void updateAdapter(Train_Tracking_Structure trainLocationData) {
        this.trainLocationData = trainLocationData;
        notifyDataSetChanged();
    }

    public void APi_Adapter_Update(Train_Tracking_Structure trainLocationData,
            ArrayList<API_Response_Train_Tracking> apiRes) {
        this.trainLocationData = trainLocationData;
        for (Train_Schedule_Station_Structure st : this.arrTrainStations) {
            for (API_Response_Train_Tracking obj : apiRes) {
                if (st.getStationCode().equals(obj.getStationCode())) {
                    st.setPlatform(obj.getPlatform());
                }
            }
        }
        notifyDataSetChanged();
        
        
    }
}
