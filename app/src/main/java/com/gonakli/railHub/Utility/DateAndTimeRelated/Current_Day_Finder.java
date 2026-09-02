package com.gonakli.railHub.Utility.DateAndTimeRelated;

import java.time.LocalDate;

public class Current_Day_Finder {
    public static LocalDate date = LocalDate.now();
    public static int getYear(){
       return date.getYear();
    }
    public static int getMonth(){
        return date.getMonthValue();
    }
    public  static int getDate(){
        return  date.getDayOfMonth();
    }
}
