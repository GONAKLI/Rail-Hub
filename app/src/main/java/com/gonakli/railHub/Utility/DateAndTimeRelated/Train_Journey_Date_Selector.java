package com.gonakli.railHub.Utility.DateAndTimeRelated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Train_Journey_Date_Selector {
    public static LocalDate date = LocalDate.now();

    public static String getDefaultDate(int days) {
        int finalDays = days - 1;
        LocalDate newDate = date.minusDays(finalDays);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());
        return newDate.format(dateFormat);
    }
}
