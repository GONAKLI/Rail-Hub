package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Train_Schedule;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Station_Structure;
import com.gonakli.railradar.Structure_Class.Train_Schedule_Structure;

import java.util.ArrayList;

public class Train_Schedule_DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TRAIN_SCHEDULE_DB";
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

    private final Context applicationContext;

    public Train_Schedule_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRAIN_TABLE =
                String.format("create table %s ( " +
                        "%s text primary key, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text )", TABLE_NAME, COLUMN_trainNumber, COLUMN_trainName,
                        COLUMN_stationFrom, COLUMN_stationTo,COLUMN_trainRunsOnMon,
                        COLUMN_trainRunsOnTue, COLUMN_trainRunsOnWed, COLUMN_trainRunsOnThu,
                        COLUMN_trainRunsOnFri, COLUMN_trainRunsOnSat, COLUMN_trainRunsOnSun,
                        COLUMN_duration);

        String CREATE_STATION_TABLE =
                String.format("create table %s ( " +
                        "id integer primary key autoincrement, " +
                        "%s text not null, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                        "%s text, " +
                                "%s text, " +
                                "%s text, " +
                                "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE ) ",STATION_TABLE_FOR_SCHEDULE,COLUMN_trainNumber, COLUMN_stationCode, COLUMN_stationName, COLUMN_arrivalTime,
                        COLUMN_departureTime,COLUMN_haltTime, COLUMN_distance, COLUMN_dayCount,
                        COLUMN_stnSerialNumber, COLUMN_stnLat, COLUMN_stnLng,COLUMN_trainNumber, TABLE_NAME, COLUMN_trainNumber);

        db.execSQL(CREATE_TRAIN_TABLE);
        db.execSQL(CREATE_STATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STATION_TABLE_FOR_SCHEDULE);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void addScheduleInDB(){
        Make_Array_Of_Train_Schedule schedule = new Make_Array_Of_Train_Schedule();
        ArrayList<Train_Schedule_Structure> arrSchedule = schedule.addTrainSchedule(applicationContext);
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.beginTransaction();
            for(Train_Schedule_Structure data: arrSchedule){
                ContentValues values = new ContentValues();

                values.put(COLUMN_trainNumber, data.getTrainNumber());
                values.put(COLUMN_trainName, data.getTrainName());
                values.put(COLUMN_stationFrom, data.getStationFrom());
                values.put(COLUMN_stationTo, data.getStationTo());
                values.put(COLUMN_trainRunsOnMon, data.getTrainRunsOnMon());
                values.put(COLUMN_trainRunsOnTue, data.getTrainRunsOnTue());
                values.put(COLUMN_trainRunsOnWed, data.getTrainRunsOnWed());
                values.put(COLUMN_trainRunsOnThu, data.getTrainRunsOnThu());
                values.put(COLUMN_trainRunsOnFri, data.getTrainRunsOnFri());
                values.put(COLUMN_trainRunsOnSat, data.getTrainRunsOnSat());
                values.put(COLUMN_trainRunsOnSun, data.getTrainRunsOnSun());
                values.put(COLUMN_duration, data.getDuration());

                db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                if(data.getStationList() != null){

                    for(Train_Schedule_Station_Structure stationData : data.getStationList()){
                        ContentValues stationValues = new ContentValues();

                        stationValues.put(COLUMN_trainNumber, data.getTrainNumber());
                        stationValues.put(COLUMN_stationCode, stationData.getStationCode());
                        stationValues.put(COLUMN_stationName, stationData.getStationName());
                        stationValues.put(COLUMN_arrivalTime, stationData.getArrivalTime());
                        stationValues.put(COLUMN_departureTime, stationData.getDepartureTime());
                        stationValues.put(COLUMN_haltTime, stationData.getHaltTime());
                        stationValues.put(COLUMN_distance, stationData.getDistance());
                        stationValues.put(COLUMN_dayCount, stationData.getDayCount());
                        stationValues.put(COLUMN_stnSerialNumber, stationData.getStnSerialNumber());

                        stationValues.put(COLUMN_stnLat, stationData.getStnLat());
                        stationValues.put(COLUMN_stnLng, stationData.getStnLng());

                        db.insert(STATION_TABLE_FOR_SCHEDULE,null, stationValues);
                    }
                }

            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }
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

            myTrainData = new Train_Schedule_Structure(trNumber,trainName,stationFrom,stationTo,
                    trainRunsOnMon,trainRunsOnTue,trainRunsOnWed,trainRunsOnThu,trainRunsOnFri,trainRunsOnSat,trainRunsOnSun,
                    trainDuration, arrStationsList);

            cursor.close();
            db.close();


        return  myTrainData;
    }
}
