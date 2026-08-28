package com.gonakli.railHub.Structure_Class;

import java.io.Serializable;

public class API_Response_Train_Tracking implements Serializable {
    private final String stationCode, platform, actualArrival, actualDeparture;

    public API_Response_Train_Tracking(String stationCode, String platform, String actualArrival, String actualDeparture) {
        this.stationCode = stationCode;
        this.platform = platform;
        this.actualArrival = actualArrival;
        this.actualDeparture = actualDeparture;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getPlatform() {
        return platform;
    }

    public String getActualArrival() {
        return actualArrival;
    }

    public String getActualDeparture() {
        return actualDeparture;
    }
}
