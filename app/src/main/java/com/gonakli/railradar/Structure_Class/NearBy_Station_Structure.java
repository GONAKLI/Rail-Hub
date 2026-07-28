package com.gonakli.railradar.Structure_Class;

public class NearBy_Station_Structure {
    private final String stationName;
    private final String stationCode;
    private final String stationLat;
    private final String stationLng;
    private final int stationDistance;

    public NearBy_Station_Structure(String stationName, String stationCode, String stationLat, String stationLng, int stationDistance) {
        this.stationName = stationName;
        this.stationCode = stationCode;
        this.stationLat = stationLat;
        this.stationLng = stationLng;
        this.stationDistance = stationDistance;
    }

    public String getStationName() {
        return stationName;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getStationLat() {
        return stationLat;
    }

    public String getStationLng() {
        return stationLng;
    }

    public int getStationDistance() {
        return stationDistance;
    }
}
