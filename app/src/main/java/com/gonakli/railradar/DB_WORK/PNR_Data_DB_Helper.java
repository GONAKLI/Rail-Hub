package com.gonakli.railradar.DB_WORK;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gonakli.railradar.Structure_Class.PassengerList_Structure;
import com.gonakli.railradar.Structure_Class.Pnr_Api_Response_Structure;

import java.util.ArrayList;

public class PNR_Data_DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PNR_DATABASE";
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
    private final String TABLE_COLUMN_CHART_STATUS = "chartStatus";
    private final String TABLE_COLUMN_CREATED_AT = "createdAt";

    // passenger list Table

    private final String PASS_TABLE_NAME = "PASSENGER_LIST";
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

    // Information Table

    private final String INFO_TABLE_TABLE_NAME = "INFORMATION";

    private final String INFO_TABLE_COLUMN_MESSAGE = "message";



    public PNR_Data_DB_Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT" +
                ", %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT" +
                ", %s TEXT, %s TEXT, %s TEXT, %s DATETIME DEFAULT CURRENT_TIMESTAMP)",
                TABLE_NAME,TABLE_COLUMN_PNR_NUMBER,TABLE_COLUMN_DATE_OF_JOURNEY,
                TABLE_COLUMN_TRAIN_START_DATE,TABLE_COLUMN_TRAIN_NUMBER,TABLE_COLUMN_TRAIN_NAME,
                TABLE_COLUMN_SOURCE_STATION,TABLE_COLUMN_DESTINATION_STATION,TABLE_COLUMN_RESERVATION_UPTO,
                TABLE_COLUMN_BOARDING_POINT,TABLE_COLUMN_JOURNEY_CLASS,TABLE_COLUMN_NUMBER_OF_PASSENGER,
                TABLE_COLUMN_BOOKING_FARE,TABLE_COLUMN_TICKET_FARE,TABLE_COLUMN_QUOTA,TABLE_COLUMN_BOOKING_DATE,
                TABLE_COLUMN_TICKET_TYPE, TABLE_COLUMN_CHART_STATUS,TABLE_COLUMN_CREATED_AT);
        db.execSQL(sqlQuery);

        String sqlQueryForPassenger = String.format("CREATE TABLE IF NOT EXISTS %s" +
                " (%s TEXT, %s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT, FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE)"
                ,PASS_TABLE_NAME, TABLE_COLUMN_PNR_NUMBER, PASS_TABLE_COLUMN_passengerSerialNumber,
                PASS_TABLE_COLUMN_passengerBerthChoice,PASS_TABLE_COLUMN_passengerAge,PASS_TABLE_COLUMN_passengerNationality,
                PASS_TABLE_COLUMN_bookingStatus,PASS_TABLE_COLUMN_bookingCoachId,PASS_TABLE_COLUMN_bookingBerthNo,
                PASS_TABLE_COLUMN_bookingBerthCode,PASS_TABLE_COLUMN_bookingStatusDetails,PASS_TABLE_COLUMN_currentStatusIndex,
                PASS_TABLE_COLUMN_currentStatus,PASS_TABLE_COLUMN_currentBerthNo,PASS_TABLE_COLUMN_currentStatusDetails,
                TABLE_COLUMN_PNR_NUMBER, TABLE_NAME,TABLE_COLUMN_PNR_NUMBER);
        db.execSQL(sqlQueryForPassenger);

        String sqlQueryForInformation = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s TEXT, %s TEXT, FOREIGN KEY(%s) REFERENCES %s(%s) ON DELETE CASCADE )", INFO_TABLE_TABLE_NAME,
                TABLE_COLUMN_PNR_NUMBER, INFO_TABLE_COLUMN_MESSAGE,TABLE_COLUMN_PNR_NUMBER, TABLE_NAME,TABLE_COLUMN_PNR_NUMBER );
        db.execSQL(sqlQueryForInformation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + INFO_TABLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addPnrPassengerInDB(Pnr_Api_Response_Structure obj){

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String dateOfJourney = obj.getDateOfJourney();
            String trainStartDate = obj.getTrainStartDate();
            String trainNumber = obj.getTrainNumber();
            String  trainName = obj.getTrainName();
            String  sourceStation = obj.getSourceStation();
            String  pnrNumber = obj.getPnrNumber();
            String destinationStation = obj.getDestinationStation();
            String  reservationUpto = obj.getReservationUpto();
            String  boardingPoint = obj.getBoardingPoint();
            String  journeyClass = obj.getJourneyClass();
            String  numberOfPassenger = obj.getNumberOfPassenger();
            String chartStatus = obj.getChartStatus();
            String  bookingFare = obj.getBookingFare();
            String  quota = obj.getQuota();
            String  ticketFare = obj.getTicketFare();
            String bookingDate = obj.getBookingDate();
            String ticketType = obj.getTicketType();
            ArrayList<PassengerList_Structure> arrPassengerList = obj.getArrPassengerList();
            ArrayList<Object> arrInformationMessage = obj.getArrInformationMessage();

            ContentValues values = new ContentValues();
            values.put(TABLE_COLUMN_PNR_NUMBER, pnrNumber);
            values.put(TABLE_COLUMN_DATE_OF_JOURNEY, dateOfJourney);
            values.put(TABLE_COLUMN_TRAIN_START_DATE, trainStartDate);
            values.put(TABLE_COLUMN_TRAIN_NUMBER, trainNumber );
            values.put(TABLE_COLUMN_TRAIN_NAME, trainName );
            values.put(TABLE_COLUMN_SOURCE_STATION, sourceStation );
            values.put(TABLE_COLUMN_DESTINATION_STATION, destinationStation );
            values.put(TABLE_COLUMN_RESERVATION_UPTO, reservationUpto );
            values.put(TABLE_COLUMN_BOARDING_POINT, boardingPoint );
            values.put(TABLE_COLUMN_JOURNEY_CLASS, journeyClass );
            values.put(TABLE_COLUMN_NUMBER_OF_PASSENGER, numberOfPassenger );
            values.put(TABLE_COLUMN_BOOKING_FARE, bookingFare );
            values.put(TABLE_COLUMN_TICKET_FARE, ticketFare );
            values.put(TABLE_COLUMN_QUOTA, quota );
            values.put(TABLE_COLUMN_BOOKING_DATE, bookingDate );
            values.put(TABLE_COLUMN_TICKET_TYPE, ticketType );
            values.put(TABLE_COLUMN_CHART_STATUS, chartStatus);
            db.insertOrThrow(TABLE_NAME,null, values);


            for(PassengerList_Structure passenger: arrPassengerList){
                String passengerSerialNumber = passenger.getPassengerSerialNumber();
                String     passengerAge = passenger.getPassengerAge();
                String  passengerBerthChoice = passenger.getPassengerBerthChoice();
                String     passengerNationality = passenger.getPassengerNationality();
                String bookingStatus = passenger.getBookingStatus();
                String     bookingCoachId = passenger.getBookingCoachId();
                String      bookingBerthNo = passenger.getBookingBerthNo();
                String      bookingBerthCode = passenger.getBookingBerthCode();
                String     bookingStatusDetails = passenger.getBookingStatusDetails();
                String currentStatus = passenger.getCurrentStatus();
                String   currentBerthNo = passenger.getCurrentBerthNo();
                String    psgnwlType = passenger.getPsgnwlType();
                String currentStatusIndex = passenger.getCurrentStatusIndex();
                String currentStatusDetails = passenger.getCurrentStatusDetails();
                ContentValues passValues = new ContentValues();

                passValues.put(TABLE_COLUMN_PNR_NUMBER, pnrNumber);
                passValues.put( PASS_TABLE_COLUMN_passengerSerialNumber ,passengerSerialNumber );
                passValues.put( PASS_TABLE_COLUMN_passengerBerthChoice ,passengerBerthChoice );
                passValues.put( PASS_TABLE_COLUMN_passengerAge , passengerAge );
                passValues.put( PASS_TABLE_COLUMN_passengerNationality , passengerNationality );
                passValues.put( PASS_TABLE_COLUMN_bookingStatus  , bookingStatus );
                passValues.put( PASS_TABLE_COLUMN_bookingCoachId , bookingCoachId );
                passValues.put( PASS_TABLE_COLUMN_bookingBerthNo , bookingBerthNo );
                passValues.put( PASS_TABLE_COLUMN_bookingBerthCode , bookingBerthCode );
                passValues.put( PASS_TABLE_COLUMN_bookingStatusDetails ,  bookingStatusDetails);
                passValues.put( PASS_TABLE_COLUMN_currentStatusIndex ,  currentStatusIndex);
                passValues.put( PASS_TABLE_COLUMN_currentStatus , currentStatus);
                passValues.put( PASS_TABLE_COLUMN_currentBerthNo ,  currentBerthNo);
                passValues.put( PASS_TABLE_COLUMN_currentStatusDetails , currentStatusDetails );
                db.insertOrThrow(PASS_TABLE_NAME, null, passValues);


            }
            for(Object message:  arrInformationMessage){
                if (message != null && !message.toString().equalsIgnoreCase("null") && !message.toString().isEmpty()){
                    String msg = message.toString().trim();
                    ContentValues infoValues = new ContentValues();
                    infoValues.put(TABLE_COLUMN_PNR_NUMBER, pnrNumber);
                    infoValues.put(INFO_TABLE_COLUMN_MESSAGE, msg);
                    db.insert(INFO_TABLE_TABLE_NAME,null, infoValues);

                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("PNR_DB", "addPnrPassengerInDB: error occurred" + e);
        }finally {
            db.endTransaction();
            db.close();
        }

    }

    public ArrayList<Pnr_Api_Response_Structure> getPnrDataFromDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Pnr_Api_Response_Structure> arrPnrList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +" ORDER BY datetime(" + TABLE_COLUMN_CREATED_AT + ") DESC", null );
        while(cursor.moveToNext()){
            Pnr_Api_Response_Structure pnrObj = new Pnr_Api_Response_Structure(true, "");
            String pnrNumber = cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_PNR_NUMBER));
            pnrObj.setPnrNumber(pnrNumber);
            pnrObj.setDateOfJourney(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_DATE_OF_JOURNEY)));
            pnrObj.setTrainStartDate(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TRAIN_START_DATE)));
            pnrObj.setTrainNumber(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TRAIN_NUMBER)));
            pnrObj.setTrainName(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TRAIN_NAME)));
            pnrObj.setSourceStation(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_SOURCE_STATION)));
            pnrObj.setDestinationStation(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_DESTINATION_STATION)));
            pnrObj.setReservationUpto(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_RESERVATION_UPTO)));
            pnrObj.setBoardingPoint(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_BOARDING_POINT)));
            pnrObj.setJourneyClass(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_JOURNEY_CLASS)));
            pnrObj.setNumberOfPassenger(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_NUMBER_OF_PASSENGER)));
            pnrObj.setBookingFare(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_BOOKING_FARE)));
            pnrObj.setTicketFare(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TICKET_FARE)));
            pnrObj.setQuota(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_QUOTA)));
            pnrObj.setBookingDate(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_BOOKING_DATE)));
            pnrObj.setTicketType(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_TICKET_TYPE)));
            pnrObj.setChartStatus(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_COLUMN_CHART_STATUS)));

            ArrayList<PassengerList_Structure> arrPassengerList = new ArrayList<>();
            String passSqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'", PASS_TABLE_NAME,
                    TABLE_COLUMN_PNR_NUMBER, pnrNumber);
           Cursor passCursor = db.rawQuery(passSqlQuery, null);
           while (passCursor.moveToNext()){
               PassengerList_Structure passenger = new PassengerList_Structure();
               passenger.setPassengerSerialNumber(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_passengerSerialNumber)));
               passenger.setPassengerBerthChoice(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_passengerBerthChoice)));
               passenger.setPassengerAge(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_passengerAge)));
               passenger.setPassengerNationality(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_passengerNationality)));
               passenger.setBookingStatus(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_bookingStatus)));
               passenger.setBookingCoachId(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_bookingCoachId)));
               passenger.setBookingBerthNo(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_bookingBerthNo)));
               passenger.setBookingBerthCode(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_bookingBerthCode)));
               passenger.setBookingStatusDetails(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_bookingStatusDetails)));
               passenger.setCurrentStatusIndex(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_currentStatusIndex)));
               passenger.setCurrentStatus(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_currentStatus)));
               passenger.setCurrentBerthNo(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_currentBerthNo)));
               passenger.setCurrentStatusDetails(passCursor.getString(passCursor.getColumnIndexOrThrow(PASS_TABLE_COLUMN_currentStatusDetails)));
               arrPassengerList.add(passenger);
           }
           pnrObj.setArrPassengerList(arrPassengerList);
           passCursor.close();

           ArrayList<Object> arrInfoObject = new ArrayList<>();
           String infoQuery = String.format("SELECT * FROM %s WHERE %s = '%s' ", INFO_TABLE_TABLE_NAME,
                   TABLE_COLUMN_PNR_NUMBER,pnrNumber);
           Cursor infoCursor = db.rawQuery(infoQuery, null);

           while (infoCursor.moveToNext()){
              String msg = infoCursor.getString(infoCursor.getColumnIndexOrThrow(INFO_TABLE_COLUMN_MESSAGE));
              arrInfoObject.add(msg);
           }
           pnrObj.setArrInformationMessage(arrInfoObject);
           infoCursor.close();

           arrPnrList.add(pnrObj);

        }
        cursor.close();
        db.close();
        return arrPnrList;
    }

    public void updatePnrDataInDB(Pnr_Api_Response_Structure obj){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String dateOfJourney = obj.getDateOfJourney();
            String trainStartDate = obj.getTrainStartDate();
            String trainNumber = obj.getTrainNumber();
            String  trainName = obj.getTrainName();
            String  sourceStation = obj.getSourceStation();
            String  pnrNumber = obj.getPnrNumber();
            String destinationStation = obj.getDestinationStation();
            String  reservationUpto = obj.getReservationUpto();
            String  boardingPoint = obj.getBoardingPoint();
            String  journeyClass = obj.getJourneyClass();
            String  numberOfPassenger = obj.getNumberOfPassenger();
            String chartStatus = obj.getChartStatus();
            String  bookingFare = obj.getBookingFare();
            String  quota = obj.getQuota();
            String  ticketFare = obj.getTicketFare();
            String bookingDate = obj.getBookingDate();
            String ticketType = obj.getTicketType();
            ArrayList<PassengerList_Structure> arrPassengerList = obj.getArrPassengerList();
            ArrayList<Object> arrInformationMessage = obj.getArrInformationMessage();

            ContentValues values = new ContentValues();
            values.put(TABLE_COLUMN_PNR_NUMBER, pnrNumber);
            values.put(TABLE_COLUMN_DATE_OF_JOURNEY, dateOfJourney);
            values.put(TABLE_COLUMN_TRAIN_START_DATE, trainStartDate);
            values.put(TABLE_COLUMN_TRAIN_NUMBER, trainNumber );
            values.put(TABLE_COLUMN_TRAIN_NAME, trainName );
            values.put(TABLE_COLUMN_SOURCE_STATION, sourceStation );
            values.put(TABLE_COLUMN_DESTINATION_STATION, destinationStation );
            values.put(TABLE_COLUMN_RESERVATION_UPTO, reservationUpto );
            values.put(TABLE_COLUMN_BOARDING_POINT, boardingPoint );
            values.put(TABLE_COLUMN_JOURNEY_CLASS, journeyClass );
            values.put(TABLE_COLUMN_NUMBER_OF_PASSENGER, numberOfPassenger );
            values.put(TABLE_COLUMN_BOOKING_FARE, bookingFare );
            values.put(TABLE_COLUMN_TICKET_FARE, ticketFare );
            values.put(TABLE_COLUMN_QUOTA, quota );
            values.put(TABLE_COLUMN_BOOKING_DATE, bookingDate );
            values.put(TABLE_COLUMN_TICKET_TYPE, ticketType );
            values.put(TABLE_COLUMN_CHART_STATUS, chartStatus);
            db.update(TABLE_NAME,  values, TABLE_COLUMN_PNR_NUMBER + " = ? ", new String[]{pnrNumber});

            db.delete(PASS_TABLE_NAME, TABLE_COLUMN_PNR_NUMBER + " = ?", new String[]{pnrNumber});
            db.delete(INFO_TABLE_TABLE_NAME, TABLE_COLUMN_PNR_NUMBER + " = ?", new String[]{pnrNumber});
            for(PassengerList_Structure passenger: arrPassengerList){
                String passengerSerialNumber = passenger.getPassengerSerialNumber();
                String     passengerAge = passenger.getPassengerAge();
                String  passengerBerthChoice = passenger.getPassengerBerthChoice();
                String     passengerNationality = passenger.getPassengerNationality();
                String bookingStatus = passenger.getBookingStatus();
                String     bookingCoachId = passenger.getBookingCoachId();
                String      bookingBerthNo = passenger.getBookingBerthNo();
                String      bookingBerthCode = passenger.getBookingBerthCode();
                String     bookingStatusDetails = passenger.getBookingStatusDetails();
                String currentStatus = passenger.getCurrentStatus();
                String   currentBerthNo = passenger.getCurrentBerthNo();
                String    psgnwlType = passenger.getPsgnwlType();
                String currentStatusIndex = passenger.getCurrentStatusIndex();
                String currentStatusDetails = passenger.getCurrentStatusDetails();
                ContentValues passValues = new ContentValues();

                passValues.put(TABLE_COLUMN_PNR_NUMBER, pnrNumber);
                passValues.put( PASS_TABLE_COLUMN_passengerSerialNumber ,passengerSerialNumber );
                passValues.put( PASS_TABLE_COLUMN_passengerBerthChoice ,passengerBerthChoice );
                passValues.put( PASS_TABLE_COLUMN_passengerAge , passengerAge );
                passValues.put( PASS_TABLE_COLUMN_passengerNationality , passengerNationality );
                passValues.put( PASS_TABLE_COLUMN_bookingStatus  , bookingStatus );
                passValues.put( PASS_TABLE_COLUMN_bookingCoachId , bookingCoachId );
                passValues.put( PASS_TABLE_COLUMN_bookingBerthNo , bookingBerthNo );
                passValues.put( PASS_TABLE_COLUMN_bookingBerthCode , bookingBerthCode );
                passValues.put( PASS_TABLE_COLUMN_bookingStatusDetails ,  bookingStatusDetails);
                passValues.put( PASS_TABLE_COLUMN_currentStatusIndex ,  currentStatusIndex);
                passValues.put( PASS_TABLE_COLUMN_currentStatus , currentStatus);
                passValues.put( PASS_TABLE_COLUMN_currentBerthNo ,  currentBerthNo);
                passValues.put( PASS_TABLE_COLUMN_currentStatusDetails , currentStatusDetails );
                db.insert(PASS_TABLE_NAME,null, passValues);


            }
            for(Object message:  arrInformationMessage){
                if (message != null && !message.toString().equalsIgnoreCase("null") && !message.toString().isEmpty()){
                    String msg = message.toString().trim();
                    ContentValues infoValues = new ContentValues();
                    infoValues.put(TABLE_COLUMN_PNR_NUMBER, pnrNumber);
                    infoValues.put(INFO_TABLE_COLUMN_MESSAGE, msg);
                    db.insert(INFO_TABLE_TABLE_NAME,null, infoValues );

                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("PNR_DB", "addPnrPassengerInDB: error occurred" + e);
        }finally {
            db.endTransaction();
            db.close();
        }

    }

    public void deletePnrFromDB(String pnrNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,TABLE_COLUMN_PNR_NUMBER + " = ?",new String[]{pnrNumber});
        db.close();
    }
}
