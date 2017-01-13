package com.plotwatt.data_ingestion.serialize;

import com.google.protobuf.Timestamp;
import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.ReadingProto;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by liam on 1/13/17.
 */
public class KafkaReadingSerializer implements Serializer<Reading> {
    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, Reading reading) {
        return ReadingProto.Reading.newBuilder()
                .setMeterId(reading.getMeterId())
                .setIntervalStart(
                        Timestamp.newBuilder()
                                .setSeconds(reading.getIntervalStart().getEpochSecond())
                                .setNanos(reading.getIntervalStart().getNano()).build()
                )
                .setIntervalEnd(
                        Timestamp.newBuilder()
                                .setSeconds(reading.getIntervalEnd().getEpochSecond())
                                .setNanos(reading.getIntervalEnd().getNano()).build()
                )
                .setValue(reading.getValue())
                .build()
                .toByteArray();
    }
}
