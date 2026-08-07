package com.gonakli.railradar.Structure_Class;

public class User_History_Structure {
   private final String trainNumber, trainName, trainSource, trainDestination;

    public User_History_Structure(String trainNumber, String trainName, String trainSource, String trainDestination) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.trainSource = trainSource;
        this.trainDestination = trainDestination;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getTrainSource() {
        return trainSource;
    }

    public String getTrainDestination() {
        return trainDestination;
    }
}
