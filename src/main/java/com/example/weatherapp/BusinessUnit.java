package com.example.weatherapp;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BusinessUnit {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "sensor.data";
    private static final String DB_URL = "jdbc:sqlite:datamart.db";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        javax.jms.Connection jmsConnection = connectionFactory.createConnection();
        jmsConnection.start();

        Session session = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(TOPIC_NAME);

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        String text = ((TextMessage) message).getText();
                        Gson gson = new Gson();
                        SensorData data = gson.fromJson(text, SensorData.class);

                        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
                            String sql = "INSERT INTO sensor_data (ts, source, value) VALUES (?, ?, ?)";
                            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
                            pstmt.setLong(1, data.getTs());
                            pstmt.setString(2, data.getSource());
                            pstmt.setDouble(3, data.getValue());
                            pstmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                session.close();
                jmsConnection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }));
    }

    static class SensorData {
        private long ts;
        private String source;
        private double value;

        public long getTs() { return ts; }
        public String getSource() { return source; }
        public double getValue() { return value; }
    }
}
