package com.arif.skylook;

public class CurrentWeather {
    private double temperature;
    private int humidity;
    private double apparentTemperature;
    private int isDay;
    private double precipitation;
    private int weatherCode;
    private double windSpeed;

    public CurrentWeather(double temperature, int humidity, double apparentTemperature, int isDay, double precipitation, int weatherCode, double windSpeed) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.apparentTemperature = apparentTemperature;
        this.isDay = isDay;
        this.precipitation = precipitation;
        this.weatherCode = weatherCode;
        this.windSpeed = windSpeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getApparentTemperature() {
        return apparentTemperature;
    }

    public int getIsDay() {
        return isDay;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
}
