package com.gonakli.railHub.Utility.DateAndTimeRelated;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Time_Tense {

    public static int check_Train_Time_Tense(String scheduleTime, String actualTime){
        if(scheduleTime.contains("pm") && actualTime.contains("am")){
            return 1;
        } else if (scheduleTime.contains("am") && actualTime.contains("pm")) {
            return 1;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        LocalTime scheduleT = LocalTime.parse(scheduleTime,dateTimeFormatter);
        LocalTime actualT = LocalTime.parse(actualTime,dateTimeFormatter);

        if(actualT.isAfter(scheduleT)){
            return 1;
        } else if (actualT.isBefore(scheduleT)) {
            return -1;
        } else{
            return 0;
        }
    }
}
