package com.gonakli.railradar.ArrayGenerater;

import android.content.Context;

import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.gonakli.railradar.Structure_Class.Train_List_Structure;
import com.gonakli.railradar.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Make_Array_Of_Trains_And_Stations_List {
    ArrayList<Station_List_Structure> arrStationList = new ArrayList<>();
    ArrayList<Train_List_Structure> arrTrainList = new ArrayList<>();
    public ArrayList<Station_List_Structure> addStations(Context context) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.arr_station_list);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String jsonString = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String code = jsonObject.getString("code");
                String name = jsonObject.getString("name");
                arrStationList.add(new Station_List_Structure(code, name));

            }
        } catch (Exception e) {
             e.printStackTrace();
        }

        return arrStationList;
    }

    public ArrayList<Train_List_Structure> addTrains(Context context){
        try{
            InputStream inputStream = context.getResources().openRawResource(R.raw.arr_train_list);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String trainList = new String(buffer, "UTF-8");
            JSONArray arrTrains = new JSONArray(trainList);
            for (int i=0; i<arrTrains.length(); i++){
                String trainData = arrTrains.getString(i);
               String[] splitTrainData =  trainData.split("-", 2);
               String trainNumber = splitTrainData[0].trim();
               String trainName = splitTrainData[1].trim();
                arrTrainList.add(new Train_List_Structure(trainNumber, trainName));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return arrTrainList;

    }



}
