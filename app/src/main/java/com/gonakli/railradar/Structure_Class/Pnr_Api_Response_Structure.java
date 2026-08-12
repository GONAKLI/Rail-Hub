package com.gonakli.railradar.Structure_Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Pnr_Api_Response_Structure implements Serializable {
   private final String errorMessage;
   private final boolean isSuccess;

    String dateOfJourney, trainStartDate, trainNumber, trainName,sourceStation, pnrNumber;
    String destinationStation, reservationUpto,boardingPoint,journeyClass,numberOfpassenger;
    String chartStatus,bookingFare,quota;
    ArrayList<Object> arrInformationMessage;
    ArrayList<PassengerList_Structure> arrPassengerList;


    public Pnr_Api_Response_Structure(boolean isSuccess, String errorMessage) {
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    public Pnr_Api_Response_Structure( boolean isSuccess,String pnrNumber, String dateOfJourney, String trainStartDate, String trainNumber, String trainName, String sourceStation, String destinationStation, String reservationUpto, String boardingPoint, String journeyClass, String numberOfpassenger, String chartStatus, String bookingFare, String quota, ArrayList<Object> arrInformationMessage, ArrayList<PassengerList_Structure> arrPassengerList) {
        this.isSuccess = isSuccess;
        this.errorMessage = "";
       this.pnrNumber = pnrNumber;
        this.dateOfJourney = dateOfJourney;
        this.trainStartDate = trainStartDate;
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.reservationUpto = reservationUpto;
        this.boardingPoint = boardingPoint;
        this.journeyClass = journeyClass;
        this.numberOfpassenger = numberOfpassenger;
        this.chartStatus = chartStatus;
        this.bookingFare = bookingFare;
        this.quota = quota;
        this.arrInformationMessage = arrInformationMessage;
        this.arrPassengerList = arrPassengerList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public String getTrainStartDate() {
        return trainStartDate;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public String getReservationUpto() {
        return reservationUpto;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public String getJourneyClass() {
        return journeyClass;
    }

    public String getNumberOfpassenger() {
        return numberOfpassenger;
    }

    public String getChartStatus() {
        return chartStatus;
    }

    public String getBookingFare() {
        return bookingFare;
    }

    public String getQuota() {
        return quota;
    }

    public ArrayList<Object> getArrInformationMessage() {
        return arrInformationMessage;
    }

    public ArrayList<PassengerList_Structure> getArrPassengerList() {
        return arrPassengerList;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }
}


