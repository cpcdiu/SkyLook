package com.arif.skylook;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private TextView tvCityName;
    private TextView tvCountryName;
    private ProgressBar progressBarWeather;
    private TextView tvError;
    private LinearLayout weatherContainer;
    private TextView tvTemperature;
    private TextView tvWeatherCondition;
    private TextView tvFeelsLike;
    private TextView tvHumidity;
    private TextView tvWind;
    private TextView tvPrecipitation;
    private TextView tvDayNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCityName = findViewById(R.id.tvCityName);
        tvCountryName = findViewById(R.id.tvCountryName);
        progressBarWeather = findViewById(R.id.progressBarWeather);
        tvError = findViewById(R.id.tvError);
        weatherContainer = findViewById(R.id.weatherContainer);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvWeatherCondition = findViewById(R.id.tvWeatherCondition);
        tvFeelsLike = findViewById(R.id.tvFeelsLike);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);
        tvPrecipitation = findViewById(R.id.tvPrecipitation);
        tvDayNight = findViewById(R.id.tvDayNight);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        City city = databaseHelper.getSelectedCity();

        if (city != null) {
            tvCityName.setText(city.getName());
            tvCountryName.setText(city.getCountry());
            loadWeather(city);
        } else {
            progressBarWeather.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("No city selected");
        }
    }

    private void loadWeather(City city) {
        progressBarWeather.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        weatherContainer.setVisibility(View.GONE);

        String timezone = TimeZone.getDefault().getID();

        new Thread(new Runnable() {
            @Override
            public void run() {
                CurrentWeather weather = ApiClient.getCurrentWeather(city.getLatitude(), city.getLongitude(), timezone);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarWeather.setVisibility(View.GONE);
                        if (weather != null) {
                            showWeather(weather);
                        } else {
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText("Failed to load weather. Please try again.");
                        }
                    }
                });
            }
        }).start();
    }

    private void showWeather(CurrentWeather weather) {
        weatherContainer.setVisibility(View.VISIBLE);

        tvTemperature.setText(String.format("%.1f°C", weather.getTemperature()));
        tvWeatherCondition.setText(getWeatherCondition(weather.getWeatherCode()));
        tvFeelsLike.setText(String.format("Feels like %.1f°C", weather.getApparentTemperature()));
        tvHumidity.setText(weather.getHumidity() + "%");
        tvWind.setText(String.format("%.1f km/h", weather.getWindSpeed()));
        tvPrecipitation.setText(String.format("%.1f mm", weather.getPrecipitation()));
        tvDayNight.setText(weather.getIsDay() == 1 ? "Day" : "Night");
    }

    private String getWeatherCondition(int code) {
        if (code == 0) {
            return "Clear sky";
        } else if (code == 1) {
            return "Mainly clear";
        } else if (code == 2) {
            return "Partly cloudy";
        } else if (code == 3) {
            return "Overcast";
        } else if (code == 45 || code == 48) {
            return "Foggy";
        } else if (code == 51 || code == 53 || code == 55) {
            return "Drizzle";
        } else if (code == 56 || code == 57) {
            return "Freezing drizzle";
        } else if (code == 61 || code == 63 || code == 65) {
            return "Rain";
        } else if (code == 66 || code == 67) {
            return "Freezing rain";
        } else if (code == 71 || code == 73 || code == 75) {
            return "Snowfall";
        } else if (code == 77) {
            return "Snow grains";
        } else if (code == 80 || code == 81 || code == 82) {
            return "Rain showers";
        } else if (code == 85 || code == 86) {
            return "Snow showers";
        } else if (code == 95) {
            return "Thunderstorm";
        } else if (code == 96 || code == 99) {
            return "Thunderstorm with hail";
        } else {
            return "Unknown";
        }
    }
}
