package com.plotwatt.data_ingestion.stage.producer.impl;

import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.stage.producer.IReadingsStageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.stream.StreamSupport;

/**
 * Created by liam on 1/13/17.
 */
public class KafkaReadingsStageProducer implements IReadingsStageProducer {
    private Producer<Integer, Reading> producer;
    public KafkaReadingsStageProducer() {
        // TODO properly consider high importance configuration https://kafka.apache.org/documentation/#producerconfigs
        // hack in config for now
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put("value.serializer", "com.plotwatt.data_ingestion.serialize.KafkaReadingSerializer");
        producer = new KafkaProducer<>(properties);
    }

    @Override
    public void send(Reading reading) {
        producer.send(new ProducerRecord<>("readings", reading.getMeterId(), reading));
    }

    @Override
    public void send(Iterable<Reading> readings) {
        readings.forEach(this::send);
    }
}
