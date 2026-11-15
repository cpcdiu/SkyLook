package com.arif.skylook;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

    public static List<City> searchCities(String cityName) {
        List<City> cities = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            String query = URLEncoder.encode(cityName, "UTF-8");
            String apiUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + query + "&count=5&language=en&format=json";
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.has("results")) {
                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject item = results.getJSONObject(i);
                        String name = item.optString("name", "");
                        String country = item.optString("country", "");
                        double latitude = item.optDouble("latitude", 0);
                        double longitude = item.optDouble("longitude", 0);
                        cities.add(new City(name, country, latitude, longitude));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return cities;
    }

    public static List<HourlyWeather> getHourlyWeather(double latitude, double longitude, String timezone) {
        List<HourlyWeather> hourlyList = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            String tz = URLEncoder.encode(timezone, "UTF-8");
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,precipitation_probability,weather_code" + "&forecast_hours=24&timezone=" + tz;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.has("hourly")) {
                    JSONObject hourly = jsonObject.getJSONObject("hourly");
                    JSONArray times = hourly.optJSONArray("time");
                    JSONArray temps = hourly.optJSONArray("temperature_2m");
                    JSONArray humidities = hourly.optJSONArray("relative_humidity_2m");
                    JSONArray precipProbs = hourly.optJSONArray("precipitation_probability");
                    JSONArray codes = hourly.optJSONArray("weather_code");

                    if (times != null) {
                        for (int i = 0; i < times.length(); i++) {
                            String time = times.optString(i, "");
                            double temp = temps != null ? temps.optDouble(i, 0) : 0;
                            int humidity = humidities != null ? humidities.optInt(i, 0) : 0;
                            int precipProb = precipProbs != null ? precipProbs.optInt(i, 0) : 0;
                            int code = codes != null ? codes.optInt(i, 0) : 0;
                            hourlyList.add(new HourlyWeather(time, temp, humidity, precipProb, code));
    public static List<DailyWeather> getDailyWeather(double latitude, double longitude, String timezone) {
        List<DailyWeather> dailyList = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            String tz = URLEncoder.encode(timezone, "UTF-8");
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                    + "&longitude=" + longitude
                    + "&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max"
                    + "&forecast_days=5&timezone=" + tz;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.has("daily")) {
                    JSONObject daily = jsonObject.getJSONObject("daily");
                    JSONArray times = daily.optJSONArray("time");
                    JSONArray codes = daily.optJSONArray("weather_code");
                    JSONArray maxTemps = daily.optJSONArray("temperature_2m_max");
                    JSONArray minTemps = daily.optJSONArray("temperature_2m_min");
                    JSONArray sunrises = daily.optJSONArray("sunrise");
                    JSONArray sunsets = daily.optJSONArray("sunset");
                    JSONArray precipProbs = daily.optJSONArray("precipitation_probability_max");

                    if (times != null) {
                        for (int i = 0; i < times.length(); i++) {
                            String date = times.optString(i, "");
                            int code = codes != null ? codes.optInt(i, 0) : 0;
                            double maxTemp = maxTemps != null ? maxTemps.optDouble(i, 0) : 0;
                            double minTemp = minTemps != null ? minTemps.optDouble(i, 0) : 0;
                            String sunrise = sunrises != null ? sunrises.optString(i, "") : "";
                            String sunset = sunsets != null ? sunsets.optString(i, "") : "";
                            int precipProb = precipProbs != null ? precipProbs.optInt(i, 0) : 0;
                            dailyList.add(new DailyWeather(date, code, maxTemp, minTemp, sunrise, sunset, precipProb));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return dailyList;
    }
}
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return hourlyList;
    }

    public static CurrentWeather getCurrentWeather(double latitude, double longitude, String timezone) {
        HttpURLConnection connection = null;
        try {
            String tz = URLEncoder.encode(timezone, "UTF-8");
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,weather_code,wind_speed_10m" + "&timezone=" + tz;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.has("current")) {
                    JSONObject current = jsonObject.getJSONObject("current");
                    double temperature = current.optDouble("temperature_2m", 0);
                    int humidity = current.optInt("relative_humidity_2m", 0);
                    double apparentTemperature = current.optDouble("apparent_temperature", 0);
                    int isDay = current.optInt("is_day", 1);
                    double precipitation = current.optDouble("precipitation", 0);
                    int weatherCode = current.optInt("weather_code", 0);
                    double windSpeed = current.optDouble("wind_speed_10m", 0);
                    return new CurrentWeather(temperature, humidity, apparentTemperature, isDay, precipitation, weatherCode, windSpeed);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
