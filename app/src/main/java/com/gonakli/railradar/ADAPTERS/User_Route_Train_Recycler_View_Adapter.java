package com.gonakli.railradar.ADAPTERS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railradar.TrainTracking.Train_Tracking;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class User_Route_Train_Recycler_View_Adapter extends RecyclerView.Adapter<User_Route_Train_Recycler_View_Adapter.viewHolder> {
    Context context;
    String fromStationCode, toStationCode;
    ArrayList<Train_Schedule_Structure> arrScheduleList;
   public User_Route_Train_Recycler_View_Adapter(Context context, ArrayList<Train_Schedule_Structure> arrScheduleList, String fromStationCode, String toStationCode){
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
                if(arrivalTime.equalsIgnoreCase("--")){
                    arrivalTime = stationData.getDepartureTime();
                }
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



        String[] splitArrival = arrivalTime.split(":", 2);
        String[] splitDepart = finalDestinationReachTime.split(":", 2);

        int arrivalHour = Integer.parseInt(splitArrival[0]);
        int arrivalMinutes = Integer.parseInt(splitArrival[1]);
        int deptHour = Integer.parseInt(splitDepart[0]);
        int deptMinutes = Integer.parseInt(splitDepart[1]);
        int arrivalTotalMinutes = arrivalHour * 60 + arrivalMinutes;
        int departTotalMinutes = deptHour * 60 + deptMinutes;
        if(departTotalMinutes < arrivalTotalMinutes){
            departTotalMinutes += 24*60;
        }

        int difference = Math.abs(departTotalMinutes - arrivalTotalMinutes);
        int hours = difference/60;
        int minutes = difference%60;


        String journeyDuration = hours + " Hours " + minutes + " Minutes";






        String trainNumber = arrScheduleList.get(position).getTrainNumber();
        String trainName = arrScheduleList.get(position).getTrainName();



        holder.trainScheduleTrainNumber.setText(trainNumber);
        holder.trainScheduleTrainName.setText(trainName);
        holder.trainScheduleArrivalTime.setText(arrivalTime);
        holder.trainScheduleJourneyDuration.setText(journeyDuration);
        holder.trainScheduleFinalDestinationReachTime.setText(finalDestinationReachTime);
        holder.trainScheduleRunningDays.setText(runningDays);

        holder.itemView.setOnClickListener(v->{
            String trNumber = holder.trainScheduleTrainNumber.getText().toString();
            String trName = holder.trainScheduleTrainName.getText().toString();
            Intent trainTracking = new Intent(context.getApplicationContext(), Train_Tracking.class);
            trainTracking.putExtra("trainNumber", trNumber);
            trainTracking.putExtra("trainName", trName);
            context.startActivity(trainTracking);
        });
            final String serviceDays = runningDays;
        holder.trainScheduleTrainNumber.setOnClickListener(v ->{
            TextView infoTrainName, infoTrainNumber, infoTrainService, infoTrainJourneyTime;
            TextView infoTrainStops, infoTrainTotalDistance, infoTrainStartingPoint, infoTrainEndingPoint;
            Button btnClose;


            BottomSheetDialog dialog = new BottomSheetDialog( context);
            dialog.setContentView(R.layout.user_route_train_info_bottom_layout);

            infoTrainName = dialog.findViewById(R.id.userRouteBottomDialogueTrainName);
            infoTrainNumber = dialog.findViewById(R.id.userRouteBottomDialogueTrainNumber);
            infoTrainService = dialog.findViewById(R.id.userRouteBottomDialogueTrainService);
            infoTrainJourneyTime = dialog.findViewById(R.id.userRouteBottomDialogueTrainTravelTime);
            infoTrainStops = dialog.findViewById(R.id.userRouteBottomDialogueTrainTotalStops);
            infoTrainTotalDistance = dialog.findViewById(R.id.userRouteBottomDialogueTrainTotalDistance);
            infoTrainStartingPoint = dialog.findViewById(R.id.userRouteBottomDialogueTrainStartingPoint);
            infoTrainEndingPoint = dialog.findViewById(R.id.userRouteBottomDialogueTrainEndingPoint);
            btnClose = dialog.findViewById(R.id.btnCloseDialog);

            String[] journeyTime = arrScheduleList.get(position).getDuration().split(":", 2);
            String formattedJourneyTime = String.format("%s Hours %s Minutes",journeyTime[0],journeyTime[1]);

            infoTrainName.setText(trainName);
            infoTrainNumber.setText(trainNumber);
            infoTrainService.setText(serviceDays);
            infoTrainJourneyTime.setText(formattedJourneyTime);
            infoTrainStops.setText(String.valueOf(arrStation.size()));
            infoTrainTotalDistance.setText(String.valueOf(arrStation.get(arrStation.size() - 1).getDistance() + " Km"));
            infoTrainStartingPoint.setText(arrStation.get(0).getStationName());
            infoTrainEndingPoint.setText(arrStation.get(arrStation.size() - 1).getStationName());



            dialog.show();
            btnClose.setOnClickListener(x -> {
                dialog.dismiss();
            });


        });


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

}
