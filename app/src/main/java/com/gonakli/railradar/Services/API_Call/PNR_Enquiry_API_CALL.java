package com.gonakli.railradar.Services.API_Call;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gonakli.railradar.Structure_Class.Pnr_Api_Response_Structure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PNR_Enquiry_API_CALL extends Service {
    String pnrNumber;
    private static final String API_URL = "http://10.182.234.238:5015/pnr-enquiry";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PNR_Service", "call_pnr_api: come in onStartCommand");

        if (intent.hasExtra("pnrNumber")) {
            pnrNumber = intent.getStringExtra("pnrNumber");
            new Thread(this::call_pnr_api).start();
        }


        return super.onStartCommand(intent, flags, startId);

    }

    private void call_pnr_api() {
        Log.d("PNR_Service", "call_pnr_api: in call_pnr_api()");

        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            String query = String.format("{\"pnrNumber\":\"%s\"}", pnrNumber);
            Log.d("PNR_Service", "call_pnr_api: " + query);

            OutputStream os = conn.getOutputStream();
            os.write(query.getBytes());
            os.flush();
            os.close();

            Log.d("PNR_Service", "call_pnr_api: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                Pnr_Api_Response_Structure resData = PnrHandler(conn);
                Log.d("PNR_Service", "call_pnr_api: response " + resData.getErrorMessage());
            }


        } catch (Exception e) {
            Log.d("PNR_Service", "call_pnr_api: error occurred" + e);
        }


    }

    private Pnr_Api_Response_Structure PnrHandler(HttpURLConnection conn) {
        Pnr_Api_Response_Structure resStruct = null;
        try {
            StringBuilder data = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
            JSONObject jsonObject = new JSONObject(data.toString());

            if (jsonObject.has("errorMessage")) {
                String errorMessage = jsonObject.optString("errorMessage");
                resStruct = new Pnr_Api_Response_Structure(false,errorMessage);

            } else if (jsonObject.has("pnrNumber")) {
                
            }

            return resStruct;

        } catch (Exception e) {
            Log.d("PNR_Service", "call_pnr_api: error occurred in pnrHandler" + e);
         return null;
        }
    }
}
