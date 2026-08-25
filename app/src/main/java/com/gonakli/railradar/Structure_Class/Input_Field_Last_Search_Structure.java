package com.gonakli.railradar.Structure_Class;

public class Input_Field_Last_Search_Structure {
    private final String fromCode;
    private final String fromValue;
    private final String toCode;
    private final String toValue;

    public Input_Field_Last_Search_Structure(String fromCode, String fromValue, String toCode, String toValue) {
        this.fromCode = fromCode;
        this.fromValue = fromValue;
        this.toCode = toCode;
        this.toValue = toValue;
    }

    public String getFromCode() {
        return fromCode;
    }

    public String getFromValue() {
        return fromValue;
    }

    public String getToCode() {
        return toCode;
    }

    public String getToValue() {
        return toValue;
    }
}
