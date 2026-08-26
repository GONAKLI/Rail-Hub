package com.gonakli.railradar.Utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Time_Converter {
    public static String giveMe_HH_MM(String time24) {
        String timeIn12Hours = null;
        try {
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = sdf24.parse(time24);
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            timeIn12Hours = sdf12.format(date);
        } catch (ParseException e) {
            timeIn12Hours = time24;
            Log.d("errorIn12", "giveMe_HH_MM: error in time conversion " + e.getMessage());
        }
        return timeIn12Hours;


    }
}
