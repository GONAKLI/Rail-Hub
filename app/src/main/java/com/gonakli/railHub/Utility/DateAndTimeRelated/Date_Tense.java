package com.gonakli.railHub.Utility.DateAndTimeRelated;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Date_Tense {
    public static String isDate_Past_Present_Future(String journeyDate) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        LocalDate userJourneyDate = LocalDate.parse(journeyDate,dateTimeFormatter);
        if (userJourneyDate.isBefore(today)){
            Log.d("pnrFormattedDate", "dateFormater: already passed" );
            return "past";
        } else if (userJourneyDate.isAfter(today)) {
            Log.d("pnrFormattedDate", "dateFormater: upcoming");
            return "future";
        }else{
            Log.d("pnrFormattedDate", "dateFormater: date is today" );
            return "present";
        }
    }
}
