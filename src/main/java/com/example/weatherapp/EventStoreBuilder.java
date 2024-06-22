package com.example.weatherapp;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EventStoreBuilder {
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("prediction.Weather");

        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        String text = ((TextMessage) message).getText();
                        Gson gson = new Gson();
                        Weather weather = gson.fromJson(text, Weather.class);

                        String directory = String.format("eventstore/prediction.Weather/%s/%s.events",
                                weather.getSs(), getDateString(weather.getTs()));
                        try {
                            Files.createDirectories(Paths.get(directory).getParent());
                            try (FileWriter writer = new FileWriter(directory, true)) {
                                writer.write(text + "\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Mantener el programa corriendo para escuchar los mensajes
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
}
