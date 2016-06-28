package com.example.mom.lirrapp.LIRRScheduleData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class LEG {

    @SerializedName("TRAIN_ID")
    @Expose
    private String TRAINID;
    @SerializedName("BRANCH")
    @Expose
    private String BRANCH;
    @SerializedName("DEPART_TIME")
    @Expose
    private String DEPARTTIME;
    @SerializedName("DESTINATION")
    @Expose
    private String DESTINATION;
    @SerializedName("TRACK")
    @Expose
    private Object TRACK;
    @SerializedName("STATUS")
    @Expose
    private String STATUS;
    @SerializedName("ARRIVE_TIME")
    @Expose
    private String ARRIVETIME;
    @SerializedName("STOPS")
    @Expose
    private List<STOP> STOPS = new ArrayList<STOP>();
    @SerializedName("PUBLIC_CON_IND")
    @Expose
    private String PUBLICCONIND;

    /**
     * @return The TRAINID
     */
    public String getTRAINID() {
        return TRAINID;
    }

    /**
     * @param TRAINID The TRAIN_ID
     */
    public void setTRAINID(String TRAINID) {
        this.TRAINID = TRAINID;
    }

    /**
     * @return The BRANCH
     */
    public String getBRANCH() {
        return BRANCH;
    }

    /**
     * @param BRANCH The BRANCH
     */
    public void setBRANCH(String BRANCH) {
        this.BRANCH = BRANCH;
    }

    /**
     * @return The DEPARTTIME
     */
    public String getDEPARTTIME() {
        return DEPARTTIME;
    }

    /**
     * @param DEPARTTIME The DEPART_TIME
     */
    public void setDEPARTTIME(String DEPARTTIME) {
        this.DEPARTTIME = DEPARTTIME;
    }

    /**
     * @return The DESTINATION
     */
    public String getDESTINATION() {
        return DESTINATION;
    }

    /**
     * @param DESTINATION The DESTINATION
     */
    public void setDESTINATION(String DESTINATION) {
        this.DESTINATION = DESTINATION;
    }

    /**
     * @return The TRACK
     */
    public Object getTRACK() {
        return TRACK;
    }

    /**
     * @param TRACK The TRACK
     */
    public void setTRACK(Object TRACK) {
        this.TRACK = TRACK;
    }

    /**
     * @return The STATUS
     */
    public String getSTATUS() {
        return STATUS;
    }

    /**
     * @param STATUS The STATUS
     */
    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    /**
     * @return The ARRIVETIME
     */
    public String getARRIVETIME() {
        return ARRIVETIME;
    }

    /**
     * @param ARRIVETIME The ARRIVE_TIME
     */
    public void setARRIVETIME(String ARRIVETIME) {
        this.ARRIVETIME = ARRIVETIME;
    }

    /**
     * @return The STOPS
     */
    public List<STOP> getSTOPS() {
        return STOPS;
    }

    /**
     * @param STOPS The STOPS
     */
    public void setSTOPS(List<STOP> STOPS) {
        this.STOPS = STOPS;
    }

    /**
     * @return The PUBLICCONIND
     */
    public String getPUBLICCONIND() {
        return PUBLICCONIND;
    }

    /**
     * @param PUBLICCONIND The PUBLIC_CON_IND
     */
    public void setPUBLICCONIND(String PUBLICCONIND) {
        this.PUBLICCONIND = PUBLICCONIND;
    }

}
