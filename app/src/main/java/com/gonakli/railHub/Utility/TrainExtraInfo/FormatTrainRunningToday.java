package com.gonakli.railHub.Utility.TrainExtraInfo;

import android.util.Log;

public class FormatTrainRunningToday {
    private static final String[] weekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    public static String runningTodayFormatter(String baseRunningDays, int startDayCount) {
        if (baseRunningDays.equalsIgnoreCase("daily") || startDayCount == 1) return baseRunningDays;
        int startCount = startDayCount - 1;

        String[] splitBaseDays = baseRunningDays.split(" ");
        StringBuilder runningDayBuilder = new StringBuilder();
        for (String base : splitBaseDays) {
            for (int i = 0; i < weekDays.length; i++) {
                if (weekDays[i].equalsIgnoreCase(base)) {
                    String day = weekDays[(i + startCount) % 7];
                    runningDayBuilder.append(day).append(" ");
                    break;
                }
            }
        }
        return arrangeDaysInValidOrder(runningDayBuilder.toString());
    }

    private static String arrangeDaysInValidOrder(String runningDays){
        StringBuilder arrangeDaysBuilder = new StringBuilder();
        for (String weekDay : weekDays) {
                if(runningDays.contains(weekDay)){
                    arrangeDaysBuilder.append(weekDay).append(" ");
                }
        }
        return arrangeDaysBuilder.toString().trim();
    }
}
