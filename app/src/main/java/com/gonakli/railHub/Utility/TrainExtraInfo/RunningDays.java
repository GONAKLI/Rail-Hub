package com.gonakli.railHub.Utility.TrainExtraInfo;

import com.gonakli.railHub.Structure_Class.Train_Schedule_Structure;

public class RunningDays {
    public static String getTrainRunningDays( Train_Schedule_Structure trainSchedule ) {
        String runOnMon = trainSchedule.getTrainRunsOnMon();
        String runOnTue = trainSchedule.getTrainRunsOnTue();
        String runOnWed = trainSchedule.getTrainRunsOnWed();
        String runOnThu = trainSchedule.getTrainRunsOnThu();
        String runOnFri = trainSchedule.getTrainRunsOnFri();
        String runOnSat = trainSchedule.getTrainRunsOnSat();
        String runOnSun = trainSchedule.getTrainRunsOnSun();

        if (
                runOnMon.equalsIgnoreCase("Y") &&
                        runOnTue.equalsIgnoreCase("Y") &&
                        runOnWed.equalsIgnoreCase("Y") &&
                        runOnThu.equalsIgnoreCase("Y") &&
                        runOnFri.equalsIgnoreCase("Y") &&
                        runOnSat.equalsIgnoreCase("Y") &&
                        runOnSun.equalsIgnoreCase("Y")
        ) {
            return "Daily";
        } else {
            StringBuilder runningDaysBuilder = new StringBuilder();
            if (runOnMon.equalsIgnoreCase("Y")) runningDaysBuilder.append("Mon ");
            if (runOnTue.equalsIgnoreCase("Y")) runningDaysBuilder.append("Tue ");
            if (runOnWed.equalsIgnoreCase("Y")) runningDaysBuilder.append("Wed ");
            if (runOnThu.equalsIgnoreCase("Y")) runningDaysBuilder.append("Thu ");
            if (runOnFri.equalsIgnoreCase("Y")) runningDaysBuilder.append("Fri ");
            if (runOnSat.equalsIgnoreCase("Y")) runningDaysBuilder.append("Sat ");
            if (runOnSun.equalsIgnoreCase("Y")) runningDaysBuilder.append("Sun");
            return runningDaysBuilder.toString().trim();
        }
    }
}
