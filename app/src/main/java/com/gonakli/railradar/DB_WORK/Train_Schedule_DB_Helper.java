package com.gonakli.railradar.DB_WORK;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Train_Schedule_DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TRAIN_SCHEDULE_DB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "TRAIN_SCHEDULE";

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
                                "FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE ) ",STATION_TABLE_FOR_SCHEDULE,COLUMN_trainNumber, COLUMN_stationCode, COLUMN_stationName, COLUMN_arrivalTime,
                        COLUMN_departureTime,COLUMN_haltTime, COLUMN_distance, COLUMN_dayCount,
                        COLUMN_stnSerialNumber,COLUMN_trainNumber, TABLE_NAME, COLUMN_trainNumber);

        db.execSQL(CREATE_TRAIN_TABLE);
        db.execSQL(CREATE_STATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STATION_TABLE_FOR_SCHEDULE);
        onCreate(db);
    }
}
