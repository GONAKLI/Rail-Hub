package com.gonakli.railradar.DB_MODAL;

public class Train_List_Modal_Class {
    private final String trainNumber;
    private final String trainName;

    public Train_List_Modal_Class(String trainNumber, String trainName){
        this.trainNumber = trainNumber;
                this.trainName = trainName;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

}
