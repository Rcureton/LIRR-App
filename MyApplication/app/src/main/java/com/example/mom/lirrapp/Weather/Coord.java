package com.example.mom.lirrapp.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Coord {

    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("lat")
    @Expose
    private double lat;

    /**
     * @return The lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * @param lon The lon
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * @return The lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

}
