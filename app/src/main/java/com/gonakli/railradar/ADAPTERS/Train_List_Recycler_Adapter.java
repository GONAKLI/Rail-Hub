package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.Structure_Class.Train_List_Structure;
import com.gonakli.railradar.R;
import com.gonakli.railradar.TrainTracking.Train_Tracking;

import java.util.ArrayList;

public class Train_List_Recycler_Adapter extends RecyclerView.Adapter<Train_List_Recycler_Adapter.viewHolder>{
    Context context;
    ArrayList<Train_List_Structure> arrTrainList;       // current list
    ArrayList<Train_List_Structure> arrTrainListFull;   // backup list

    public Train_List_Recycler_Adapter(Context context, ArrayList<Train_List_Structure> arrTrainList){
        this.context = context;
        this.arrTrainList = new ArrayList<>(arrTrainList);
        this.arrTrainListFull = new ArrayList<>(arrTrainList); // backup copy
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getBindingAdapterPosition();
               if(pos != RecyclerView.NO_POSITION){
                  Train_List_Structure clickedItem = arrTrainList.get(pos);
                  Intent trainTrackingLive = new Intent(context, Train_Tracking.class);
                  trainTrackingLive.putExtra("trainNumber", clickedItem.getTrainNumber());
                  trainTrackingLive.putExtra("trainName", clickedItem.getTrainName());
                  context.startActivity(trainTrackingLive);
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrTrainList.size() >=15){
            return 15;
        }
        return arrTrainList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView trainNumber, trainName, trainRouteFrom, trainRouteTo;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            trainNumber = itemView.findViewById(R.id.trainNumber);
            trainName = itemView.findViewById(R.id.trainName);
//            trainRouteFrom = itemView.findViewById(R.id.trainRouteFrom);
//            trainRouteTo = itemView.findViewById(R.id.trainRouteTo);
        }
    }

    // 🔹 Filter method
    public void filterSearchResult(String text){
        ArrayList<Train_List_Structure> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            filteredList.addAll(arrTrainListFull);
        } else {
            try {
                int trainNumber = Integer.parseInt(text);
                for (Train_List_Structure data: arrTrainListFull){
                    if(data.getTrainNumber().contains(text)){
                        filteredList.add(data);
                    }
                }
            } catch (NumberFormatException e) {
                for (Train_List_Structure data: arrTrainListFull){
                    if(data.getTrainName().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(data);
                    }
                }
            }
        }
        updateList(filteredList);
    }

    public void updateList(ArrayList<Train_List_Structure> newList){
        arrTrainList.clear();
        arrTrainList.addAll(newList);
        notifyDataSetChanged();
    }
}
