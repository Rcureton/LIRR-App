package com.example.mom.lirrapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MOM on 5/20/16.
 */
public class Items implements Parcelable {
    public final static String MY_ITEMS = "myItems";
    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
    private double latitude;
    private double longitude;

    public Items() {

    }

    protected Items(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
