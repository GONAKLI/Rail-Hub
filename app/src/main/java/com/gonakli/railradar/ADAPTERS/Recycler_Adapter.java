package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.Structure_Class.Train_List_Structure;
import com.gonakli.railradar.R;

import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.viewHolder>{
    Context context;
    ArrayList<Train_List_Structure> arrTrainList;
    public Recycler_Adapter(Context context, ArrayList<Train_List_Structure> arrTrainList){
    this.context = context;
    this.arrTrainList = arrTrainList;
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

    }

    @Override
    public int getItemCount() {
        return arrTrainList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
TextView trainNumber, trainName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            trainNumber = itemView.findViewById(R.id.trainNumber);
            trainName = itemView.findViewById(R.id.trainName);
        }
    }

}