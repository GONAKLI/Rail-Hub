package com.gonakli.railradar.Structure_Class;

import java.io.Serializable;

public class PassengerList_Structure implements Serializable {
    private final String passengerSerialNumber, passengerAge,passengerBerthChoice,passengerNationality;
    private final String bookingStatus, bookingCoachId,bookingBerthNo,bookingBerthCode,bookingStatusDetails;
    private final String currentStatus,currentBerthNo,psgnwlType;

    public PassengerList_Structure(String passengerSerialNumber, String passengerAge, String passengerBerthChoice, String passengerNationality, String bookingStatus, String bookingCoachId, String bookingBerthNo, String bookingBerthCode, String bookingStatusDetails, String currentStatus, String currentBerthNo, String psgnwlType) {
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
}
