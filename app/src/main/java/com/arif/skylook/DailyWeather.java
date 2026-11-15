package com.arif.skylook;

public class DailyWeather {
    private String date;
    private int weatherCode;
    private double maxTemp;
    private double minTemp;
    private String sunrise;
    private String sunset;
    private int precipitationProbabilityMax;

    public DailyWeather(String date, int weatherCode, double maxTemp, double minTemp, String sunrise, String sunset, int precipitationProbabilityMax) {
        this.date = date;
        this.weatherCode = weatherCode;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.precipitationProbabilityMax = precipitationProbabilityMax;
    }

    public String getDate() {
        return date;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public int getPrecipitationProbabilityMax() {
        return precipitationProbabilityMax;
    }
}
