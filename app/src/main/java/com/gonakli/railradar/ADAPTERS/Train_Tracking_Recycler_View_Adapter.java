package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;

import java.util.ArrayList;

public class Train_Tracking_Recycler_View_Adapter extends RecyclerView.Adapter<Train_Tracking_Recycler_View_Adapter.myViewHolder> {

    Context context;
    String fromStationCode, toStationCode;
    Train_Schedule_Structure trainData;
    ArrayList<Train_Schedule_Station_Structure> arrTrainStations;

    public Train_Tracking_Recycler_View_Adapter(Context context, Train_Schedule_Structure trainData,String fromStationCode, String toStationCode){
        this.context = context;
        this.trainData = trainData;
        arrTrainStations = trainData.getStationList();
        this.fromStationCode = fromStationCode;
        this.toStationCode = toStationCode;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_train_tracking_recycler_view_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String fromStCode = arrTrainStations.get(position).getStationCode();
        String toStCode = arrTrainStations.get(position).getStationCode();
        holder.trainStationName.setTextColor(Color.BLACK);
        if(fromStationCode != null && fromStationCode.equalsIgnoreCase(fromStCode)){
                holder.trainStationName.setTextColor(Color.GREEN);
        }
        if (toStationCode != null && toStationCode.equalsIgnoreCase(toStCode)){
            holder.trainStationName.setTextColor(Color.RED);
        }
        String stName = arrTrainStations.get(position).getStationName();
        String arrAt = arrTrainStations.get(position).getArrivalTime();
        String depAt = arrTrainStations.get(position).getDepartureTime();
        String actualArrAt;
        String actualDepAt;
        String distanceTrav = arrTrainStations.get(position).getDistance();
        String platformAt;

        Log.d("testingRecycler", "onBindViewHolder: "+ stName + " " + arrAt + " " + depAt + " " + distanceTrav);
        holder.trainStationName.setText(stName);
        holder.trainArrivalAt.setText(arrAt);
        holder.trainDepartureAt.setText(depAt);
        holder.trainDistanceTravelled.setText(distanceTrav);

    }

    @Override
    public int getItemCount() {
        return arrTrainStations.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
TextView trainStationName,trainArrivalAt,trainDepartureAt;
TextView trainActualArrivalAt,trainActualDepartureAt, trainDistanceTravelled, trainPlatformNo;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            trainStationName = itemView.findViewById(R.id.trainStationName);
            trainArrivalAt = itemView.findViewById(R.id.trainArrivalAt);
            trainDepartureAt = itemView.findViewById(R.id.trainDepartureAt);
            trainActualArrivalAt = itemView.findViewById(R.id.trainActualArrivalAt);
            trainActualDepartureAt = itemView.findViewById(R.id.trainActualDepartureAt);
            trainDistanceTravelled = itemView.findViewById(R.id.trainDistanceTravelled);
            trainPlatformNo = itemView.findViewById(R.id.trainPlatformNo);
        }
    }


}
