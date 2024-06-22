package com.example.weatherapp;

import com.google.gson.Gson;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Timer;
import java.util.TimerTask;

public class NewSensor {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "sensor.data";
    private static final long FREQUENCY = 3600000; // Cada 1 hora

    public static void main(String[] args) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(TOPIC_NAME);

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        SensorData data = fetchDataFromNewSource();
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(data);
                        TextMessage message = session.createTextMessage(jsonData);
                        producer.send(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, FREQUENCY);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    session.close();
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private static SensorData fetchDataFromNewSource() {
        return new SensorData(System.currentTimeMillis(), "new-source", Math.random());
    }

    static class SensorData {
        private long ts;
        private String source;
        private double value;

        public SensorData(long ts, String source, double value) {
            this.ts = ts;
            this.source = source;
            this.value = value;
        }

        public long getTs() { return ts; }
        public String getSource() { return source; }
        public double getValue() { return value; }
    }
}

