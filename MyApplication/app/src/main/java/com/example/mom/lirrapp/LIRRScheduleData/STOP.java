package com.example.mom.lirrapp.LIRRScheduleData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class STOP {

    @SerializedName("STATION")
    @Expose
    private String STATION;
    @SerializedName("TIME")
    @Expose
    private String TIME;

    /**
     * @return The STATION
     */
    public String getSTATION() {
        return STATION;
    }

    /**
     * @param STATION The STATION
     */
    public void setSTATION(String STATION) {
        this.STATION = STATION;
    }

    /**
     * @return The TIME
     */
    public String getTIME() {
        return TIME;
    }

    /**
     * @param TIME The TIME
     */
    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

}
