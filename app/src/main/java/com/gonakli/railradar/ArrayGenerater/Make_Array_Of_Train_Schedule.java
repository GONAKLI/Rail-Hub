package com.gonakli.railradar.ArrayGenerater;

import android.content.Context;
import android.util.Log;

import com.gonakli.railradar.R;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Make_Array_Of_Train_Schedule {
    ArrayList<Train_Schedule_Structure> arrTrainSchedule = new ArrayList<>();

    public ArrayList<Train_Schedule_Structure> addTrainSchedule(Context context){

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.arr_train_schedule);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
    if(jsonObject == null){
        continue;
    }

                String trainNumber = jsonObject.optString("trainNumber");
                String trainName = jsonObject.optString("trainName");
                String stationFrom = jsonObject.optString("stationFrom");
                String stationTo = jsonObject.optString("stationTo");
                String trainRunsOnMon = jsonObject.optString("trainRunsOnMon");
                String trainRunsOnTue = jsonObject.optString("trainRunsOnTue");
                String trainRunsOnWed = jsonObject.optString("trainRunsOnWed");
                String trainRunsOnThu = jsonObject.optString("trainRunsOnThu");
                String trainRunsOnFri = jsonObject.optString("trainRunsOnFri");
                String trainRunsOnSat = jsonObject.optString("trainRunsOnSat");
                String trainRunsOnSun = jsonObject.optString("trainRunsOnSun");
                String duration = jsonObject.optString("duration");
                ArrayList<Train_Schedule_Station_Structure> stationList = new ArrayList<>();

                JSONArray arrStationList = jsonObject.getJSONArray("stationList");
                for (int j = 0; j < arrStationList.length(); j++) {
                    JSONObject stationJSONObj = arrStationList.optJSONObject(j);

                    if(stationJSONObj == null){
                        continue;
                    }

                    String stationCode = stationJSONObj.optString("stationCode");
                    String stationName = stationJSONObj.optString("stationName");
                    String arrivalTime = stationJSONObj.optString("arrivalTime");
                    String departureTime = stationJSONObj.optString("departureTime");
                    String haltTime = stationJSONObj.optString("haltTime");
                    String distance = stationJSONObj.optString("distance");
                    String dayCount = stationJSONObj.optString("dayCount");
                    String stnSerialNumber = stationJSONObj.optString("stnSerialNumber");
                    String stnLat = stationJSONObj.optString("lat");
                    String stnLng = stationJSONObj.optString("lng");

                    stationList.add(new Train_Schedule_Station_Structure(
                            stationCode, stationName, arrivalTime, departureTime, haltTime, distance, dayCount, stnSerialNumber,
                            stnLat,stnLng));

                }
                arrTrainSchedule.add(
                        new Train_Schedule_Structure(trainNumber, trainName, stationFrom, stationTo,
                                trainRunsOnMon, trainRunsOnTue, trainRunsOnWed, trainRunsOnThu,
                                trainRunsOnFri, trainRunsOnSat, trainRunsOnSun, duration, stationList)
                );


            }

            return arrTrainSchedule;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
