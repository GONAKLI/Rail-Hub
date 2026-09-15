package com.gonakli.railHub.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railHub.DB_WORK.User_Routes_History_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railHub.TrainTracking.Train_Tracking;
import com.gonakli.railHub.Utility.DateAndTimeRelated.Day_Of_Week;
import com.gonakli.railHub.Utility.DateAndTimeRelated.Journey_Time_Finder;
import com.gonakli.railHub.Utility.DateAndTimeRelated.Time_Converter;
import com.gonakli.railHub.Utility.DateAndTimeRelated.Train_Journey_Date_Selector;
import com.gonakli.railHub.Utility.TrainExtraInfo.RunningDays;
import com.gonakli.railHub.Utility.TrainExtraInfo.ShowTrainInfoDialog;

import java.time.Duration;
import java.util.ArrayList;

public class User_Route_Train_Recycler_View_Adapter extends RecyclerView.Adapter<User_Route_Train_Recycler_View_Adapter.viewHolder> {
    Context context;
    String fromStationCode, toStationCode;
    ArrayList<Train_Schedule_Structure> arrScheduleList;

    public User_Route_Train_Recycler_View_Adapter(Context context, ArrayList<Train_Schedule_Structure> arrScheduleList, String fromStationCode, String toStationCode) {
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
        ArrayList<Train_Schedule_Station_Structure> arrStation = arrScheduleList.get(position).getStationList();
        String arrivalTime = "", finalDestinationReachTime = "";
        String runningDays = RunningDays.getTrainRunningDays(arrScheduleList.get(position));
        int startDayCount = Integer.MIN_VALUE;
        int endDayCount = Integer.MIN_VALUE;


        for (Train_Schedule_Station_Structure stationData : arrStation) {
            if (stationData.getStationCode().equals(fromStationCode)) {
                arrivalTime = stationData.getArrivalTime();
                if (arrivalTime.equalsIgnoreCase("--")) {
                    arrivalTime = stationData.getDepartureTime();
                }
                try {
                    startDayCount = Integer.parseInt(stationData.getDayCount());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
            if (stationData.getStationCode().equals(toStationCode)) {
                finalDestinationReachTime = stationData.getArrivalTime();
                try {
                    endDayCount = Integer.parseInt(stationData.getDayCount());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("timeTesting", "onBindViewHolder: arr " + arrivalTime);

        Log.d("timeTesting", "onBindViewHolder: dest " + finalDestinationReachTime);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String day = Day_Of_Week.getDayOfWeek(startDayCount);
            if (!runningDays.equalsIgnoreCase("daily") && !runningDays.toLowerCase().contains(day.toLowerCase())) {
                holder.itemView.setBackgroundColor(Color.GRAY);
                holder.trainScheduleRunningDays.setTextColor(Color.parseColor("#54D12E"));
                holder.trainScheduleJourneyDuration.setTextColor(ContextCompat.getColor(context, R.color.card_text_primary));
                holder.otherWarning.setText("Train is not running Today");
                holder.otherWarning.setTextColor(Color.RED);
                holder.otherWarning.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setBackgroundResource(R.color.card_background);
                holder.trainScheduleRunningDays.setTextColor(Color.parseColor("#54D12E"));
                holder.trainScheduleJourneyDuration.setTextColor(ContextCompat.getColor(context, R.color.card_text_primary));
                holder.otherWarning.setVisibility(View.GONE);
            }
        }


        String[] splitArrival = arrivalTime.split(":", 2);
        String[] splitDepart = finalDestinationReachTime.split(":", 2);
        Journey_Time_Finder finder = new Journey_Time_Finder();
        Duration durationResult = finder.getJourneyTime(Integer.parseInt(splitArrival[0]), Integer.parseInt(splitArrival[1]), startDayCount, Integer.parseInt(splitDepart[0]), Integer.parseInt(splitDepart[1]), endDayCount);
        String journeyDuration = durationResult.toHours() + " Hours " + durationResult.toMinutes() % 60 + " Minutes";

        arrivalTime = Time_Converter.giveMe_HH_MM(arrivalTime);
        finalDestinationReachTime = Time_Converter.giveMe_HH_MM(finalDestinationReachTime);


        String trainNumber = arrScheduleList.get(position).getTrainNumber();
        String trainName = arrScheduleList.get(position).getTrainName();


        holder.trainScheduleTrainNumber.setText(trainNumber);
        holder.trainScheduleTrainName.setText(trainName);
        holder.trainScheduleArrivalTime.setText(arrivalTime);
        holder.trainScheduleJourneyDuration.setText(journeyDuration);
        holder.trainScheduleFinalDestinationReachTime.setText(finalDestinationReachTime);
        holder.trainScheduleRunningDays.setText(runningDays);

        int finalStartDayCount = startDayCount;
        holder.itemView.setOnClickListener(v -> {
            String trainStartDate = null;
            if (finalStartDayCount > 1) {
                trainStartDate = Train_Journey_Date_Selector.getDefaultDate(finalStartDayCount);
            }
            String trNumber = holder.trainScheduleTrainNumber.getText().toString();
            String trName = holder.trainScheduleTrainName.getText().toString();
            Intent trainTracking = new Intent(context.getApplicationContext(), Train_Tracking.class);
            trainTracking.putExtra("trainNumber", trNumber);
            trainTracking.putExtra("trainName", trName);
            trainTracking.putExtra("fromStationCode", fromStationCode);
            trainTracking.putExtra("toStationCode", toStationCode);
            trainTracking.putExtra("trainStartDate", trainStartDate);
            trainTracking.putExtra("dayCount", finalStartDayCount);

            new Thread(() -> {
                //save user train in history for future access
                User_Routes_History_DB_Helper helper = new User_Routes_History_DB_Helper(context);
                helper.addHistoryInDB(trNumber, trName, fromStationCode, toStationCode, finalStartDayCount);
                helper.close();
            }).start();

            context.startActivity(trainTracking);
        });
        holder.itemView.setOnLongClickListener(v -> {

            Train_Schedule_Structure myTrainData = arrScheduleList.get(position);
            ShowTrainInfoDialog.showExtraTrainInfo(null, myTrainData, context);
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return arrScheduleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView trainScheduleTrainNumber, trainScheduleArrivalTime, trainScheduleJourneyDuration;
        TextView trainScheduleFinalDestinationReachTime, trainScheduleTrainName, trainScheduleRunningDays;
        TextView otherWarning;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            trainScheduleTrainNumber = itemView.findViewById(R.id.trainScheduleTrainNumber);
            trainScheduleArrivalTime = itemView.findViewById(R.id.trainScheduleArrivalTime);
            trainScheduleJourneyDuration = itemView.findViewById(R.id.trainScheduleJourneyDuration);
            trainScheduleFinalDestinationReachTime = itemView.findViewById(R.id.trainScheduleFinalDestinationReachTime);
            trainScheduleTrainName = itemView.findViewById(R.id.trainScheduleTrainName);
            trainScheduleRunningDays = itemView.findViewById(R.id.trainScheduleRunningDays);
            otherWarning = itemView.findViewById(R.id.otherWarning);
        }

    }

}
