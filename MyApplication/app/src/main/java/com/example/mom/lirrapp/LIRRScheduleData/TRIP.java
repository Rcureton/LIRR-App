package com.example.mom.lirrapp.LIRRScheduleData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class TRIP {

    @SerializedName("ROUTE_DATE")
    @Expose
    private String ROUTEDATE;
    @SerializedName("DURATION")
    @Expose
    private String DURATION;
    @SerializedName("PEAK_INDICATOR")
    @Expose
    private int PEAKINDICATOR;
    @SerializedName("FARES")
    @Expose
    private com.example.mom.lirrapp.LIRRScheduleData.FARES FARES;
    @SerializedName("VIA_POINT")
    @Expose
    private Object VIAPOINT;
    @SerializedName("BUS_INDICATOR")
    @Expose
    private String BUSINDICATOR;
    @SerializedName("CONNECTIONS")
    @Expose
    private List<CONNECTION> CONNECTIONS = new ArrayList<CONNECTION>();
    @SerializedName("LEGS")
    @Expose
    private List<LEG> LEGS = new ArrayList<LEG>();
    @SerializedName("ALERTS")
    @Expose
    private List<String> ALERTS = new ArrayList<String>();

    /**
     * @return The ROUTEDATE
     */
    public String getROUTEDATE() {
        return ROUTEDATE;
    }

    /**
     * @param ROUTEDATE The ROUTE_DATE
     */
    public void setROUTEDATE(String ROUTEDATE) {
        this.ROUTEDATE = ROUTEDATE;
    }

    /**
     * @return The DURATION
     */
    public String getDURATION() {
        return DURATION;
    }

    /**
     * @param DURATION The DURATION
     */
    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    /**
     * @return The PEAKINDICATOR
     */
    public int getPEAKINDICATOR() {
        return PEAKINDICATOR;
    }

    /**
     * @param PEAKINDICATOR The PEAK_INDICATOR
     */
    public void setPEAKINDICATOR(int PEAKINDICATOR) {
        this.PEAKINDICATOR = PEAKINDICATOR;
    }

    /**
     * @return The FARES
     */
    public com.example.mom.lirrapp.LIRRScheduleData.FARES getFARES() {
        return FARES;
    }

    /**
     * @param FARES The FARES
     */
    public void setFARES(com.example.mom.lirrapp.LIRRScheduleData.FARES FARES) {
        this.FARES = FARES;
    }

    /**
     * @return The VIAPOINT
     */
    public Object getVIAPOINT() {
        return VIAPOINT;
    }

    /**
     * @param VIAPOINT The VIA_POINT
     */
    public void setVIAPOINT(Object VIAPOINT) {
        this.VIAPOINT = VIAPOINT;
    }

    /**
     * @return The BUSINDICATOR
     */
    public String getBUSINDICATOR() {
        return BUSINDICATOR;
    }

    /**
     * @param BUSINDICATOR The BUS_INDICATOR
     */
    public void setBUSINDICATOR(String BUSINDICATOR) {
        this.BUSINDICATOR = BUSINDICATOR;
    }

    /**
     * @return The CONNECTIONS
     */
    public List<CONNECTION> getCONNECTIONS() {
        return CONNECTIONS;
    }

    /**
     * @param CONNECTIONS The CONNECTIONS
     */
    public void setCONNECTIONS(List<CONNECTION> CONNECTIONS) {
        this.CONNECTIONS = CONNECTIONS;
    }

    /**
     * @return The LEGS
     */
    public List<LEG> getLEGS() {
        return LEGS;
    }

    /**
     * @param LEGS The LEGS
     */
    public void setLEGS(List<LEG> LEGS) {
        this.LEGS = LEGS;
    }

    /**
     * @return The ALERTS
     */
    public List<String> getALERTS() {
        return ALERTS;
    }

    /**
     * @param ALERTS The ALERTS
     */
    public void setALERTS(List<String> ALERTS) {
        this.ALERTS = ALERTS;
    }

}
