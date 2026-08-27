package com.gonakli.railradar.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gonakli.railradar.DB_WORK.User_Routes_History_DB_Helper;
import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;
import com.gonakli.railradar.TrainTracking.Train_Tracking;
import com.gonakli.railradar.Utility.Current_Day_Finder;
import com.gonakli.railradar.Utility.Journey_Time_Finder;
import com.gonakli.railradar.Utility.Time_Converter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        String runningDays = "";
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
        String runOnMon = arrScheduleList.get(position).getTrainRunsOnMon();
        String runOnTue = arrScheduleList.get(position).getTrainRunsOnTue();
        String runOnWed = arrScheduleList.get(position).getTrainRunsOnWed();
        String runOnThu = arrScheduleList.get(position).getTrainRunsOnThu();
        String runOnFri = arrScheduleList.get(position).getTrainRunsOnFri();
        String runOnSat = arrScheduleList.get(position).getTrainRunsOnSat();
        String runOnSun = arrScheduleList.get(position).getTrainRunsOnSun();

        if (
                runOnMon.equalsIgnoreCase("Y") &&
                        runOnTue.equalsIgnoreCase("Y") &&
                        runOnWed.equalsIgnoreCase("Y") &&
                        runOnThu.equalsIgnoreCase("Y") &&
                        runOnFri.equalsIgnoreCase("Y") &&
                        runOnSat.equalsIgnoreCase("Y") &&
                        runOnSun.equalsIgnoreCase("Y")
        ) {
            runningDays = "Daily";
        } else {
            StringBuilder runningDaysBuilder = new StringBuilder();
            if (runOnMon.equalsIgnoreCase("Y")) runningDaysBuilder.append("Mon ");
            if (runOnTue.equalsIgnoreCase("Y")) runningDaysBuilder.append("Tue ");
            if (runOnWed.equalsIgnoreCase("Y")) runningDaysBuilder.append("Wed ");
            if (runOnThu.equalsIgnoreCase("Y")) runningDaysBuilder.append("Thu ");
            if (runOnFri.equalsIgnoreCase("Y")) runningDaysBuilder.append("Fri ");
            if (runOnSat.equalsIgnoreCase("Y")) runningDaysBuilder.append("Sat ");
            if (runOnSun.equalsIgnoreCase("Y")) runningDaysBuilder.append("Sun");
            runningDays = runningDaysBuilder.toString().trim();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate date = LocalDate.now();
            String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            if (!runningDays.equalsIgnoreCase("daily") && !runningDays.toLowerCase().contains(day.toLowerCase())) {
                holder.itemView.setBackgroundColor(Color.GRAY);
                holder.trainScheduleRunningDays.setTextColor(Color.parseColor("#54D12E"));
                holder.trainScheduleJourneyDuration.setTextColor(Color.parseColor("#FAA18F"));
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

        holder.itemView.setOnClickListener(v -> {
            String trNumber = holder.trainScheduleTrainNumber.getText().toString();
            String trName = holder.trainScheduleTrainName.getText().toString();
            Intent trainTracking = new Intent(context.getApplicationContext(), Train_Tracking.class);
            trainTracking.putExtra("trainNumber", trNumber);
            trainTracking.putExtra("trainName", trName);
            trainTracking.putExtra("fromStationCode", fromStationCode);
            trainTracking.putExtra("toStationCode", toStationCode);

            new Thread(() -> {
                //save user train inhistory for future access
                User_Routes_History_DB_Helper helper = new User_Routes_History_DB_Helper(context);
                helper.addHistoryInDB(trNumber, trName, fromStationCode, toStationCode);
                helper.close();
            }).start();

            context.startActivity(trainTracking);
        });
        final String serviceDays = runningDays;
        holder.itemView.setOnLongClickListener(v -> {
            TextView infoTrainName, infoTrainNumber, infoTrainService, infoTrainJourneyTime;
            TextView infoTrainStops, infoTrainTotalDistance, infoTrainStartingPoint, infoTrainEndingPoint;
            Button btnClose;
            Log.d("testCrashing", "onBindViewHolder: line 201");

            BottomSheetDialog dialog = new BottomSheetDialog(context);
            dialog.setContentView(R.layout.user_route_train_info_bottom_sheet);

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
            String formattedJourneyTime = String.format("%s Hours %s Minutes", journeyTime[0], journeyTime[1]);

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
