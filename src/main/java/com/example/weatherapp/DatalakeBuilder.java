package com.example.weatherapp;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatalakeBuilder {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "sensor.data";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(TOPIC_NAME);

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        String text = ((TextMessage) message).getText();
                        Gson gson = new Gson();
                        SensorData data = gson.fromJson(text, SensorData.class);

                        String directory = String.format("datalake/eventstore/%s/%s/%s.events",
                                TOPIC_NAME, data.getSource(), getDateString(data.getTs()));
                        Files.createDirectories(Paths.get(directory).getParent());
                        try (FileWriter writer = new FileWriter(directory, true)) {
                            writer.write(text + "\n");
                        } catch (IOException e) {
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
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }));
    }

    private static String getDateString(long timestamp) {
        return new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date(timestamp));
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
