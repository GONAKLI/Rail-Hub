package com.gonakli.railradar.Services.API_Call;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Train_Tracking_API_Call extends Service{

    private static final String API_URL = "http://10.236.89.225:5015/find-my-train";
    String trainNumber;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("apiTest", "onStartCommand: enter here");
        if(intent != null && intent.hasExtra("trainNumber")){
            trainNumber = intent.getStringExtra("trainNumber");
            new Thread(this::fetch_Train_Location).start();

        }
        return super.onStartCommand(intent, flags, startId);
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
            System.out.println(sb);
            System.out.println(statusCode);

        }catch (Exception e){

            Log.d("apiTest", "onStartCommand: catch block: " + e);
            System.out.println("error occurred");
        }

    }
}
