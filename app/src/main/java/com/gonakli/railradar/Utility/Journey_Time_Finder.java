package com.gonakli.railradar.Utility;

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
        LocalDateTime journeyStartAt = LocalDateTime.of(currentYear, currentMonth, currentDate + (startDayCount - 1), startHour, startMinutes);

        // End time = current date + endDayCount offset
        LocalDateTime journeyEndAt = LocalDateTime.of(currentYear, currentMonth, currentDate + (endDayCount - 1), endHour, endMinutes);

        // Duration calculation
        return Duration.between(journeyStartAt, journeyEndAt);
    }


    public int getDaysInMonth(int year, int month){
        YearMonth yearMonth = YearMonth.of(year,month);
        return yearMonth.lengthOfMonth();
    }
}
