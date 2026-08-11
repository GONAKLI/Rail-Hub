package com.gonakli.railradar.Structure_Class;

public class Pnr_Api_Response_Structure {
   private final String errorMessage;
   private final boolean isSuccess;

   String trainNumber, trainName, sourceStation, destinationStation, reservationUpto;
   String boardingPoint, journeyClass, numberOfpassenger, chartStatus;

    public Pnr_Api_Response_Structure(boolean isSuccess, String errorMessage) {
        this.errorMessage = errorMessage;
        this.isSuccess = isSuccess;


    }


    public String getErrorMessage() {
        return errorMessage;
    }

//    public String getPnrNumber() {
//        return pnrNumber;
//    }
}
