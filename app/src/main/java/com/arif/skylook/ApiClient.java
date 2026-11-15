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
}
