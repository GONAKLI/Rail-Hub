package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.User_History_Structure;
import com.gonakli.railradar.TrainTracking.Train_Tracking;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class User_History_ListView_Adapter extends ArrayAdapter<User_History_Structure> {
    public static  class viewHolder{
        TextView trNumber;
        TextView trName;
        TextView sourceStation;
        TextView destinationStation;
    }

    Context context;
    ArrayList<User_History_Structure> userHistory;

    public User_History_ListView_Adapter(@NonNull Context context, ArrayList<User_History_Structure> userHistory) {
        super(context,0);
        this.context = context;
        this.userHistory = userHistory;
    }
    public User_History_ListView_Adapter(@NonNull Context context) {
        super(context,0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        viewHolder holder = new viewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_list_view_history_activity, parent,false);

        holder.trNumber = convertView.findViewById(R.id.historyTrainNumber);
        holder.trName = convertView.findViewById(R.id.historyTrainName);
        holder.sourceStation = convertView.findViewById(R.id.historySource);
        holder.destinationStation = convertView.findViewById(R.id.historyDestination);

        holder.trNumber.setText(userHistory.get(position).getTrainNumber());
        holder.trName.setText(userHistory.get(position).getTrainName());
        holder.sourceStation.setText(userHistory.get(position).getTrainSource());
        holder.destinationStation.setText(userHistory.get(position).getTrainDestination());

        convertView.setOnClickListener(v ->{
            Intent iTracking = new Intent(getContext(), Train_Tracking.class);
            String trainNumber = holder.trNumber.getText().toString();
            String trainName = holder.trName.getText().toString();
            String sourceStation = holder.sourceStation.getText().toString();
            String destinationStation = holder.destinationStation.getText().toString();
            iTracking.putExtra("trainNumber", trainNumber );
            iTracking.putExtra("trainName", trainName);
            if(!sourceStation.isBlank() && !destinationStation.isBlank()){
                iTracking.putExtra("fromStationCode", sourceStation );
                iTracking.putExtra("toStationCode", destinationStation );
            }
            context.startActivity(iTracking);
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return userHistory.size();
    }
}
