package com.gonakli.railradar.Structure_Class;

import java.util.ArrayList;

public class Train_Schedule_Structure {
    private final String trainNumber, trainName, stationFrom, stationTo;
    private final String trainRunsOnMon, trainRunsOnTue, trainRunsOnWed, trainRunsOnThu, trainRunsOnFri, trainRunsOnSat, trainRunsOnSun;

    private final String duration;

    private  final ArrayList<Train_Schedule_Station_Structure> stationList;

    private  ArrayList<Track_Polyline_Point_Structure> polylinePoints;

    public Train_Schedule_Structure(String trainNumber, String trainName, String stationFrom, String stationTo, String trainRunsOnMon, String trainRunsOnTue, String trainRunsOnWed, String trainRunsOnThu, String trainRunsOnFri, String trainRunsOnSat, String trainRunsOnSun, String duration, ArrayList<Train_Schedule_Station_Structure> stationList) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.trainRunsOnMon = trainRunsOnMon;
        this.trainRunsOnTue = trainRunsOnTue;
        this.trainRunsOnWed = trainRunsOnWed;
        this.trainRunsOnThu = trainRunsOnThu;
        this.trainRunsOnFri = trainRunsOnFri;
        this.trainRunsOnSat = trainRunsOnSat;
        this.trainRunsOnSun = trainRunsOnSun;
        this.duration = duration;
        this.stationList = stationList;
    }
    public Train_Schedule_Structure(String trainNumber, String trainName, String stationFrom, String stationTo, String trainRunsOnMon, String trainRunsOnTue, String trainRunsOnWed, String trainRunsOnThu, String trainRunsOnFri, String trainRunsOnSat, String trainRunsOnSun, String duration, ArrayList<Train_Schedule_Station_Structure> stationList, ArrayList<Track_Polyline_Point_Structure> polylinePoints) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.stationFrom = stationFrom;
        this.stationTo = stationTo;
        this.trainRunsOnMon = trainRunsOnMon;
        this.trainRunsOnTue = trainRunsOnTue;
        this.trainRunsOnWed = trainRunsOnWed;
        this.trainRunsOnThu = trainRunsOnThu;
        this.trainRunsOnFri = trainRunsOnFri;
        this.trainRunsOnSat = trainRunsOnSat;
        this.trainRunsOnSun = trainRunsOnSun;
        this.duration = duration;
        this.stationList = stationList;
        this.polylinePoints = polylinePoints;
    }


    @Override
    public String toString() {
        return "Train_Schedule_Structure{" +
                "trainNumber='" + trainNumber + '\'' +
                ", trainName='" + trainName + '\'' +
                ", stationFrom='" + stationFrom + '\'' +
                ", stationTo='" + stationTo + '\'' +
                ", trainRunsOnMon='" + trainRunsOnMon + '\'' +
                ", trainRunsOnTue='" + trainRunsOnTue + '\'' +
                ", trainRunsOnWed='" + trainRunsOnWed + '\'' +
                ", trainRunsOnThu='" + trainRunsOnThu + '\'' +
                ", trainRunsOnFri='" + trainRunsOnFri + '\'' +
                ", trainRunsOnSat='" + trainRunsOnSat + '\'' +
                ", trainRunsOnSun='" + trainRunsOnSun + '\'' +
                ", duration='" + duration + '\'' +
                ", stationList=" + stationList +
                '}';
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getStationFrom() {
        return stationFrom;
    }

    public String getStationTo() {
        return stationTo;
    }

    public String getTrainRunsOnMon() {
        return trainRunsOnMon;
    }

    public String getTrainRunsOnTue() {
        return trainRunsOnTue;
    }

    public String getTrainRunsOnWed() {
        return trainRunsOnWed;
    }

    public String getTrainRunsOnThu() {
        return trainRunsOnThu;
    }

    public String getTrainRunsOnFri() {
        return trainRunsOnFri;
    }

    public String getTrainRunsOnSat() {
        return trainRunsOnSat;
    }

    public String getTrainRunsOnSun() {
        return trainRunsOnSun;
    }

    public String getDuration() {
        return duration;
    }

    public ArrayList<Train_Schedule_Station_Structure> getStationList() {
        return stationList;
    }
    public ArrayList<Track_Polyline_Point_Structure> getPolylinePoints() {
        return polylinePoints;
    }
}
