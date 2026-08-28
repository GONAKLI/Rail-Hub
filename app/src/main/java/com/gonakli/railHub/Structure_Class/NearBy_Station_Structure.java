package com.gonakli.railHub.Structure_Class;

public class NearBy_Station_Structure {
    private final String stationName;
    private final String stationCode;
    private final String stationLat;
    private final String stationLng;
    private final double stationDistance;

    public NearBy_Station_Structure(String stationName, String stationCode, String stationLat, String stationLng, double stationDistance) {
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

    @Override
    public String toString() {
        return "NearBy_Station_Structure{" +
                "stationName='" + stationName + '\'' +
                ", stationCode='" + stationCode + '\'' +
                ", stationLat='" + stationLat + '\'' +
                ", stationLng='" + stationLng + '\'' +
                ", stationDistance=" + stationDistance +
                '}';
    }

    public String getStationLng() {
        return stationLng;
    }

    public double getStationDistance() {
        return stationDistance;
    }
}
