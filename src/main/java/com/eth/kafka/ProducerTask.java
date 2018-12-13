package com.eth.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class ProducerTask implements Runnable {

    public void produce() {

        // Set properties used to configure the kafka
        Properties properties = new Properties();
        // Set the brokers (bootstrap servers)
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        // Set how to serialize key/value pairs
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String message = "I am a Kafka message! Time: ";

        try {
            producer.send(new ProducerRecord<>("test", message + LocalTime.now())).get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        produce();
    }
}
