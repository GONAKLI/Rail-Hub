package com.gonakli.railHub.Structure_Class;

public class User_History_Structure {
   private final String trainNumber, trainName, trainSource, trainDestination;
   private final int dayCount;

    public User_History_Structure(String trainNumber, String trainName, String trainSource, String trainDestination, int dayCount) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.trainSource = trainSource;
        this.trainDestination = trainDestination;
        this.dayCount = dayCount;

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

    public int getDayCount() {
        return dayCount;
    }
}
