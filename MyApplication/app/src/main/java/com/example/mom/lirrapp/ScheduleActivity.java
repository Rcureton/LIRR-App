package com.example.mom.lirrapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ScheduleActivity extends AppCompatActivity {

    AutoCompleteTextView mDepart, mArrive;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mDepart = (AutoCompleteTextView) findViewById(R.id.departFrom);
        mArrive = (AutoCompleteTextView) findViewById(R.id.arriveAt);
        mButton = (Button) findViewById(R.id.sendScheduleButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleAsyncTask scheduleAsyncTask = new ScheduleAsyncTask();
                scheduleAsyncTask.execute();

            }
        });


        String[] trainStations = getResources().getStringArray(R.array.lirr_stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, trainStations);

        mDepart.setThreshold(1);
        mDepart.setAdapter(adapter);
        mArrive.setThreshold(1);
        mArrive.setAdapter(adapter);
    }

    private class ScheduleAsyncTask extends AsyncTask<String, Void, String> {
        String departFrom;
        String arrivingStation;


        @Override
        protected void onPreExecute() {
            departFrom = mDepart.getText().toString();
            arrivingStation = mArrive.getText().toString();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder builder = HttpUrl.parse(Constants.LIRR_URL).newBuilder();
            builder.addQueryParameter("startsta", departFrom);
            builder.addQueryParameter("endsta", arrivingStation);
            String url = builder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, final Response response) throws IOException {
//                    // ... check for failure using `isSuccessful` before proceeding
//
//                    // Read data on the worker thread
//                    final String responseData = response.body().toString();
//                    responseText = responseData;
//
//                    Log.d("url", responseText+"hmm");
//
//                    // Run view-related code back on the main thread
////                    MainActivity.this.runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            TextView myTextView = (TextView) findViewById(R.id.responseText);
////                            myTextView.setText(responseData);
////                        }
////                    });
//                }
//            });
//
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView myTextView = (TextView) findViewById(R.id.responseText);
            myTextView.setText(s);
        }
    }
}
