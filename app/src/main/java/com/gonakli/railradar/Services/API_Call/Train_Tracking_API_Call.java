package com.gonakli.railradar.Services.API_Call;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gonakli.railradar.Structure_Class.API_Response_Train_Tracking;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Train_Tracking_API_Call extends Service{

    private static final String API_URL = "http://10.232.190.180:5015/find-my-train";
//     private static final String API_URL = "https://railhub.gonakli.com/find-my-train";
    public static String API_TRAIN_DATA = "API_TRAIN_DATA";
    String trainNumber;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("apiTest", "onStartCommand: enter here");
        if(intent == null){
            stopSelf();
            return START_NOT_STICKY;
        }
        if(intent != null && intent.hasExtra("trainNumber")){
            trainNumber = intent.getStringExtra("trainNumber");
            new Thread(this::fetch_Train_Location).start();
        }else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private void fetch_Train_Location(){
        Log.d("apiTest", "onStartCommand: enter in fetch method");
        try{
            URL url = new URL(API_URL);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            String trNumJSON = String.format("{\"trainNumber\" : \"%s\"}", trainNumber);
            OutputStream os = conn.getOutputStream();
            os.write(trNumJSON.getBytes());
            os.flush();
            os.close();


            int statusCode = conn.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line.trim());
            }
            JSONObject trainTracking = new JSONObject(sb.toString());
            Log.d("trackmyAPI", "fetch_Train_Location: lat  " + trainTracking.optString("trainLat"));

            Log.d("trackmyAPI", "fetch_Train_Location: lng " + trainTracking.optString("trainLng"));
            String lat = trainTracking.optString("trainLat");
            String lng = trainTracking.optString("trainLng");
            String trainApiStatusMsg = trainTracking.optString("statusMsg");
            String dataLastUpdatedAt = trainTracking.optString("lastUpdatedAt", "");
            JSONArray jsonArray = trainTracking.optJSONArray("stationData");

            ArrayList<API_Response_Train_Tracking> arrTrainApi = new ArrayList<>();
            for(int i=0; i< jsonArray.length(); i++){
                JSONObject objData = jsonArray.getJSONObject(i);
                String stationCode = objData.optString("stationCode", "");
                String platform = objData.optString("platform", "");
                String actualArrival = objData.optString("actualArrival", "");
                String actualDeparture = objData.optString("actualDeparture", "");
                arrTrainApi.add(new API_Response_Train_Tracking(stationCode,platform, actualArrival,actualDeparture));
            }

            Intent iApiData = new Intent(API_TRAIN_DATA);
            iApiData.putExtra("trainLat", Double.parseDouble(lat));
            iApiData.putExtra("trainLng", Double.parseDouble(lng));
            iApiData.putExtra("trainApiStatusMsg", trainApiStatusMsg);
            iApiData.putExtra("dataLastUpdatedAt", dataLastUpdatedAt);
            iApiData.putExtra("stationData", arrTrainApi);

            iApiData.setPackage(getPackageName());
            sendBroadcast(iApiData);

        }catch (Exception e){

            Log.d("apiTest", "onStartCommand: catch block: " + e);
            System.out.println("error occurred");
        }

    }
}
