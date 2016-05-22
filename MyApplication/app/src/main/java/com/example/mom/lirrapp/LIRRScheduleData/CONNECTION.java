
package com.example.mom.lirrapp.LIRRScheduleData;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CONNECTION {

    @SerializedName("CONNECTING_STATION")
    @Expose
    private String CONNECTINGSTATION;
    @SerializedName("FROM_TRAIN_ID")
    @Expose
    private String FROMTRAINID;
    @SerializedName("TO_TRAIN_ID")
    @Expose
    private String TOTRAINID;

    /**
     * 
     * @return
     *     The CONNECTINGSTATION
     */
    public String getCONNECTINGSTATION() {
        return CONNECTINGSTATION;
    }

    /**
     * 
     * @param CONNECTINGSTATION
     *     The CONNECTING_STATION
     */
    public void setCONNECTINGSTATION(String CONNECTINGSTATION) {
        this.CONNECTINGSTATION = CONNECTINGSTATION;
    }

    /**
     * 
     * @return
     *     The FROMTRAINID
     */
    public String getFROMTRAINID() {
        return FROMTRAINID;
    }

    /**
     * 
     * @param FROMTRAINID
     *     The FROM_TRAIN_ID
     */
    public void setFROMTRAINID(String FROMTRAINID) {
        this.FROMTRAINID = FROMTRAINID;
    }

    /**
     * 
     * @return
     *     The TOTRAINID
     */
    public String getTOTRAINID() {
        return TOTRAINID;
    }

    /**
     * 
     * @param TOTRAINID
     *     The TO_TRAIN_ID
     */
    public void setTOTRAINID(String TOTRAINID) {
        this.TOTRAINID = TOTRAINID;
    }

}
