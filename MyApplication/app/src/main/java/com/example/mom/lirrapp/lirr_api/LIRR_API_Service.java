package com.example.mom.lirrapp.lirr_api;


import com.example.mom.lirrapp.LIRRScheduleData.LIRRScheduleClass;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nat on 7/7/16.
 */
public interface LIRR_API_Service {
    @GET("api/TrainTime")
    Observable<LIRRScheduleClass> getTrainSchedule(@Query("api_key")String apiKey,
                                                @Query("startsta")String startStation,
                                                   @Query("endsta")String endStation);
}
