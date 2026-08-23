package com.gonakli.railradar.API_Limit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pnr_Check_Api_Limit {
    private static Map<String,Long> arrPnrRecords = new HashMap<>();

    public static boolean canCallPnrAPI(String pnrNum){
        long currentTime = System.currentTimeMillis();
        if(!arrPnrRecords.containsKey(pnrNum)){
            arrPnrRecords.put(pnrNum, currentTime);
            return true;
        }else{
          long lastCallTime = arrPnrRecords.get(pnrNum);
            if(currentTime - lastCallTime > (1000 *60 * 20)){
                arrPnrRecords.put(pnrNum,currentTime);
                return true;
            }
            return false;
        }
    }
}
