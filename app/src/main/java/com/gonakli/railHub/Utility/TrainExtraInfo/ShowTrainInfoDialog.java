package com.gonakli.railHub.Utility.TrainExtraInfo;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.gonakli.railHub.DB_WORK.Train_Schedule_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Structure;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ShowTrainInfoDialog {

        public static void showExtraTrainInfo(String trNum, Context context) {
            try (Train_Schedule_DB_Helper dbHelper = new Train_Schedule_DB_Helper(context)) {
                Train_Schedule_Structure myTrainData = dbHelper.getTrainDataByTrainNumber(trNum);
                Train_Schedule_Station_Structure sourceStation = myTrainData.getStationList().get(0);
                Train_Schedule_Station_Structure destinationStation = myTrainData.getStationList().get(myTrainData.getStationList().size() - 1);
                int totalStops = myTrainData.getStationList().size();
                String runningDays = RunningDays.getTrainRunningDays(myTrainData);
                TextView infoTrainName, infoTrainNumber, infoTrainService, infoTrainJourneyTime;
                TextView infoTrainStops, infoTrainTotalDistance, infoTrainStartingPoint, infoTrainEndingPoint;
                Button btnClose;

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

                String[] journeyTime = myTrainData.getDuration().split(":", 2);
                String formattedJourneyTime = null;
                if (journeyTime[0].equalsIgnoreCase("00")) {
                    formattedJourneyTime = String.format("%s Minutes", journeyTime[1]);

                } else {
                    formattedJourneyTime = String.format("%s Hours %s Minutes", journeyTime[0], journeyTime[1]);

                }

                infoTrainName.setText(myTrainData.getTrainName());
                infoTrainNumber.setText(myTrainData.getTrainNumber());
                infoTrainService.setText(runningDays);
                infoTrainJourneyTime.setText(formattedJourneyTime);
                infoTrainStops.setText(String.valueOf(totalStops));
                infoTrainTotalDistance.setText(String.valueOf(destinationStation.getDistance() + " Km"));
                infoTrainStartingPoint.setText(sourceStation.getStationName());
                infoTrainEndingPoint.setText(destinationStation.getStationName());


                dialog.show();
                btnClose.setOnClickListener(x -> {
                    dialog.dismiss();
                });
            }
    }


    public static void showExtraTrainInfoWithoutDB(Train_Schedule_Structure myTrainData, Context context) {
            Train_Schedule_Station_Structure sourceStation = myTrainData.getStationList().get(0);
            Train_Schedule_Station_Structure destinationStation = myTrainData.getStationList().get(myTrainData.getStationList().size() - 1);
            int totalStops = myTrainData.getStationList().size();
            String runningDays = RunningDays.getTrainRunningDays(myTrainData);
            TextView infoTrainName, infoTrainNumber, infoTrainService, infoTrainJourneyTime;
            TextView infoTrainStops, infoTrainTotalDistance, infoTrainStartingPoint, infoTrainEndingPoint;
            Button btnClose;

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

            String[] journeyTime = myTrainData.getDuration().split(":", 2);
            String formattedJourneyTime = null;
            if (journeyTime[0].equalsIgnoreCase("00")) {
                formattedJourneyTime = String.format("%s Minutes", journeyTime[1]);

            } else {
                formattedJourneyTime = String.format("%s Hours %s Minutes", journeyTime[0], journeyTime[1]);

            }

            infoTrainName.setText(myTrainData.getTrainName());
            infoTrainNumber.setText(myTrainData.getTrainNumber());
            infoTrainService.setText(runningDays);
            infoTrainJourneyTime.setText(formattedJourneyTime);
            infoTrainStops.setText(String.valueOf(totalStops));
            infoTrainTotalDistance.setText(String.valueOf(destinationStation.getDistance() + " Km"));
            infoTrainStartingPoint.setText(sourceStation.getStationName());
            infoTrainEndingPoint.setText(destinationStation.getStationName());


            dialog.show();
            btnClose.setOnClickListener(x -> {
                dialog.dismiss();
            });
        }
}
