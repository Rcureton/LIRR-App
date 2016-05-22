
package com.example.mom.lirrapp.LIRRScheduleData;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class FARES {

    @SerializedName("ONE_WAY_DISABLED")
    @Expose
    private String ONEWAYDISABLED;
    @SerializedName("ONE_WAY_SENIOR")
    @Expose
    private String ONEWAYSENIOR;
    @SerializedName("ONE_WAY_OFF_PEAK")
    @Expose
    private String ONEWAYOFFPEAK;
    @SerializedName("ON-BOARD_ONE_WAY_OFF_PEAK")
    @Expose
    private String ONBOARDONEWAYOFFPEAK;
    @SerializedName("TEN_TRIP_SENIOR/DISABLED/MEDICARE")
    @Expose
    private String TENTRIPSENIORDISABLEDMEDICARE;
    @SerializedName("TEN_TRIP_OFF_PEAK")
    @Expose
    private String TENTRIPOFFPEAK;
    @SerializedName("WEEKLY")
    @Expose
    private String WEEKLY;
    @SerializedName("MONTHLY")
    @Expose
    private String MONTHLY;

    /**
     * 
     * @return
     *     The ONEWAYDISABLED
     */
    public String getONEWAYDISABLED() {
        return ONEWAYDISABLED;
    }

    /**
     * 
     * @param ONEWAYDISABLED
     *     The ONE_WAY_DISABLED
     */
    public void setONEWAYDISABLED(String ONEWAYDISABLED) {
        this.ONEWAYDISABLED = ONEWAYDISABLED;
    }

    /**
     * 
     * @return
     *     The ONEWAYSENIOR
     */
    public String getONEWAYSENIOR() {
        return ONEWAYSENIOR;
    }

    /**
     * 
     * @param ONEWAYSENIOR
     *     The ONE_WAY_SENIOR
     */
    public void setONEWAYSENIOR(String ONEWAYSENIOR) {
        this.ONEWAYSENIOR = ONEWAYSENIOR;
    }

    /**
     * 
     * @return
     *     The ONEWAYOFFPEAK
     */
    public String getONEWAYOFFPEAK() {
        return ONEWAYOFFPEAK;
    }

    /**
     * 
     * @param ONEWAYOFFPEAK
     *     The ONE_WAY_OFF_PEAK
     */
    public void setONEWAYOFFPEAK(String ONEWAYOFFPEAK) {
        this.ONEWAYOFFPEAK = ONEWAYOFFPEAK;
    }

    /**
     * 
     * @return
     *     The ONBOARDONEWAYOFFPEAK
     */
    public String getONBOARDONEWAYOFFPEAK() {
        return ONBOARDONEWAYOFFPEAK;
    }

    /**
     * 
     * @param ONBOARDONEWAYOFFPEAK
     *     The ON-BOARD_ONE_WAY_OFF_PEAK
     */
    public void setONBOARDONEWAYOFFPEAK(String ONBOARDONEWAYOFFPEAK) {
        this.ONBOARDONEWAYOFFPEAK = ONBOARDONEWAYOFFPEAK;
    }

    /**
     * 
     * @return
     *     The TENTRIPSENIORDISABLEDMEDICARE
     */
    public String getTENTRIPSENIORDISABLEDMEDICARE() {
        return TENTRIPSENIORDISABLEDMEDICARE;
    }

    /**
     * 
     * @param TENTRIPSENIORDISABLEDMEDICARE
     *     The TEN_TRIP_SENIOR/DISABLED/MEDICARE
     */
    public void setTENTRIPSENIORDISABLEDMEDICARE(String TENTRIPSENIORDISABLEDMEDICARE) {
        this.TENTRIPSENIORDISABLEDMEDICARE = TENTRIPSENIORDISABLEDMEDICARE;
    }

    /**
     * 
     * @return
     *     The TENTRIPOFFPEAK
     */
    public String getTENTRIPOFFPEAK() {
        return TENTRIPOFFPEAK;
    }

    /**
     * 
     * @param TENTRIPOFFPEAK
     *     The TEN_TRIP_OFF_PEAK
     */
    public void setTENTRIPOFFPEAK(String TENTRIPOFFPEAK) {
        this.TENTRIPOFFPEAK = TENTRIPOFFPEAK;
    }

    /**
     * 
     * @return
     *     The WEEKLY
     */
    public String getWEEKLY() {
        return WEEKLY;
    }

    /**
     * 
     * @param WEEKLY
     *     The WEEKLY
     */
    public void setWEEKLY(String WEEKLY) {
        this.WEEKLY = WEEKLY;
    }

    /**
     * 
     * @return
     *     The MONTHLY
     */
    public String getMONTHLY() {
        return MONTHLY;
    }

    /**
     * 
     * @param MONTHLY
     *     The MONTHLY
     */
    public void setMONTHLY(String MONTHLY) {
        this.MONTHLY = MONTHLY;
    }

}
