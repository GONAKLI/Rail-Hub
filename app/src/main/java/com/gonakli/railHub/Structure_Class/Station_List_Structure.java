package com.gonakli.railHub.Structure_Class;

import androidx.annotation.NonNull;

public class Station_List_Structure {
    private final String station_Code;
    private final String station_Name;
    private String station_Lat;

    private String station_Lng;

    public Station_List_Structure(String station_Code, String station_Name, String station_Lat, String station_Lng) {
        this.station_Code = station_Code;
        this.station_Name = station_Name;
        this.station_Lat = station_Lat;
        this.station_Lng = station_Lng;
    }

    public Station_List_Structure(String station_Code, String station_Name) {
        this.station_Code = station_Code;
        this.station_Name = station_Name;
    }

    public String getStation_Code() {
        return station_Code;
    }

    public String getStation_Name() {
        return station_Name;
    }

    public String getStation_Lat() {
        return station_Lat;
    }

    public String getStation_Lng() {
        return station_Lng;
    }

    @NonNull
    @Override
    public String toString() {
        return station_Code + " -> " + station_Name + " \n";
    }
}
