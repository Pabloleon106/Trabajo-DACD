package com.example.weatherapp;

public class Weather {
    private long ts;
    private String ss;
    private long predictionTime;
    private Location location;
    private double temperature;
    private double precipitation;
    private double humidity;
    private double clouds;
    private double windSpeed;

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public long getPredictionTime() {
        return predictionTime;
    }

    public void setPredictionTime(long predictionTime) {
        this.predictionTime = predictionTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    // Constructor, getters, and setters
    public Weather(long ts, String ss, long predictionTime, Location location, double temperature, double precipitation, double humidity, double clouds, double windSpeed) {
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.location = location;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
    }

    // Getters and setters...
}
