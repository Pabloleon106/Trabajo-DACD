package com.example.weatherapp;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class WeatherAPI {
    private final String apiKey;

    public WeatherAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    public JSONObject getWeatherData(double lat, double lon) throws Exception {
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", lat, lon, apiKey);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                return new JSONObject(responseBody);
            }
        }
    }
}
