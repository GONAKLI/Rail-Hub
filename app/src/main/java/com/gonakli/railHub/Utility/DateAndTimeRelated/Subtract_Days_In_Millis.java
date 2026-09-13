package com.gonakli.railHub.Utility.DateAndTimeRelated;

public class Subtract_Days_In_Millis {

    public static long getSubtractedDaysInMillis(long fromValue, int days){
        int finalDays = days - 1;
        long timeToSubtracted = finalDays *(24 * 60 * 60 * 1000);
        return fromValue - timeToSubtracted;
    }
}
