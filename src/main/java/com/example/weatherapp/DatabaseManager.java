package com.example.weatherapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String dbName) throws SQLException {
        String url = "jdbc:sqlite:" + dbName;
        connection = DriverManager.getConnection(url);
    }

    public void createTable(String tableName) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "date TEXT PRIMARY KEY," +
                "temperature REAL," +
                "precipitation REAL," +
                "humidity REAL," +
                "clouds REAL," +
                "wind_speed REAL)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.execute();
    }

    public void insertData(String tableName, String date, double temperature, double precipitation, double humidity, double clouds, double windSpeed) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (date, temperature, precipitation, humidity, clouds, wind_speed) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, date);
        stmt.setDouble(2, temperature);
        stmt.setDouble(3, precipitation);
        stmt.setDouble(4, humidity);
        stmt.setDouble(5, clouds);
        stmt.setDouble(6, windSpeed);
        stmt.execute();
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
