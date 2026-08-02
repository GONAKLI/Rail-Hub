package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

// import com.gonakli.railradar.ArrayGenerater.Make_Array_Of_Trains_And_Stations_List;
import com.gonakli.railradar.Structure_Class.Train_List_Structure;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class Train_List_DB_Helper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "GONAKLI.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "TRAIN_LIST_TABLE";

    private static final String TABLE_TRAIN_NUMBER_COLUMN = "trainNumber";
    private static final String TABLE_TRAIN_NAME_COLUMN = "trainName";
    private static final String TABLE_TRAIN_TYPE_COLUMN = "trainType";

//    private static final String TABLE_TRAIN_NUMBER_COLUMN = "TRAIN_NUMBER";
//    private static final String TABLE_TRAIN_NAME_COLUMN = "TRAIN_NAME";
//    private static final String TABLE_TRAIN_TYPE_COLUMN = "TRAIN_TYPE";

    private final Context applicationContext;
    public Train_List_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.applicationContext = context;
    }

  //  @Override
//    public void onCreate(SQLiteDatabase db) {
////        db.execSQL(String.format(
////                "CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT )",
////                TABLE_NAME,
////                TABLE_TRAIN_NUMBER_COLUMN,
////                TABLE_TRAIN_NAME_COLUMN,
////                TABLE_TRAIN_TYPE_COLUMN));
//    }
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
//    }

//    public void addTrainsInDB(){
//        Make_Array_Of_Trains_And_Stations_List arrProvider = new Make_Array_Of_Trains_And_Stations_List();
//       ArrayList<Train_List_Structure> arrTrainList =  arrProvider.addTrains(applicationContext);
//        SQLiteDatabase db = this.getWritableDatabase();
//
//       try {
//           db.beginTransaction();
//           for (Train_List_Structure data : arrTrainList) {
//               ContentValues values = new ContentValues();
//               values.put(TABLE_TRAIN_NUMBER_COLUMN, data.getTrainNumber());
//               values.put(TABLE_TRAIN_NAME_COLUMN, data.getTrainName());
//               values.put(TABLE_TRAIN_TYPE_COLUMN, data.getTrainType());
//               db.insert(TABLE_NAME, null, values);
//           }
//           db.setTransactionSuccessful();
//       }finally {
//           db.endTransaction();
//           db.close();
//       }
//    }

    public ArrayList<Train_List_Structure> getTrainList(){
        ArrayList<Train_List_Structure> arrTrainList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s", TABLE_NAME), null);
        while(cursor.moveToNext()){
            String trainNumber = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_TRAIN_NUMBER_COLUMN));
            String trainName = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_TRAIN_NAME_COLUMN));
            String trainType = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_TRAIN_TYPE_COLUMN));
            arrTrainList.add(new Train_List_Structure(trainNumber, trainName, trainType));
        }
        cursor.close();
        db.close();
        return  arrTrainList;

    }

}
