package com.example.mom.lirrapp.lirr_api;

import android.util.Base64;
import android.util.Log;

import com.example.mom.lirrapp.Constants;
import com.example.mom.lirrapp.LIRRScheduleData.LIRRScheduleClass;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nat on 7/7/16.
 */
public class LIRR_Manager
            implements Callback<LIRRScheduleClass> {

        private static final String TAG = LIRR_Manager.class.getSimpleName();
        private static LIRR_Manager mInstance;
        private LIRR_API_Service mLIRRApiService=null;

        private LIRR_Manager(){

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//added this once started using RxJava
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://traintime.lirr.org/")
                    .client(client.build())
                    .build();




            mLIRRApiService = retrofit.create(LIRR_API_Service.class);
        }



        public static LIRR_Manager getInstance(){
            if(mInstance==null){
                mInstance = new LIRR_Manager();
            }
            return mInstance;
        }

        public Observable<LIRRScheduleClass> getTrainSchedule(String startStation,String endStation){
            return mLIRRApiService.getTrainSchedule(Arrays.toString(Base64.decode(Constants.A, Base64.DEFAULT)),startStation,endStation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


        }
        @Override
        public void onResponse(Call<LIRRScheduleClass> call, Response<LIRRScheduleClass> response) {
                    Log.d(TAG,"inside onResponse");


        }

        @Override
        public void onFailure(Call<LIRRScheduleClass> call, Throwable t) {
            Log.d(TAG,"onFailure");
        }


    }

