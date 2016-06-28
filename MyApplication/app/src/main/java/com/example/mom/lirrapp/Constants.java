package com.example.mom.lirrapp;

import android.util.Base64;

import java.util.Arrays;

/**
 * Created by farazfazli on 6/28/16.
 */

public class Constants {
    private static String A = "ZGE0ZTM3YjcwZmQ3YmJjZjA3ZjhhNzIwMjM4MjUyM2M="; // Put this in an external Gradle file when you get a chance -- I named this 'A' because it's less obvious haha
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public static final String background_call = "http://openweathermap.org/img/w/";

    public static final String LIRR_URL = "https://traintime.lirr.org/api/TrainTime?api_key=" + Arrays.toString(Base64.decode(A, Base64.DEFAULT));
    public static final String SENDER_ID = String.valueOf(R.string.gcm_defaultSenderId);
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int UPDATE_INTERVAL = 10000;
    public static final int FASTEST_INTERVAL = 5000;
    public static final int DISPLACEMENT = 10;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
}
