package com.arif.skylook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
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
    private ProgressBar progressBarHourly;
    private LinearLayout hourlyContainer;

    private ProgressBar progressBarDaily;
    private LinearLayout dailyContainer;

    private LinearLayout cityHeader;
    private ImageButton btnEditCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCityName = findViewById(R.id.tvCityName);
        tvCountryName = findViewById(R.id.tvCountryName);
        cityHeader = findViewById(R.id.cityHeader);
        btnEditCity = findViewById(R.id.btnEditCity);
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
        progressBarHourly = findViewById(R.id.progressBarHourly);
        hourlyContainer = findViewById(R.id.hourlyContainer);
        progressBarDaily = findViewById(R.id.progressBarDaily);
        dailyContainer = findViewById(R.id.dailyContainer);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        City city = databaseHelper.getSelectedCity();

        if (city != null) {
            tvCityName.setText(city.getName());
            tvCountryName.setText(city.getCountry());
            loadWeather(city);
            loadHourlyWeather(city);
            loadDailyWeather(city);
        } else {
            progressBarWeather.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("No city selected");
        }

        View.OnClickListener editCityListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CitySelectionActivity.class);
                startActivity(intent);
            }
        };
        cityHeader.setOnClickListener(editCityListener);
        btnEditCity.setOnClickListener(editCityListener);
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

    private void loadHourlyWeather(City city) {
        progressBarHourly.setVisibility(View.VISIBLE);
        hourlyContainer.removeAllViews();

        String timezone = TimeZone.getDefault().getID();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<HourlyWeather> hourlyList = ApiClient.getHourlyWeather(city.getLatitude(), city.getLongitude(), timezone);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarHourly.setVisibility(View.GONE);
                        if (hourlyList != null && !hourlyList.isEmpty()) {
                            showHourlyWeather(hourlyList);
                        }
                    }
                });
            }
        }).start();
    }

    private void loadDailyWeather(City city) {
        progressBarDaily.setVisibility(View.VISIBLE);
        dailyContainer.removeAllViews();

        String timezone = TimeZone.getDefault().getID();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<DailyWeather> dailyList = ApiClient.getDailyWeather(city.getLatitude(), city.getLongitude(), timezone);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarDaily.setVisibility(View.GONE);
                        if (dailyList != null && !dailyList.isEmpty()) {
                            showDailyWeather(dailyList);
                        }
                    }
                });
            }
        }).start();
    }

    private void showDailyWeather(List<DailyWeather> dailyList) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (DailyWeather item : dailyList) {
            View view = inflater.inflate(R.layout.item_daily_weather, dailyContainer, false);

            TextView tvDailyDate = view.findViewById(R.id.tvDailyDate);
            TextView tvDailyCondition = view.findViewById(R.id.tvDailyCondition);
            TextView tvDailyTemp = view.findViewById(R.id.tvDailyTemp);
            TextView tvDailyRain = view.findViewById(R.id.tvDailyRain);
            TextView tvDailySun = view.findViewById(R.id.tvDailySun);

            tvDailyDate.setText(formatDate(item.getDate()));
            tvDailyCondition.setText(getWeatherCondition(item.getWeatherCode()));
            tvDailyTemp.setText(String.format("%.0f° / %.0f°", item.getMaxTemp(), item.getMinTemp()));
            tvDailyRain.setText("Rain: " + item.getPrecipitationProbabilityMax() + "%");
            tvDailySun.setText("Sunrise " + formatHour(item.getSunrise()) + " • Sunset " + formatHour(item.getSunset()));

            dailyContainer.addView(view);
        }
    }

    private String formatDate(String date) {
        try {
            String[] parts = date.split("-");
            if (parts.length == 3) {
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                if (month >= 1 && month <= 12) {
                    return months[month - 1] + " " + day;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
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

    private void showHourlyWeather(List<HourlyWeather> hourlyList) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (HourlyWeather item : hourlyList) {
            View view = inflater.inflate(R.layout.item_hourly_weather, hourlyContainer, false);

            TextView tvHourTime = view.findViewById(R.id.tvHourTime);
            TextView tvHourCondition = view.findViewById(R.id.tvHourCondition);
            TextView tvHourTemp = view.findViewById(R.id.tvHourTemp);
            TextView tvHourHumidity = view.findViewById(R.id.tvHourHumidity);
            TextView tvHourPrecip = view.findViewById(R.id.tvHourPrecip);

            tvHourTime.setText(formatHour(item.getTime()));
            tvHourCondition.setText(getWeatherCondition(item.getWeatherCode()));
            tvHourTemp.setText(String.format("%.1f°C", item.getTemperature()));
            tvHourHumidity.setText("Humidity: " + item.getHumidity() + "%");
            tvHourPrecip.setText("Rain: " + item.getPrecipitationProbability() + "%");

            hourlyContainer.addView(view);
        }
    }

    private String formatHour(String time) {
        if (time != null && time.contains("T")) {
            try {
                String[] parts = time.split("T");
                if (parts.length > 1) {
                    String timePart = parts[1];
                    String[] hm = timePart.split(":");
                    if (hm.length >= 2) {
                        int hour = Integer.parseInt(hm[0]);
                        int minute = Integer.parseInt(hm[1]);
                        String amPm = hour >= 12 ? "PM" : "AM";
                        int hour12 = hour % 12;
                        if (hour12 == 0) {
                            hour12 = 12;
                        }
                        return String.format("%d:%02d %s", hour12, minute, amPm);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time;
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
