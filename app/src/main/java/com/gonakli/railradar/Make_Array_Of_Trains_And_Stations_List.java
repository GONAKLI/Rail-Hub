package com.gonakli.railradar;

import android.content.Context;

import com.gonakli.railradar.DB_MODAL.Station_List_Modal_Class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Make_Array_Of_Trains_And_Stations_List {
    ArrayList<Station_List_Modal_Class> arrStationList = new ArrayList<>();
    public ArrayList<Station_List_Modal_Class> addStations(Context context) {
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
                arrStationList.add(new Station_List_Modal_Class(code, name));

            }
        } catch (Exception e) {
             e.printStackTrace();
        }

        return arrStationList;
    }



}
