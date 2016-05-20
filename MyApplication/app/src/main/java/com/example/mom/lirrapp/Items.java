package com.example.mom.lirrapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MOM on 5/20/16.
 */
public class Items implements Parcelable {
    private static String MY_ITEMS= "myItems";
    private double lattitude;
    private double longitude;

    public Items(){

    }

    protected Items(Parcel in) {
        lattitude = in.readDouble();
        longitude = in.readDouble();
    }

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lattitude);
        dest.writeDouble(longitude);
    }
}
