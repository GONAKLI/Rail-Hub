package com.gonakli.railHub.DB_WORK;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

//import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Train_Schedule;
import com.gonakli.railHub.Structure_Class.Track_Polyline_Point_Structure;
import com.gonakli.railHub.Structure_Class.Train_List_Structure_New;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railHub.Structure_Class.Train_Schedule_Structure;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Train_Schedule_DB_Helper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "GONAKLI.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "TRAIN_SCHEDULE";

    private static final String COLUMN_trainNumber = "trainNumber";
    private static final String     COLUMN_trainName = "trainName";
    private static final String  COLUMN_stationFrom = "stationFrom";
    private static final String  COLUMN_stationTo = "stationTo";
    private static final String  COLUMN_trainRunsOnMon = "trainRunsOnMon";
    private static final String  COLUMN_trainRunsOnTue = "trainRunsOnTue";
    private static final String  COLUMN_trainRunsOnWed = "trainRunsOnWed";
    private static final String  COLUMN_trainRunsOnThu = "trainRunsOnThu";
    private static final String  COLUMN_trainRunsOnFri = "trainRunsOnFri";
    private static final String  COLUMN_trainRunsOnSat = "trainRunsOnSat";
    private static final String  COLUMN_trainRunsOnSun = "trainRunsOnSun";
    private static final String  COLUMN_duration = "duration";

    // now Station

    private static final String  STATION_TABLE_FOR_SCHEDULE ="STATION_SCHEDULE";

    private static final String  COLUMN_stationCode ="stationCode";
    private static final String  COLUMN_stationName = "stationName";
    private static final String  COLUMN_arrivalTime = "arrivalTime";
    private static final String  COLUMN_departureTime = "departureTime";
    private static final String  COLUMN_haltTime = "haltTime";
    private static final String  COLUMN_distance = "distance";
    private static final String  COLUMN_dayCount = "dayCount";
    private static final String  COLUMN_stnSerialNumber = "stnSerialNumber";
    private static final String  COLUMN_stnLat = "lat";
    private static final String  COLUMN_stnLng = "lng";

    // now PolyLine
    private static final String  POLYLINE_TABLE_NAME ="STATION_POLYLINE_TABLE";


    private final Context applicationContext;

    public Train_Schedule_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = context;
    }

    public ArrayList<Train_Schedule_Structure> getScheduleList() {
        ArrayList<Train_Schedule_Structure> arrTrainSchedule = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // 1. मुख्य TRAIN_SCHEDULE टेबल से सभी ट्रेनें पढ़ें
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null) {
            // while(cursor.moveToNext()) अपने आप पहली रो से शुरू करता है
            while (cursor.moveToNext()) {

                // Cursor से ट्रेन की डिटेल्स निकालें
                String trainNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainNumber));
                String trainName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainName));
                String stationFrom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationFrom));
                String stationTo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationTo));
                String trainRunsOnMon = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnMon));
                String trainRunsOnTue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnTue));
                String trainRunsOnWed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnWed));
                String trainRunsOnThu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnThu));
                String trainRunsOnFri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnFri));
                String trainRunsOnSat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnSat));
                String trainRunsOnSun = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnSun));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_duration));

                // 2. इस ट्रेन के सभी स्टेशनों की लिस्ट (Station Schedule) लाएँ
                ArrayList<Train_Schedule_Station_Structure> stationList = getStationsForTrain(db, trainNumber);

                // 3. Train_Schedule_Structure ऑब्जेक्ट बनाकर एरेलिस्ट में जोड़ें
                Train_Schedule_Structure trainObj = new Train_Schedule_Structure(
                        trainNumber, trainName, stationFrom, stationTo,
                        trainRunsOnMon, trainRunsOnTue, trainRunsOnWed, trainRunsOnThu,
                        trainRunsOnFri, trainRunsOnSat, trainRunsOnSun, duration,
                        stationList
                );

                arrTrainSchedule.add(trainObj);
            }
            cursor.close(); // Cursor बंद करें
        }

        return arrTrainSchedule;
    }

    /**
     * किसी ट्रेन नंबर के आधार पर उसके सभी स्टेशनों की लिस्ट निकालने का प्राइवेट हेल्पर मेथड
     */
    private ArrayList<Train_Schedule_Station_Structure> getStationsForTrain(SQLiteDatabase db, String trainNumber) {
        ArrayList<Train_Schedule_Station_Structure> stationList = new ArrayList<>();

        // stnSerialNumber के हिसाब से सॉर्ट (ASC) करके स्टेशनों का डेटा पढ़ें
        String query = "SELECT * FROM " + STATION_TABLE_FOR_SCHEDULE +
                " WHERE " + COLUMN_trainNumber + " = ?" +
                " ORDER BY CAST(" + COLUMN_stnSerialNumber + " AS INTEGER) ASC";

        Cursor stnCursor = db.rawQuery(query, new String[]{trainNumber});

        if (stnCursor != null) {
            while (stnCursor.moveToNext()) {
                String stationCode = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_stationCode));
                String stationName = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_stationName));
                String arrivalTime = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_arrivalTime));
                String departureTime = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_departureTime));
                String haltTime = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_haltTime));
                String distance = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_distance));
                String dayCount = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_dayCount));
                String stnSerialNumber = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_stnSerialNumber));
                String stnLat = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_stnLat));
                String stnLng = stnCursor.getString(stnCursor.getColumnIndexOrThrow(COLUMN_stnLng));

                Train_Schedule_Station_Structure stationObj = new Train_Schedule_Station_Structure(
                        stationCode, stationName, arrivalTime, departureTime,
                        haltTime, distance, dayCount, stnSerialNumber, stnLat, stnLng
                );

                stationList.add(stationObj);
            }
            stnCursor.close(); // Station Cursor बंद करें
        }

        return stationList;
    }


    /**
     * दो स्टेशनों के बीच चलने वाली सभी ट्रेनों की लिस्ट निकालने का मेथड
     * @param srcCode  - सोर्स स्टेशन कोड (उदा. "NDLS")
     * @param destCode - डेस्टिनेशन स्टेशन कोड (उदा. "CNB")
     * @return उन सभी ट्रेनों की लिस्ट जो इस रूट से होकर गुज़रती हैं
     */
    public ArrayList<Train_Schedule_Structure> getTrainsBetweenStations(String srcCode, String destCode) {
        ArrayList<Train_Schedule_Structure> trainList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL Query: s1 = Source Station, s2 = Destination Station
        // यह पक्का करता है कि दोनों स्टेशन उस ट्रेन के रूट में हों और Source पहले आए
        String query = "SELECT DISTINCT t.* FROM " + TABLE_NAME + " t " +
                "INNER JOIN " + STATION_TABLE_FOR_SCHEDULE + " s1 ON t." + COLUMN_trainNumber + " = s1." + COLUMN_trainNumber + " " +
                "INNER JOIN " + STATION_TABLE_FOR_SCHEDULE + " s2 ON t." + COLUMN_trainNumber + " = s2." + COLUMN_trainNumber + " " +
                "WHERE s1." + COLUMN_stationCode + " = ? " +
                "AND s2." + COLUMN_stationCode + " = ? " +
                "AND CAST(s1." + COLUMN_stnSerialNumber + " AS INTEGER) < CAST(s2." + COLUMN_stnSerialNumber + " AS INTEGER)";

        Cursor cursor = db.rawQuery(query, new String[]{srcCode.trim(), destCode.trim()});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String trainNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainNumber));
                String trainName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainName));
                String stationFrom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationFrom));
                String stationTo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationTo));
                String trainRunsOnMon = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnMon));
                String trainRunsOnTue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnTue));
                String trainRunsOnWed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnWed));
                String trainRunsOnThu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnThu));
                String trainRunsOnFri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnFri));
                String trainRunsOnSat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnSat));
                String trainRunsOnSun = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnSun));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_duration));

                // उस ट्रेन के सभी स्टेशनों की लिस्ट प्राप्त करें
                ArrayList<Train_Schedule_Station_Structure> stationList = getStationsForTrain(db, trainNumber);

                Train_Schedule_Structure trainObj = new Train_Schedule_Structure(
                        trainNumber, trainName, stationFrom, stationTo,
                        trainRunsOnMon, trainRunsOnTue, trainRunsOnWed, trainRunsOnThu,
                        trainRunsOnFri, trainRunsOnSat, trainRunsOnSun, duration,
                        stationList
                );

                trainList.add(trainObj);
            }
            cursor.close();
        }

        return trainList;
    }

    public Train_Schedule_Structure getTrainDataByTrainNumber(String trainNumber){
        ArrayList<Train_Schedule_Station_Structure> arrStationsList;
        ArrayList<Track_Polyline_Point_Structure> arrPolylineList;
        Train_Schedule_Structure myTrainData;
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s' ",
                TABLE_NAME,COLUMN_trainNumber,trainNumber);
        Cursor cursor = db.rawQuery(sqlQuery,null);
        cursor.moveToFirst();
            String trainName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainName));
            String trNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainNumber));

            String stationFrom = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationFrom));
            String stationTo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationTo));
            String trainRunsOnMon = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnMon));
            String    trainRunsOnTue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnTue));
            String     trainRunsOnWed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnWed));
            String      trainRunsOnThu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnThu));
            String  trainRunsOnFri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnFri));
            String  trainRunsOnSat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnSat));
            String  trainRunsOnSun = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainRunsOnSun));
            String trainDuration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_duration));
            arrStationsList =(getStationsForTrain(db, trainNumber));
            arrPolylineList = getTrainPolylinePoints(trainNumber);

            myTrainData = new Train_Schedule_Structure(trNumber,trainName,stationFrom,stationTo,
                    trainRunsOnMon,trainRunsOnTue,trainRunsOnWed,trainRunsOnThu,trainRunsOnFri,trainRunsOnSat,trainRunsOnSun,
                    trainDuration, arrStationsList, arrPolylineList);

            cursor.close();
            db.close();


        return  myTrainData;
    }

    public  ArrayList<Track_Polyline_Point_Structure> getTrainPolylinePoints(String trainNumber){
        ArrayList<Track_Polyline_Point_Structure> arrPolyLineList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s' ",
                                    POLYLINE_TABLE_NAME, COLUMN_trainNumber, trainNumber);
        Cursor cursor = db.rawQuery(sqlQuery,null);
        while(cursor.moveToNext()){
            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_stnLat));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_stnLng));
            arrPolyLineList.add(new Track_Polyline_Point_Structure(lat,lng));

        }
        db.close();
        cursor.close();
        return arrPolyLineList;
    }

    public ArrayList<Train_List_Structure_New> getTrainSearchResult(String trainNameOrNumber){
        ArrayList<Train_List_Structure_New> arrTrainListData = new ArrayList<>();
        try(SQLiteDatabase db = this.getReadableDatabase(); Station_List_DB_Helper dbHelper = new Station_List_DB_Helper(applicationContext)) {
            arrTrainListData.clear();
            String sqlQuery;
            Cursor cursor;
            if(trainNameOrNumber == null || trainNameOrNumber.isEmpty()){
                sqlQuery = String.format("SELECT %s, %s, %s, %s FROM %s ORDER BY RANDOM() LIMIT 10",
                        COLUMN_trainNumber,COLUMN_trainName, COLUMN_stationFrom, COLUMN_stationTo, TABLE_NAME);
            }else {
                sqlQuery = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s LIKE '%s%%' OR %s LIKE '%%%s%%' LIMIT 15",
                        COLUMN_trainNumber,COLUMN_trainName, COLUMN_stationFrom, COLUMN_stationTo, TABLE_NAME,
                        COLUMN_trainNumber, trainNameOrNumber, COLUMN_trainName,trainNameOrNumber);
            }
            cursor = db.rawQuery(sqlQuery,null);
            while(cursor.moveToNext()){

                String trNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainNumber));
                String trName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_trainName));

                String sourceStation = dbHelper.getStationNameByCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationFrom)));
                String destinationStation = dbHelper.getStationNameByCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_stationTo)));
                arrTrainListData.add(new Train_List_Structure_New(trNumber,trName,sourceStation,destinationStation));
            }
            cursor.close();
        }
    return arrTrainListData;

    }
}
