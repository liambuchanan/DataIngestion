package com.plotwatt.data_ingestion.receive.impl;

import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.receive.IReadingsReceiver;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.stream.StreamSupport;

/**
 * Created by liam on 1/13/17.
 */
public class KafkaReadingsReceiver implements IReadingsReceiver {
    private Producer<Integer, Reading> producer;
    public KafkaReadingsReceiver() {
        // hack in config for now
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "com.plotwatt.data_ingestion.serialize.KafkaReadingSerializer");
        this.producer = new KafkaProducer<Integer, Reading>(properties);
    }

    @Override
    public void receive(Reading reading) {
        this.producer.send(new ProducerRecord<Integer, Reading>("readings", reading.getMeterId(), reading));
    }

    @Override
    public void receive(Iterable<Reading> readings) {
        StreamSupport.stream(readings.spliterator(), false)
                .forEach(this::receive);
    }
}
