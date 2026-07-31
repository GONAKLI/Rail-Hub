package com.gonakli.railradar.Structure_Class;

public class Train_Schedule_Station_Structure {
    private final String stationCode;
    private final String stationName;
    private final String arrivalTime;
    private final String departureTime;
    private final String haltTime;
    private final String distance;
    private final String dayCount;
    private final String stnSerialNumber;
    private final String stnLat;
    private final String stnLng;

    public Train_Schedule_Station_Structure(String stationCode, String stationName, String arrivalTime,
                                String departureTime, String haltTime, String distance,
                                String dayCount, String stnSerialNumber, String stnLat, String stnLng) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.haltTime = haltTime;
        this.distance = distance;
        this.dayCount = dayCount;
        this.stnSerialNumber = stnSerialNumber;
        this.stnLat = stnLat;
        this.stnLng = stnLng;

    }

    @Override
    public String toString() {
        return "Train_Schedule_Station_Structure{" +
                "stationCode='" + stationCode + '\'' +
                ", stationName='" + stationName + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", haltTime='" + haltTime + '\'' +
                ", distance='" + distance + '\'' +
                ", dayCount='" + dayCount + '\'' +
                ", stnSerialNumber='" + stnSerialNumber + '\'' +
                '}';
    }

    public String getStationCode() { return stationCode; }
    public String getStationName() { return stationName; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDepartureTime() { return departureTime; }
    public String getHaltTime() { return haltTime; }
    public String getDistance() { return distance; }
    public String getDayCount() { return dayCount; }

    public String getStnSerialNumber() { return stnSerialNumber; }
    public String getStnLat() {
        return stnLat;
    }

    public String getStnLng() {
        return stnLng;
    }
}