package com.gonakli.railradar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

public class User_Route_Train_Recycler_View extends RecyclerView.Adapter<User_Route_Train_Recycler_View.viewHolder> {
    Context context;
    String fromStationCode, toStationCode;
    ArrayList<Train_Schedule_Structure> arrScheduleList;
    User_Route_Train_Recycler_View(Context context, ArrayList<Train_Schedule_Structure> arrScheduleList, String fromStationCode, String toStationCode){
        this.context = context;
        this.arrScheduleList = new ArrayList<>(arrScheduleList);
        this.fromStationCode = fromStationCode;
        this.toStationCode = toStationCode;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_for_train_schedule_recycler, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ArrayList < Train_Schedule_Station_Structure> arrStation = arrScheduleList.get(position).getStationList();
        String arrivalTime = "", finalDestinationReachTime = "";
        String runningDays = "";





        for(Train_Schedule_Station_Structure stationData : arrStation){
            if(stationData.getStationCode().equals(fromStationCode)){
                arrivalTime = stationData.getArrivalTime();
            }
            if(stationData.getStationCode().equals(toStationCode)){
                finalDestinationReachTime = stationData.getArrivalTime();
            }
        }
        String runOnMon = arrScheduleList.get(position).getTrainRunsOnMon();
        String runOnTue = arrScheduleList.get(position).getTrainRunsOnTue();
        String runOnWed = arrScheduleList.get(position).getTrainRunsOnWed();
        String runOnThu = arrScheduleList.get(position).getTrainRunsOnThu();
        String runOnFri = arrScheduleList.get(position).getTrainRunsOnFri();
        String runOnSat = arrScheduleList.get(position).getTrainRunsOnSat();
        String runOnSun = arrScheduleList.get(position).getTrainRunsOnSun();

        if(
                runOnMon.equalsIgnoreCase("Y") &&
                        runOnTue.equalsIgnoreCase("Y") &&
                        runOnWed.equalsIgnoreCase("Y") &&
                        runOnThu.equalsIgnoreCase("Y") &&
                        runOnFri.equalsIgnoreCase("Y") &&
                        runOnSat.equalsIgnoreCase("Y") &&
                        runOnSun.equalsIgnoreCase("Y")
        ){
                runningDays = "Daily";
        }else{
            StringBuilder runningDaysBuilder = new StringBuilder();
           if (runOnMon.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Mon ");
           if (runOnTue.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Tue ");
           if (runOnWed.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Wed ");
           if (runOnThu.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Thu ");
           if (runOnFri.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Fri ");
           if (runOnSat.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Sat ");
           if (runOnSun.equalsIgnoreCase("Y") ) runningDaysBuilder.append("Sun");
           runningDays = runningDaysBuilder.toString().trim();
        }

        String journeyDuration ="2 hours";





        String trainNumber = arrScheduleList.get(position).getTrainNumber();
        String trainName = arrScheduleList.get(position).getTrainName();



        holder.trainScheduleTrainNumber.setText(trainNumber);
        holder.trainScheduleTrainName.setText(trainName);
        holder.trainScheduleArrivalTime.setText(arrivalTime);
        holder.trainScheduleJourneyDuration.setText(journeyDuration);
        holder.trainScheduleFinalDestinationReachTime.setText(finalDestinationReachTime);
        holder.trainScheduleRunningDays.setText(runningDays);


    }

    @Override
    public int getItemCount() {
        return arrScheduleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView trainScheduleTrainNumber,trainScheduleArrivalTime,trainScheduleJourneyDuration;
        TextView trainScheduleFinalDestinationReachTime,trainScheduleTrainName,trainScheduleRunningDays;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            trainScheduleTrainNumber = itemView.findViewById(R.id.trainScheduleTrainNumber);
            trainScheduleArrivalTime = itemView.findViewById(R.id.trainScheduleArrivalTime);
            trainScheduleJourneyDuration = itemView.findViewById(R.id.trainScheduleJourneyDuration);
            trainScheduleFinalDestinationReachTime = itemView.findViewById(R.id.trainScheduleFinalDestinationReachTime);
            trainScheduleTrainName = itemView.findViewById(R.id.trainScheduleTrainName);
            trainScheduleRunningDays = itemView.findViewById(R.id.trainScheduleRunningDays);
        }

    }

    private void runningDaysFormatter(){

    }
}
