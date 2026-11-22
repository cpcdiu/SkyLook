package com.arif.skylook;

public class WeatherIconHelper {

    public static int getWeatherIconResource(int weatherCode) {
        if (weatherCode == 0) {
            return R.drawable.ic_weather_clear;
        } else if (weatherCode == 1) {
            return R.drawable.ic_weather_partly_cloudy;
        } else if (weatherCode == 2) {
            return R.drawable.ic_weather_partly_cloudy;
        } else if (weatherCode == 3) {
            return R.drawable.ic_weather_overcast;
        } else if (weatherCode == 45 || weatherCode == 48) {
            return R.drawable.ic_weather_foggy;
        } else if (weatherCode >= 51 && weatherCode <= 57) {
            return R.drawable.ic_weather_drizzle;
        } else if (weatherCode >= 61 && weatherCode <= 67) {
            return R.drawable.ic_weather_rain;
        } else if (weatherCode >= 71 && weatherCode <= 77) {
            return R.drawable.ic_weather_snow;
        } else if (weatherCode >= 80 && weatherCode <= 82) {
            return R.drawable.ic_weather_rain;
        } else if (weatherCode >= 85 && weatherCode <= 86) {
            return R.drawable.ic_weather_snow;
        } else if (weatherCode == 95) {
            return R.drawable.ic_weather_thunderstorm;
        } else if (weatherCode >= 96 && weatherCode <= 99) {
            return R.drawable.ic_weather_thunderstorm_hail;
        } else {
            return R.drawable.ic_weather_cloudy;
        }
    }
}