package com.gonakli.railHub.Services.API_Call;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gonakli.railHub.Structure_Class.PassengerList_Structure;
import com.gonakli.railHub.Structure_Class.Pnr_Api_Response_Structure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PNR_Enquiry_API_CALL extends Service {
    String pnrNumber;
    private static String API_URL;
    public static String PNR_RESPONSE = "PNR_RESPONSE_ACTION";
    public static String INTERNET_ISSUE = "PNR_INTERNET_ISSUE";
    public static  String INTERNAL_APPLICATION_ERROR = "INTERNAL_APPLICATION_ERROR";
    //private static final String API_URL = "https://railhub.gonakli.com/pnr-enquiry";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PNR_Service", "call_pnr_api: come in onStartCommand");
        if(intent == null){
            stopSelf();
            return START_NOT_STICKY;
        }

        if (intent.hasExtra("pnrNumber")) {
            pnrNumber = intent.getStringExtra("pnrNumber");
            API_URL = String.format("https://www.irctc.co.in/eticketing/protected/mapps1/pnrenq/%s?pnrEnqType=E", pnrNumber);
            Log.d("PNR_Service", "onStartCommand: " + API_URL);
            new Thread(() ->{
                call_pnr_api(intent);
            }).start();
        }else{
            stopSelf();
        }


        return START_NOT_STICKY;

    }

    private void call_pnr_api(Intent intent) {
        Intent iPnrResponse = new Intent();
        Log.d("PNR_Service", "call_pnr_api: in call_pnr_api()");

        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Referer", "https://www.irctc.co.in/eticket/enquiry/pnr-enquiry");

            // 4. Baaki sabhi headers
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:153.0) Gecko/20100101 Firefox/153.0");
            conn.setRequestProperty("Accept", "application/json, text/plain, */*");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.0");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("greq", "1787074947751:c48d8deb-131f-4a00-bb83-b51335bd8b90");
            conn.setRequestProperty("bmirak", "webmb");
            conn.setRequestProperty("Content-Language", "en");
            conn.setRequestProperty("Alt-Used", "www.irctc.co.in");
            conn.setRequestProperty("Sec-Fetch-Dest", "empty");
            conn.setRequestProperty("Sec-Fetch-Mode", "cors");
            conn.setRequestProperty("Sec-Fetch-Site", "same-origin");
            conn.setRequestProperty("Priority", "u=0");
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setRequestProperty("Accept", "application/json");
           // conn.setDoOutput(true);
