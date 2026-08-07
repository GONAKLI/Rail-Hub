package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gonakli.railradar.Structure_Class.User_History_Structure;

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

    public User_Routes_History_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s (" +
               " %s text, %s text, %s text, %s text, %s text )", TABLE_NAME, TABLE_COLUMN_TRAIN_NUMBER,
               TABLE_COLUMN_TRAIN_NAME,TABLE_COLUMN_SOURCE_STATION_CODE,TABLE_COLUMN_DESTINATION_STATION_CODE,
               TABLE_COLUMN_ADDED_ON);
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

        db.insert(TABLE_NAME, null, values);
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
}
