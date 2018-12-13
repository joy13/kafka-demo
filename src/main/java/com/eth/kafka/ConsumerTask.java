package com.eth.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerTask implements Runnable {
    public int consume() {
        System.out.println("Consumer task!");
        // Create a consumer
        KafkaConsumer<String, String> consumer;
        // Configure the consumer
        Properties properties = new Properties();
        // Point it to the brokers
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Set the consumer group (all consumers must belong to a group).
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "100");
        // Set how to serialize key/value pairs
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        // When a group is first created, it has no offset stored to start reading from. This tells it to start
        // with the earliest record in the stream.
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(properties);

        // Subscribe to the 'test' topic
        consumer.subscribe(Arrays.asList("test"));

        int count = 0;

        File f = new File("/Users/UdayanMac2013/msg.txt");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            // Poll for records
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(200));
            if (records.count() == 0) {
                // timeout/nothing to read
                System.out.println("!!! NO records !!!");
            } else {
                for (ConsumerRecord<String, String> record : records) {
                    count += 1;
                    System.out.println("Message No " + count + ": " + record.value());
                    try {
                        //Writing in file now, but should be stored in database.
                        bw.write(count + "," + record.value());
                        bw.newLine();
                        bw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public void run() {
        consume();
    }
}

