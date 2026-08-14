package com.gonakli.railradar.Structure_Class;

import java.io.Serializable;
import java.util.ArrayList;

public class Pnr_Api_Response_Structure implements Serializable {
   private final String errorMessage;
   private final boolean isSuccess;

    private String dateOfJourney, trainStartDate, trainNumber, trainName,sourceStation, pnrNumber;
    private String destinationStation, reservationUpto,boardingPoint,journeyClass,numberOfPassenger;
    private String chartStatus,bookingFare,quota, ticketFare;
    private String bookingDate, ticketType;

    ArrayList<Object> arrInformationMessage;
    ArrayList<PassengerList_Structure> arrPassengerList;


    public Pnr_Api_Response_Structure(boolean isSuccess, String errorMessage) {
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;
    }

    public Pnr_Api_Response_Structure( boolean isSuccess,String pnrNumber, String dateOfJourney, String trainStartDate, String trainNumber, String trainName, String sourceStation, String destinationStation, String reservationUpto, String boardingPoint, String journeyClass, String numberOfPassenger, String chartStatus, String bookingFare, String quota, ArrayList<Object> arrInformationMessage, ArrayList<PassengerList_Structure> arrPassengerList, String ticketFare, String bookingDate, String ticketType) {
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
        this.numberOfPassenger = numberOfPassenger;
        this.chartStatus = chartStatus;
        this.bookingFare = bookingFare;
        this.quota = quota;
        this.arrInformationMessage = arrInformationMessage;
        this.arrPassengerList = arrPassengerList;
        this.ticketFare = ticketFare;
        this.bookingDate = bookingDate;
        this.ticketType = ticketType;
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

    public String getNumberOfPassenger() {
        return numberOfPassenger;
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

    public String getTicketFare() {
        return ticketFare;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setDateOfJourney(String dateOfJourney) {
        this.dateOfJourney = dateOfJourney;
    }

    public void setTrainStartDate(String trainStartDate) {
        this.trainStartDate = trainStartDate;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setSourceStation(String sourceStation) {
        this.sourceStation = sourceStation;
    }

    public void setPnrNumber(String pnrNumber) {
        this.pnrNumber = pnrNumber;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public void setReservationUpto(String reservationUpto) {
        this.reservationUpto = reservationUpto;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public void setJourneyClass(String journeyClass) {
        this.journeyClass = journeyClass;
    }

    public void setNumberOfPassenger(String numberOfPassenger) {
        this.numberOfPassenger = numberOfPassenger;
    }

    public void setChartStatus(String chartStatus) {
        this.chartStatus = chartStatus;
    }

    public void setBookingFare(String bookingFare) {
        this.bookingFare = bookingFare;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public void setTicketFare(String ticketFare) {
        this.ticketFare = ticketFare;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public void setArrInformationMessage(ArrayList<Object> arrInformationMessage) {
        this.arrInformationMessage = arrInformationMessage;
    }

    public void setArrPassengerList(ArrayList<PassengerList_Structure> arrPassengerList) {
        this.arrPassengerList = arrPassengerList;
    }
}


