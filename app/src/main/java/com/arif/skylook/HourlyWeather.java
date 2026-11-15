package com.arif.skylook;

public class HourlyWeather {
    private String time;
    private double temperature;
    private int humidity;
    private int precipitationProbability;
    private int weatherCode;

    public HourlyWeather(String time, double temperature, int humidity, int precipitationProbability, int weatherCode) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitationProbability = precipitationProbability;
        this.weatherCode = weatherCode;
    }

    public String getTime() {
        return time;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getPrecipitationProbability() {
        return precipitationProbability;
    }

    public int getWeatherCode() {
        return weatherCode;
    }
}
