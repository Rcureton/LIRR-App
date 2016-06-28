package com.example.mom.lirrapp.Weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Clouds {

    @SerializedName("all")
    @Expose
    private int all;

    /**
     * @return The all
     */
    public int getAll() {
        return all;
    }

    /**
     * @param all The all
     */
    public void setAll(int all) {
        this.all = all;
    }

}
