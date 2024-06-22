package com.example.weatherapp;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class WeatherPublisher {
    public static void sendWeatherData(Weather weather) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("prediction.Weather");

        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        Gson gson = new Gson();
        String weatherJson = gson.toJson(weather);
        TextMessage message = session.createTextMessage(weatherJson);

        producer.send(message);
        session.close();
        connection.close();
    }
}
