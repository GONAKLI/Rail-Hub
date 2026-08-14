package com.gonakli.railradar.Structure_Class;

import java.io.Serializable;

public class PassengerList_Structure implements Serializable {
    private String passengerSerialNumber, passengerAge,passengerBerthChoice,passengerNationality;
    private String bookingStatus, bookingCoachId,bookingBerthNo,bookingBerthCode,bookingStatusDetails;
    private String currentStatus,currentBerthNo,psgnwlType;
    private String currentStatusIndex,currentStatusDetails;

    public PassengerList_Structure(String passengerSerialNumber, String passengerAge, String passengerBerthChoice, String passengerNationality, String bookingStatus, String bookingCoachId, String bookingBerthNo, String bookingBerthCode, String bookingStatusDetails, String currentStatus, String currentBerthNo, String psgnwlType,String currentStatusIndex,String currentStatusDetails) {
        this.passengerSerialNumber = passengerSerialNumber;
        this.passengerAge = passengerAge;
        this.passengerBerthChoice = passengerBerthChoice;
        this.passengerNationality = passengerNationality;
        this.bookingStatus = bookingStatus;
        this.bookingCoachId = bookingCoachId;
        this.bookingBerthNo = bookingBerthNo;
        this.bookingBerthCode = bookingBerthCode;
        this.bookingStatusDetails = bookingStatusDetails;
        this.currentStatus = currentStatus;
        this.currentBerthNo = currentBerthNo;
        this.psgnwlType = psgnwlType;
        this.currentStatusIndex = currentStatusIndex;
        this.currentStatusDetails = currentStatusDetails;

    }

    public  PassengerList_Structure(){

    }

    public String getPassengerSerialNumber() {
        return passengerSerialNumber;
    }

    public String getPassengerAge() {
        return passengerAge;
    }

    public String getPassengerBerthChoice() {
        return passengerBerthChoice;
    }

    public String getPassengerNationality() {
        return passengerNationality;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public String getBookingCoachId() {
        return bookingCoachId;
    }

    public String getBookingBerthNo() {
        return bookingBerthNo;
    }

    public String getBookingBerthCode() {
        return bookingBerthCode;
    }

    public String getBookingStatusDetails() {
        return bookingStatusDetails;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getCurrentBerthNo() {
        return currentBerthNo;
    }

    public String getPsgnwlType() {
        return psgnwlType;
    }

    public String getCurrentStatusIndex() {
        return currentStatusIndex;
    }

    public String getCurrentStatusDetails() {
        return currentStatusDetails;
    }

    public void setPassengerSerialNumber(String passengerSerialNumber) {
        this.passengerSerialNumber = passengerSerialNumber;
    }

    public void setPassengerAge(String passengerAge) {
        this.passengerAge = passengerAge;
    }

    public void setPassengerBerthChoice(String passengerBerthChoice) {
        this.passengerBerthChoice = passengerBerthChoice;
    }

    public void setPassengerNationality(String passengerNationality) {
        this.passengerNationality = passengerNationality;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setBookingCoachId(String bookingCoachId) {
        this.bookingCoachId = bookingCoachId;
    }

    public void setBookingBerthNo(String bookingBerthNo) {
        this.bookingBerthNo = bookingBerthNo;
    }

    public void setBookingBerthCode(String bookingBerthCode) {
        this.bookingBerthCode = bookingBerthCode;
    }

    public void setBookingStatusDetails(String bookingStatusDetails) {
        this.bookingStatusDetails = bookingStatusDetails;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setCurrentBerthNo(String currentBerthNo) {
        this.currentBerthNo = currentBerthNo;
    }

    public void setPsgnwlType(String psgnwlType) {
        this.psgnwlType = psgnwlType;
    }

    public void setCurrentStatusIndex(String currentStatusIndex) {
        this.currentStatusIndex = currentStatusIndex;
    }

    public void setCurrentStatusDetails(String currentStatusDetails) {
        this.currentStatusDetails = currentStatusDetails;
    }
}
