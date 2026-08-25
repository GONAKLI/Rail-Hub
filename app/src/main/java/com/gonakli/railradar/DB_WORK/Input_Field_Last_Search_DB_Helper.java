package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gonakli.railradar.Structure_Class.Input_Field_Last_Search_Structure;

public class Input_Field_Last_Search_DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Recent Searches";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "Recent_Search";
    private final String TABLE_COLUMN_FROM_CODE = "fromCode";
    private final String TABLE_COLUMN_FROM_VALUE = "fromValue";
    private final String TABLE_COLUMN_TO_CODE = "toCode";
    private final String TABLE_COLUMN_TO_VALUE = "toValue";


    public Input_Field_Last_Search_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT ) ",
            TABLE_NAME,TABLE_COLUMN_FROM_CODE,TABLE_COLUMN_FROM_VALUE,TABLE_COLUMN_TO_CODE,TABLE_COLUMN_TO_VALUE);
    db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sqlQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sqlQuery);
            this.onCreate(db);
    }

    public void insertRecentFieldDataInDB(Input_Field_Last_Search_Structure obj){
        try(SQLiteDatabase db = this.getWritableDatabase()){
            ContentValues values = new ContentValues();
            values.put(TABLE_COLUMN_FROM_CODE, obj.getFromCode());
            values.put(TABLE_COLUMN_FROM_VALUE, obj.getFromValue());
            values.put(TABLE_COLUMN_TO_CODE, obj.getToCode());
            values.put(TABLE_COLUMN_TO_VALUE, obj.getToValue());
            db.insertOrThrow(TABLE_NAME,null,values);
        }
    }

    public Input_Field_Last_Search_Structure getRecentFieldData(){
        Cursor cursor = null;
        try(SQLiteDatabase db = this.getReadableDatabase()){
            Input_Field_Last_Search_Structure inputFieldLastSearchStructure = null;
            String sqlQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC LIMIT 1";
            cursor = db.rawQuery(sqlQuery,null);

            if(cursor.moveToFirst()){
                String fCode = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_FROM_CODE));
                String fValue = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_FROM_VALUE));
                String tCode = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TO_CODE));
                String tValue = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TO_VALUE));
            inputFieldLastSearchStructure = new Input_Field_Last_Search_Structure(fCode,fValue,tCode,tValue);
            }
            return inputFieldLastSearchStructure;
        }finally {
            if(cursor != null)  cursor.close();
        }
    }
}
