package com.example.mom.lirrapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mom.lirrapp.Weather.WeatherConversion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.mom.lirrapp.Constants.BASE_URL;
import static com.example.mom.lirrapp.Constants.background_call;

public class WeatherActivity extends AppCompatActivity {

    // Please name things clearly, progressBar is much better than pb
    private ProgressBar progressBar;
    private TextView weatherText, mTemp, mTowntext;
    private String coordinates;
    private Items items;
    // Private whenever possible
    private double latitude;
    private double longitude;
    private String API_URL;
    private String wDescription;
    private String wTemp;
    private String mTown;
    private ImageView mBackground;
    private String final_image_Call;
    private String mWeatherIcon;
    private String weatherDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        items = intent.getParcelableExtra(Items.MY_ITEMS);

        String apiKey = getResources().getString(R.string.open_weather_api);

        longitude = items.getLongitude();
        latitude = items.getLatitude();
        coordinates = String.valueOf(latitude + ":" + longitude);

        mBackground = (ImageView) findViewById(R.id.background);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        weatherText = (TextView) findViewById(R.id.weatherText);
        mTemp = (TextView) findViewById(R.id.weatherTemp);
        mTowntext = (TextView) findViewById(R.id.townText);

        API_URL = BASE_URL + "lat=" + latitude + "&" + "lon=" + longitude + "&" + "appid=" + apiKey;

        WeatherAsync weatherAsync = new WeatherAsync();
        weatherAsync.execute();


    }

    private class WeatherAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(API_URL)
                        .build();
                Response response = null;
                response = client.newCall(request).execute();
                String result = response.body().string();
                JSONObject object = new JSONObject(result);
                JSONArray array = object.getJSONArray("weather");

                //TODO:Gets the weather description
                for (int i = 0; i < array.length(); i++) {
                    JSONObject weatherObject = array.getJSONObject(i);
                    weatherDesc = weatherObject.optString("description");
                    String weatherIconObject = weatherObject.optString("icon");
                    mWeatherIcon = weatherIconObject;
                    Log.d("WEATHER", weatherDesc);
                    wDescription = weatherDesc;
                }
                //TODO: Gets the temperature
                JSONObject main = object.getJSONObject("main");
                String temp = main.getString("temp");
                wTemp = temp;

                //TODO:Gets the town location
                mTown = object.getString("name");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            weatherText.setText(wDescription);
            mTemp.setText(String.format(Locale.ENGLISH, "%.2f", WeatherConversion.convertToFahrenheit(wTemp)));
            mTowntext.setText(mTown);
            final_image_Call = background_call + mWeatherIcon + ".png";
//            Picasso.with(WeatherActivity.this).load(final_image_Call).into(mBackground);

            mBackground.setImageResource(getImage(wDescription));


        }

        private int getImage(String image) {
            switch (image) {
                case "thunderstorm with light rain":
                    return R.drawable.thunderstorm;

                case "thunderstorm with heavy rain":
                    return R.drawable.thunderstorm;

                case "thunderstorm with rain":
                    return R.drawable.thunderstorm;
                case "thunderstorm":
                    return R.drawable.thunderstorm;
                case "light thunderstorm":
                    return R.drawable.thunderstorm;
                case "ragged thunderstorm":
                    return R.drawable.thunderstorm;
                case "thunderstorm with light drizzle":
                    return R.drawable.thunderstorm;
                case "thunderstorm with drizzle":
                    return R.drawable.thunderstorm;
                case "thunderstorm with heavy drizzle":
                    return R.drawable.thunderstorm;
                case "light intensity drizzle":
                    return R.drawable.drizzle;
                case "drizzle":
                    return R.drawable.drizzle;
                case "heavy intensity drizzle":
                    return R.drawable.drizzle;
                case "light intensity drizzle rain":
                    return R.drawable.drizzle;
                case "drizzle rain":
                    return R.drawable.drizzle;
                case "heavy intensity drizzle rain":
                    return R.drawable.drizzle;
                case "shower rain and drizzle":
                    return R.drawable.drizzle;
                case "heavy shower rain and drizzle":
                    return R.drawable.drizzle;
                case "shower drizzle":
                    return R.drawable.drizzle;
                case "light rain":
                    return R.drawable.light_rain;
                case "moderate rain":
                    return R.drawable.light_rain;
                case "heavy intensity rain":
                    return R.drawable.light_rain;
                case "very heavy rain":
                    return R.drawable.light_rain;
                case "extreme rain":
                    return R.drawable.light_rain;
                case "freezing rain":
                    return R.drawable.light_rain;
                case "light intensity shower rain":
                    return R.drawable.showers;
                case "shower rain":
                    return R.drawable.showers;
                case "heavy intensity shower rain":
                    return R.drawable.showers;
                case "ragged shower rain":
                    return R.drawable.showers;
                case "light snow":
                    return R.drawable.snow;
                case "snow":
                    return R.drawable.snow;
                case "heavy snow":
                    return R.drawable.snow;
                case "clear sky":
                    return R.drawable.sunny;
                case "few clouds":
                    return R.drawable.overcast;
                case "scattered clouds":
                    return R.drawable.overcast;
                case "broken clouds":
                    return R.drawable.overcast;
                case "overcast clouds":
                    return R.drawable.overcast;
                default:
                    return 0;

            }
        }
    }


}
