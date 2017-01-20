package com.plotwatt.data_ingestion.serialize;

import com.google.protobuf.InvalidProtocolBufferException;
import com.plotwatt.data_ingestion.Reading;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by liam on 1/20/17.
 */
public class KafkaReadingDeserializer implements Deserializer<Reading> {
    private ReadingDeserializer readingDeserializer = new ReadingDeserializer();
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public Reading deserialize(String topic, byte[] data) {
        try {
            return readingDeserializer.fromByteArray(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null; //TODO option type here
    }

    @Override
    public void close() {}
}
