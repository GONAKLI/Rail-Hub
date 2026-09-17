package com.gonakli.railHub.Structure_Class;

public class Track_Polyline_Point_Structure {
    private final double lat;
    private final double lng;

    public Track_Polyline_Point_Structure(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}