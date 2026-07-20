package com.gonakli.railradar.Structure_Class;

import androidx.annotation.NonNull;

public class Train_List_Structure {
    private final String trainNumber;
    private final String trainName;

    public Train_List_Structure(String trainNumber, String trainName){
        this.trainNumber = trainNumber;
                this.trainName = trainName;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return getTrainNumber() + getTrainName();
    }
}
