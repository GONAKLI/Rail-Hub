package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Trains_And_Stations_List;
import com.gonakli.railradar.Structure_Class.Station_List_Structure;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Station_List_DB_Helper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "GONAKLI.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "STATION_LIST_TABLE";
    private static final String TABLE_STATION_CODE_COLUMN = "code";
    private static final String TABLE_STATION_NAME_COLUMN = "name";

    private static final String TABLE_STATION_LAT_COLUMN = "lat";
    private static final String TABLE_STATION_LNG_COLUMN = "lng";

//    private static final String TABLE_STATION_CODE_COLUMN = "STATION_CODE";
//    private static final String TABLE_STATION_NAME_COLUMN = "STATION_NAME";
//
//    private static final String TABLE_STATION_LAT_COLUMN = "STATION_LAT";
//    private static final String TABLE_STATION_LNG_COLUMN = "STATION_LNG";

    private final Context applicationContext;

    public Station_List_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = context;
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {

//        db.execSQL(String.format(
//                "CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT )",
//                TABLE_NAME,
//                TABLE_STATION_CODE_COLUMN,
//                TABLE_STATION_NAME_COLUMN,
//                TABLE_STATION_LAT_COLUMN,
//                TABLE_STATION_LNG_COLUMN));
   // }

//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
//
//    }

//    public void addStationInDB(){
//    Make_Array_Of_Trains_And_Stations_List stationService = new Make_Array_Of_Trains_And_Stations_List();
//    ArrayList<Station_List_Structure> arrStationList = stationService.addStations(applicationContext);
//
//        SQLiteDatabase db = this.getWritableDatabase();
//    try{
//
//        db.beginTransaction();
//        for (Station_List_Structure station : arrStationList){
//            ContentValues values =new ContentValues();
//            values.put(TABLE_STATION_CODE_COLUMN, station.getStation_Code());
//            values.put(TABLE_STATION_NAME_COLUMN, station.getStation_Name());
//
//            values.put(TABLE_STATION_LAT_COLUMN, station.getStation_Lat());
//            values.put(TABLE_STATION_LNG_COLUMN, station.getStation_Lng());
//
//            db.insert(TABLE_NAME, null, values);
//        }
//        db.setTransactionSuccessful();
//
//    }finally {
//        db.endTransaction();
//        db.close();
//    }
//
//
//    }

    public ArrayList<Station_List_Structure> getStationList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s", TABLE_NAME), null);
        ArrayList<Station_List_Structure> arrStationList = new ArrayList<>();
        while (cursor.moveToNext()){
            String code = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_STATION_CODE_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_STATION_NAME_COLUMN));

            String lat = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_STATION_LAT_COLUMN));
            String lng = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_STATION_LNG_COLUMN));
            arrStationList.add(new Station_List_Structure(code, name, lat, lng));
        }
        db.close();
        cursor.close();
        return arrStationList;
    }






}
