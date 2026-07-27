package com.gonakli.railradar.Structure_Class;

import androidx.annotation.NonNull;

public class Train_List_Structure {
    private final String trainNumber;
    private final String trainName;
    private final String trainType;

    public Train_List_Structure(String trainNumber, String trainName, String trainType){
        this.trainNumber = trainNumber;
                this.trainName = trainName;
                this.trainType = trainType;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainType(){ return  trainType;}

    @NonNull
    @Override
    public String toString() {
        return getTrainNumber() + getTrainName() + getTrainType();
    }
}
