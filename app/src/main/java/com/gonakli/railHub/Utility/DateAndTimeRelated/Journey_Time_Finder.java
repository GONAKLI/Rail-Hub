package com.gonakli.railHub.Utility.DateAndTimeRelated;

import android.util.Log;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class Journey_Time_Finder {
    int currentYear = Current_Day_Finder.getYear();
    int currentMonth = Current_Day_Finder.getMonth();
    int currentDate = Current_Day_Finder.getDate();

    public Duration getJourneyTime(int startHour, int startMinutes, int startDayCount,
                                   int endHour, int endMinutes, int endDayCount) {
        // Start time = current date + startDayCount offset
        try{
            LocalDateTime baseDate = LocalDateTime.of(currentYear,currentMonth,currentDate,0,0);

            LocalDateTime journeyStartAt = baseDate.plusDays(startDayCount - 1).plusHours(startHour).plusMinutes(startMinutes);
            // End time = current date + endDayCount offset
            LocalDateTime journeyEndAt = baseDate.plusDays(endDayCount - 1).plusHours(endHour).plusMinutes(endMinutes);
            // Duration calculation
            return Duration.between(journeyStartAt, journeyEndAt);
        }catch (DateTimeException e){
            Log.d("dateException", "getJourneyTime: exception occurred " + e.getMessage());
            Log.d("dateException", "getJourneyTime: exception occurred " + startDayCount+ " " + endDayCount);

        }
        return null;
    }


    public int getDaysInMonth(int year, int month){
        try{
            YearMonth yearMonth = YearMonth.of(year,month);
            return yearMonth.lengthOfMonth();
        } catch (Exception e) {
            Log.d("dateException", "getDaysInMonth: exception occured " + e.getMessage());
        }
        return 0;
    }
}
