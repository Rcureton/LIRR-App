package com.example.mom.lirrapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class ScheduleActivity extends AppCompatActivity {

    AutoCompleteTextView mDepart, mArrive;
    private String LIRR_URL="https://traintime.lirr.org/api/TrainTime?api_key=da4e37b70fd7bbcf07f8a7202382523c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mDepart=(AutoCompleteTextView)findViewById(R.id.departFrom);
        mArrive=(AutoCompleteTextView)findViewById(R.id.arriveAt);


        String [] trainStations= getResources().getStringArray(R.array.lirr_stations);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,android.R.id.text1,trainStations);

        mDepart.setThreshold(1);
        mDepart.setAdapter(adapter);
        mArrive.setThreshold(1);
        mArrive.setAdapter(adapter);
    }

    private class ScheduleAsyncTask extends AsyncTask<String,Void,String>{
        String departFrom;
        String arrivingStation;


        @Override
        protected void onPreExecute() {
            departFrom=mDepart.getText().toString();
            arrivingStation=mArrive.getText().toString();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
