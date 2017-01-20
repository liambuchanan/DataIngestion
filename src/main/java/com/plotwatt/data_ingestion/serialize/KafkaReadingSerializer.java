package com.plotwatt.data_ingestion.serialize;

import com.google.protobuf.Timestamp;
import com.plotwatt.data_ingestion.DataIngestionProtos;
import com.plotwatt.data_ingestion.Reading;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by liam on 1/13/17.
 */
public class KafkaReadingSerializer extends ReadingSerializer implements Serializer<Reading> {
    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Reading reading) {
        return this.toByteArray(reading);
    }
}
