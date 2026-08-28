package com.gonakli.railHub.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gonakli.railHub.Structure_Class.User_History_Structure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class User_Routes_History_DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "USER_CHOICES";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "History";
    private final String TABLE_COLUMN_TRAIN_NUMBER = "trainNumber";
    private final String TABLE_COLUMN_TRAIN_NAME = "trainName";
    private final String TABLE_COLUMN_SOURCE_STATION_CODE = "sourceCode";
    private final String TABLE_COLUMN_DESTINATION_STATION_CODE = "destinationCode";
    private final String TABLE_COLUMN_ADDED_ON = "addedOn";
    private final String TABLE_COLUMN_UNIQUE_ID = "id";

    public User_Routes_History_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s (" +
               " %s text, %s text, %s text, %s text, %s text, %s text PRIMARY KEY)", TABLE_NAME, TABLE_COLUMN_TRAIN_NUMBER,
               TABLE_COLUMN_TRAIN_NAME,TABLE_COLUMN_SOURCE_STATION_CODE,TABLE_COLUMN_DESTINATION_STATION_CODE,
               TABLE_COLUMN_ADDED_ON, TABLE_COLUMN_UNIQUE_ID);
        db.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      String sqlQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sqlQuery);
        this.onCreate(db);
    }

    public void addHistoryInDB(String trainNumber, String trainName, String source, String destination){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMN_TRAIN_NUMBER, trainNumber);
        values.put(TABLE_COLUMN_TRAIN_NAME, trainName);
        values.put(TABLE_COLUMN_SOURCE_STATION_CODE, source);
        values.put(TABLE_COLUMN_DESTINATION_STATION_CODE, destination);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put(TABLE_COLUMN_ADDED_ON, currentDate );
        String id = trainNumber+source+destination;
        values.put(TABLE_COLUMN_UNIQUE_ID, id);
        db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public ArrayList<User_History_Structure> getHistory(){
        ArrayList<User_History_Structure> history = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 10", TABLE_NAME, TABLE_COLUMN_ADDED_ON);
        Cursor cursor = db.rawQuery(sqlQuery, null);
        while (cursor.moveToNext()){
            String trainNumber = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TRAIN_NUMBER));
            String trainName  = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TRAIN_NAME));
            String sourceCode = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_SOURCE_STATION_CODE));
            String destinationCode = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_DESTINATION_STATION_CODE));
            history.add(new User_History_Structure(trainNumber,trainName,sourceCode,destinationCode));
        }
        db.close();
        cursor.close();
        return history;
    }

    public void deleteHistory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void updateHistory(String trainNumber, String fromStationCode, String toStationCode){
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String sqlQuery = String.format("UPDATE %S SET %s = \"%s\" WHERE %s = \"%s\" AND %s = \"%s\" AND %s = \"%s\"",
                TABLE_NAME, TABLE_COLUMN_ADDED_ON, currentDate, TABLE_COLUMN_TRAIN_NUMBER, trainNumber, TABLE_COLUMN_SOURCE_STATION_CODE,
                fromStationCode, TABLE_COLUMN_DESTINATION_STATION_CODE, toStationCode);

        try(SQLiteDatabase db = this.getWritableDatabase()){
            db.execSQL(sqlQuery);
        }
    }
}
