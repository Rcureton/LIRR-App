
package com.example.mom.lirrapp.LIRRScheduleData;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class LIRRSchedule {

    @SerializedName("START_STATION")
    @Expose
    private String STARTSTATION;
    @SerializedName("END_STATION")
    @Expose
    private String ENDSTATION;
    @SerializedName("TRIPS")
    @Expose
    private List<TRIP> TRIPS = new ArrayList<TRIP>();
    @SerializedName("NOTES")
    @Expose
    private List<Object> NOTES = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The STARTSTATION
     */
    public String getSTARTSTATION() {
        return STARTSTATION;
    }

    /**
     * 
     * @param STARTSTATION
     *     The START_STATION
     */
    public void setSTARTSTATION(String STARTSTATION) {
        this.STARTSTATION = STARTSTATION;
    }

    /**
     * 
     * @return
     *     The ENDSTATION
     */
    public String getENDSTATION() {
        return ENDSTATION;
    }

    /**
     * 
     * @param ENDSTATION
     *     The END_STATION
     */
    public void setENDSTATION(String ENDSTATION) {
        this.ENDSTATION = ENDSTATION;
    }

    /**
     * 
     * @return
     *     The TRIPS
     */
    public List<TRIP> getTRIPS() {
        return TRIPS;
    }

    /**
     * 
     * @param TRIPS
     *     The TRIPS
     */
    public void setTRIPS(List<TRIP> TRIPS) {
        this.TRIPS = TRIPS;
    }

    /**
     * 
     * @return
     *     The NOTES
     */
    public List<Object> getNOTES() {
        return NOTES;
    }

    /**
     * 
     * @param NOTES
     *     The NOTES
     */
    public void setNOTES(List<Object> NOTES) {
        this.NOTES = NOTES;
    }

}
