package com.gonakli.railradar.Utility;

import java.time.LocalDate;
import java.util.Date;

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
