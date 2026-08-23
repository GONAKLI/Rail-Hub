package com.gonakli.railradar.API_Limit;

import java.util.HashMap;
import java.util.Map;

public class Train_Finder_Api_Limit {
    private static Map<String, Long> arrTrainRecords = new HashMap<>();
    public static boolean canCallFindTrainAPI(String trainNumber){
        long currentTime = System.currentTimeMillis();
        if(!arrTrainRecords.containsKey(trainNumber)){
            arrTrainRecords.put(trainNumber, currentTime);
            return true;
        }else{
            long lastCallTime = arrTrainRecords.get(trainNumber);
            if(currentTime - lastCallTime > (15000)){
                arrTrainRecords.put(trainNumber,currentTime);
                return true;
            }
            return false;
        }

    }
}
