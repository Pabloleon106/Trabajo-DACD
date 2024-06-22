package com.example.weatherapp;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherApp {
    private static final String API_KEY = "4d3a1982e9507ce81626f4eccc4e3bd3";
    private static final String[] ISLANDS = {"Tenerife", "Gran_Canaria", "Lanzarote", "Fuerteventura", "La_Palma", "La_Gomera", "El_Hierro", "La_Graciosa"};
    private static final double[][] ISLAND_COORDINATES = {
            {28.291565, -16.629129}, // Tenerife
            {28.150000, -15.416667}, // Gran Canaria
            {29.035000, -13.633000}, // Lanzarote
            {28.358743, -14.053676}, // Fuerteventura
            {28.673747, -17.789537}, // La Palma
            {28.116667, -17.216667}, // La Gomera
            {27.785969, -17.918650}, // El Hierro
            {29.2520, -13.5080}      // La Graciosa
    };

    public static void main(String[] args) {
        WeatherAPI weatherAPI = new WeatherAPI(API_KEY);
        try {
            DatabaseManager dbManager = new DatabaseManager("weather.db");
            for (String island : ISLANDS) {
                dbManager.createTable(island);
            }

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < ISLANDS.length; i++) {
                        try {
                            JSONObject weatherData = weatherAPI.getWeatherData(ISLAND_COORDINATES[i][0], ISLAND_COORDINATES[i][1]);
                            String date = "2024-06-22"; // Usa la lógica real para la fecha
                            double temperature = weatherData.getJSONObject("main").getDouble("temp");
                            double precipitation = weatherData.has("precipitation") ? weatherData.getJSONArray("weather").getJSONObject(0).getDouble("precipitation") : 0.0;
                            double humidity = weatherData.getJSONObject("main").getDouble("humidity");
                            double clouds = weatherData.getJSONObject("clouds").getDouble("all");
                            double windSpeed = weatherData.getJSONObject("wind").getDouble("speed");

                            dbManager.insertData(ISLANDS[i], date, temperature, precipitation, humidity, clouds, windSpeed);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 0, 6 * 60 * 60 * 1000); // Cada 6 horas

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