//            String query = String.format("{\"pnrNumber\":\"%s\"}", pnrNumber);
//            Log.d("PNR_Service", "call_pnr_api: " + query);
//
//            OutputStream os = conn.getOutputStream();
//            os.write(query.getBytes());
//            os.flush();
//            os.close();

            Log.d("PNR_Service", "call_pnr_api: " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                Pnr_Api_Response_Structure resData = PnrHandler(conn);

                Log.d("pnrWork", "call_pnr_api: " + resData.isSuccess());

                Log.d("pnrWork", "call_pnr_api: " + resData.getTrainName());
                // Log.d("pnrWork", "call_pnr_api: " + resData.getErrorMessage());
                iPnrResponse.setAction(PNR_RESPONSE);
                iPnrResponse.setPackage(getPackageName());
                iPnrResponse.putExtra("pnrResponse", resData);
                if(intent.hasExtra("isRefresh")){
                    boolean isRefresh = intent.getBooleanExtra("isRefresh", false);
                    if(isRefresh){
                        iPnrResponse.putExtra("isRefresh", true);
                    }
                }
                sendBroadcast(iPnrResponse);
                Log.d("pnrWork", "call_pnr_api: broadcast sent");

            }
        } catch (UnknownHostException e) {
            iPnrResponse.setAction(INTERNET_ISSUE);
            iPnrResponse.setPackage(getPackageName());
            sendBroadcast(iPnrResponse);
        } catch (IOException e){
            iPnrResponse.setAction(INTERNAL_APPLICATION_ERROR);
            iPnrResponse.setPackage(getPackageName());
            sendBroadcast(iPnrResponse);
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
                String dateOfJourney, trainStartDate, trainNumber, trainName,sourceStation, pnrNumber;
                String destinationStation, reservationUpto,boardingPoint,journeyClass,numberOfpassenger;
                String chartStatus,bookingFare,quota,ticketFare;
                String bookingDate, ticketType;
                ArrayList<Object> arrInformationMessage = new ArrayList<>();
                ArrayList<PassengerList_Structure> arrPassengerList = new ArrayList<>();

                pnrNumber = jsonObject.optString("pnrNumber", null);
                dateOfJourney = jsonObject.optString("dateOfJourney", null);
                trainStartDate = jsonObject.optString("trainStartDate", null);
                trainNumber = jsonObject.optString("trainNumber", null);
                trainName = jsonObject.optString("trainName", null);
                sourceStation = jsonObject.optString("sourceStation", null);
                destinationStation = jsonObject.optString("destinationStation", null);
                reservationUpto = jsonObject.optString("reservationUpto", null);
                boardingPoint = jsonObject.optString("boardingPoint", null);
                journeyClass = jsonObject.optString("journeyClass", null);
                numberOfpassenger = jsonObject.optString("numberOfpassenger", null);
                chartStatus = jsonObject.optString("chartStatus", null);
                bookingFare = jsonObject.optString("bookingFare", null);
                quota = jsonObject.optString("quota", null);
                ticketFare = jsonObject.optString("ticketFare", null);
                bookingDate = jsonObject.optString("bookingDate");
                ticketType = jsonObject.optString("ticketType");


                JSONArray arrPassList = jsonObject.optJSONArray("passengerList");
                for(int j =0; j < arrPassList.length(); j++){
                    // passengerList
                    String currentStatus,currentBerthNo,psgnwlType;
                    String passengerSerialNumber, passengerAge,passengerBerthChoice,passengerNationality;
                    String bookingStatus, bookingCoachId,bookingBerthNo,bookingBerthCode,bookingStatusDetails;
                    String currentStatusIndex,currentStatusDetails;
                    JSONObject objPassList = arrPassList.optJSONObject(j);
                    currentStatus = objPassList.optString("currentStatus");
                    currentBerthNo = objPassList.optString("currentBerthNo");
                    psgnwlType = objPassList.optString("psgnwlType");
                    passengerSerialNumber = objPassList.optString("passengerSerialNumber");
                    passengerAge = objPassList.optString("passengerAge");
                    passengerBerthChoice = objPassList.optString("passengerBerthChoice");
                    passengerNationality = objPassList.optString("passengerNationality");
                    bookingStatus = objPassList.optString("bookingStatus");
                    bookingCoachId = objPassList.optString("bookingCoachId");
                    bookingBerthNo = objPassList.optString("bookingBerthNo");
                    bookingBerthCode = objPassList.optString("bookingBerthCode");
                    bookingStatusDetails = objPassList.optString("bookingStatusDetails");
                    currentStatusIndex =  objPassList.optString("currentStatusIndex");
                    currentStatusDetails =  objPassList.optString("currentStatusDetails");


                    arrPassengerList.add(new PassengerList_Structure(passengerSerialNumber, passengerAge,
                            passengerBerthChoice,passengerNationality,bookingStatus,bookingCoachId,bookingBerthNo,bookingBerthCode,
                            bookingStatusDetails,currentStatus,currentBerthNo,psgnwlType, currentStatusIndex,currentStatusDetails));
                }


                JSONArray infoJson = jsonObject.optJSONArray("informationMessage");
                for(int i=0; i<infoJson.length(); i++){
                    if(!infoJson.isNull(i)){
                        Object value = infoJson.opt(i);
                        arrInformationMessage.add(value);
                    }

                }

                resStruct = new Pnr_Api_Response_Structure(true,pnrNumber, dateOfJourney,
                        trainStartDate, trainNumber, trainName, sourceStation, destinationStation,
                        reservationUpto, boardingPoint, journeyClass,
                        numberOfpassenger, chartStatus, bookingFare, quota,
                        arrInformationMessage,arrPassengerList, ticketFare,bookingDate,ticketType);
            }

            return resStruct;

        } catch (Exception e) {
            Log.d("PNR_Service", "call_pnr_api: error occurred in pnrHandler" + e);
         return null;
        }
    }
}
