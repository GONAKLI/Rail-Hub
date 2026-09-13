package com.gonakli.railHub.Utility.DateAndTimeRelated;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Day_Of_Week {
    public static String getDayOfWeek(int day){
        int finalDay = day -1;
        LocalDate date = LocalDate.now().minusDays(finalDay);
        return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }
}
