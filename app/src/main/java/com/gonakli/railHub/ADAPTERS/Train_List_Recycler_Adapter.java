package com.gonakli.railHub.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railHub.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railHub.DB_WORK.User_Routes_History_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.Train_List_Structure_New;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railHub.TrainTracking.Train_Tracking;
import com.gonakli.railHub.Utility.TrainExtraInfo.ShowTrainInfoDialog;

import java.util.ArrayList;

public class Train_List_Recycler_Adapter extends RecyclerView.Adapter<Train_List_Recycler_Adapter.viewHolder> {
    Context context;
    ArrayList<Train_List_Structure_New> arrTrainList;

    public Train_List_Recycler_Adapter(Context context, ArrayList<Train_List_Structure_New> arrTrainList) {
        this.context = context;
        this.arrTrainList = new ArrayList<>(arrTrainList);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_train_recycler_view_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.trainNumber.setText(arrTrainList.get(position).getTrainNumber());
        holder.trainName.setText(arrTrainList.get(position).getTrainName());
        holder.trainSourceStation.setText(arrTrainList.get(position).getSourceStationName());
        holder.trainDestinationStation.setText(arrTrainList.get(position).getDestinationStationName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Train_List_Structure_New clickedItem = arrTrainList.get(pos);
                    Intent trainTrackingLive = new Intent(context, Train_Tracking.class);
                    trainTrackingLive.putExtra("trainNumber", clickedItem.getTrainNumber());
                    trainTrackingLive.putExtra("trainName", clickedItem.getTrainName());
                    new Thread(() -> {
                        String trNumber = clickedItem.getTrainNumber();
                        String trName = clickedItem.getTrainName();
                        String srStationCode = "";
                        String destStationCode = "";
                        try (Train_Schedule_DB_Helper db = new Train_Schedule_DB_Helper(context)) {
                            Train_Schedule_Structure data = db.getTrainDataByTrainNumber(trNumber);
                            srStationCode = data.getStationFrom();
                            destStationCode = data.getStationTo();
                        }

                        // to save train in history section for future access
                        User_Routes_History_DB_Helper helper = new User_Routes_History_DB_Helper(context);
                        helper.addHistoryInDB(trNumber, trName, srStationCode, destStationCode);
                        helper.close();
                    }).start();
                    context.startActivity(trainTrackingLive);
                }
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            String trNum = arrTrainList.get(position).getTrainNumber();
            ShowTrainInfoDialog.showExtraTrainInfo(trNum, context);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return arrTrainList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView trainNumber, trainName, trainSourceStation, trainDestinationStation;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            trainNumber = itemView.findViewById(R.id.trainNumber);
            trainName = itemView.findViewById(R.id.trainName);
            trainSourceStation = itemView.findViewById(R.id.sourceStation);
            trainDestinationStation = itemView.findViewById(R.id.destinationStation);
        }
    }


    public void updateList(ArrayList<Train_List_Structure_New> newList) {
        arrTrainList.clear();
        arrTrainList.addAll(newList);
        notifyDataSetChanged();
    }
}
