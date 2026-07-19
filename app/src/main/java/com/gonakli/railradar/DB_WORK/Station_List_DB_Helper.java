package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Trains_And_Stations_List;
import com.gonakli.railradar.DB_MODAL.Station_List_Modal_Class;

import java.util.ArrayList;

public class Station_List_DB_Helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "STATION_DB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "STATION_LIST_TABLE";

    private static final String TABLE_STATION_CODE_COLUMN = "STATION_CODE";
    private static final String TABLE_STATION_NAME_COLUMN = "STATION_NAME";

    private final Context applicationContext;

    public Station_List_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT )",
                TABLE_NAME,
                TABLE_STATION_CODE_COLUMN,
                TABLE_STATION_NAME_COLUMN));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addStationInDB(){
    Make_Array_Of_Trains_And_Stations_List stationService = new Make_Array_Of_Trains_And_Stations_List();
    ArrayList<Station_List_Modal_Class> arrStationList = stationService.addStations(applicationContext);

        SQLiteDatabase db = this.getWritableDatabase();
    try{

        db.beginTransaction();
        for (Station_List_Modal_Class station : arrStationList){
            ContentValues values =new ContentValues();
            values.put(TABLE_STATION_CODE_COLUMN, station.getStation_Code());
            values.put(TABLE_STATION_NAME_COLUMN, station.getStation_Name());
            db.insert(TABLE_NAME, null, values);
        }
        db.setTransactionSuccessful();

    }finally {
        db.endTransaction();
        db.close();
    }


    }

    public ArrayList<Station_List_Modal_Class> getStationList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s", TABLE_NAME), null);
        ArrayList<Station_List_Modal_Class> arrStationList = new ArrayList<>();
        while (cursor.moveToNext()){
            String code = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_STATION_CODE_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_STATION_NAME_COLUMN));
            arrStationList.add(new Station_List_Modal_Class(code, name));
        }
        db.close();
        cursor.close();
        return arrStationList;
    }






}
