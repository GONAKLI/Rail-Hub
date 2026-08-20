package com.gonakli.railradar.Structure_Class;

public class Train_Tracking_Structure {
    private final Train_Schedule_Station_Structure previousStation, currentStation, nextStation;
    private  String statusMessage;
    private final boolean isOnRoute, isAtStation;
    private final int stationCoveredPercentage,totalJourneyCovered, totalJourneyCoveredPercentage, totalTrainJourney;

    private final String sourceStation, finalStation;

    public Train_Tracking_Structure(Train_Schedule_Station_Structure previousStation,
                                    Train_Schedule_Station_Structure currentStation,
                                    Train_Schedule_Station_Structure nextStation,
                                    String statusMessage, boolean isOnRoute,
                                    boolean isAtStation, int stationCoveredPercentage,
                                    int totalJourneyCovered,
                                    int totalJourneyCoveredPercentage,
                                    int totalTrainJourney, String sourceStation,
                                    String finalStation) {
        this.previousStation = previousStation;
        this.currentStation = currentStation;
        this.nextStation = nextStation;
        this.statusMessage = statusMessage;
        this.isOnRoute = isOnRoute;
        this.isAtStation = isAtStation;
        this.stationCoveredPercentage = stationCoveredPercentage;
        this.totalJourneyCovered = totalJourneyCovered;
        this.totalJourneyCoveredPercentage = totalJourneyCoveredPercentage;
        this.totalTrainJourney = totalTrainJourney;
        this.sourceStation = sourceStation;
        this.finalStation = finalStation;
    }

    public Train_Schedule_Station_Structure getPreviousStation() {
        return previousStation;
    }

    public Train_Schedule_Station_Structure getCurrentStation() {
        return currentStation;
    }

    public Train_Schedule_Station_Structure getNextStation() {
        return nextStation;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isOnRoute() {
        return isOnRoute;
    }

    public boolean isAtStation() {
        return isAtStation;
    }

    public int getStationCoveredPercentage() {
        return stationCoveredPercentage;
    }

    public int getTotalJourneyCovered() {
        return totalJourneyCovered;
    }

    public int getTotalJourneyCoveredPercentage() {
        return totalJourneyCoveredPercentage;
    }

    public int getTotalTrainJourney() {
        return totalTrainJourney;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getFinalStation() {
        return finalStation;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
