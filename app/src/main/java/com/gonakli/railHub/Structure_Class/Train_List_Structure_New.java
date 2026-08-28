package com.gonakli.railHub.Structure_Class;

public class Train_List_Structure_New {
    private final String trainNumber, trainName, sourceStationName, destinationStationName;

    public Train_List_Structure_New(String trainNumber, String trainName, String sourceStationName, String destinationStationName) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.sourceStationName = sourceStationName;
        this.destinationStationName = destinationStationName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getDestinationStationName() {
        return destinationStationName;
    }
}
