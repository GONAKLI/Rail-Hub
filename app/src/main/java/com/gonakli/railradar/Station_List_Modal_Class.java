package com.gonakli.railradar;

public class Station_List_Modal_Class {
    private final String station_Code;
    private final String station_Name;

    public Station_List_Modal_Class(String station_Code, String station_Name ){
        this.station_Code = station_Code;
        this.station_Name = station_Name;
    }

    public String getStation_Code() {
        return station_Code;
    }

    public String getStation_Name() {
        return station_Name;
    }
}
