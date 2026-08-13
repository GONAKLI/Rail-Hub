package com.gonakli.railradar.DB_WORK;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PNR_Data_DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GONAKLI.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "User_Pnr_Data";
    private final String TABLE_COLUMN_PNR_NUMBER = "pnrNumber";
    private final String TABLE_COLUMN_DATE_OF_JOURNEY = "dateOfJourney";
    private final String TABLE_COLUMN_TRAIN_START_DATE = "trainStartDate";
    private final String TABLE_COLUMN_TRAIN_NUMBER = "trainNumber";
    private final String TABLE_COLUMN_TRAIN_NAME = "trainName";
    private final String TABLE_COLUMN_SOURCE_STATION = "sourceStation";
    private final String TABLE_COLUMN_DESTINATION_STATION = "destinationStation";
    private final String TABLE_COLUMN_RESERVATION_UPTO = "reservationUpto";
    private final String TABLE_COLUMN_BOARDING_POINT = "boardingPoint";
    private final String TABLE_COLUMN_JOURNEY_CLASS = "journeyClass";
    private final String TABLE_COLUMN_NUMBER_OF_PASSENGER = "numberOfpassenger";
    private final String TABLE_COLUMN_BOOKING_FARE = "bookingFare";
    private final String TABLE_COLUMN_TICKET_FARE = "ticketFare";
    private final String TABLE_COLUMN_QUOTA = "quota";
    private final String TABLE_COLUMN_BOOKING_DATE = "bookingDate";
    private final String TABLE_COLUMN_TICKET_TYPE = "ticketType";

    // passenger list Table

    private final String PASS_TABLE_COLUMN_passengerSerialNumber = "passengerSerialNumber";
    private final String PASS_TABLE_COLUMN_passengerBerthChoice = "passengerBerthChoice";
    private final String PASS_TABLE_COLUMN_passengerAge = "passengerAge";
    private final String PASS_TABLE_COLUMN_passengerNationality = "passengerNationality";
    private final String PASS_TABLE_COLUMN_bookingStatus = "bookingStatus";
    private final String PASS_TABLE_COLUMN_bookingCoachId = "bookingCoachId";
    private final String PASS_TABLE_COLUMN_bookingBerthNo = "bookingBerthNo";
    private final String PASS_TABLE_COLUMN_bookingBerthCode = "bookingBerthCode";
    private final String PASS_TABLE_COLUMN_bookingStatusDetails = "bookingStatusDetails";
    private final String PASS_TABLE_COLUMN_currentStatusIndex = "currentStatusIndex";
    private final String PASS_TABLE_COLUMN_currentStatus = "currentStatus";
    private final String PASS_TABLE_COLUMN_currentBerthNo = "currentBerthNo";
    private final String PASS_TABLE_COLUMN_currentStatusDetails = "currentStatusDetails";



    public PNR_Data_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT" +
                ", %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT" +
                ", %s TEXT, %s TEXT)",
                TABLE_NAME,TABLE_COLUMN_PNR_NUMBER,TABLE_COLUMN_DATE_OF_JOURNEY,
                TABLE_COLUMN_TRAIN_START_DATE,TABLE_COLUMN_TRAIN_NUMBER,TABLE_COLUMN_TRAIN_NAME,
                TABLE_COLUMN_SOURCE_STATION,TABLE_COLUMN_DESTINATION_STATION,TABLE_COLUMN_RESERVATION_UPTO,
                TABLE_COLUMN_BOARDING_POINT,TABLE_COLUMN_JOURNEY_CLASS,TABLE_COLUMN_NUMBER_OF_PASSENGER,
                TABLE_COLUMN_BOOKING_FARE,TABLE_COLUMN_TICKET_FARE,TABLE_COLUMN_QUOTA,TABLE_COLUMN_BOOKING_DATE,
                TABLE_COLUMN_TICKET_TYPE);
        db.execSQL(sqlQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
}
