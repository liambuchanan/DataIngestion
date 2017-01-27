package com.plotwatt.data_ingestion.stage.producer.impl;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.serialize.ReadingSerializer;
import com.plotwatt.data_ingestion.stage.producer.IReadingsStageProducer;

import java.nio.ByteBuffer;

/**
 * Created by liam on 1/13/17.
 */
public class KinesisReadingsStageProducer implements IReadingsStageProducer {
    private final KinesisProducer producer;
    private final ReadingSerializer serializer;

    public KinesisReadingsStageProducer() {
        producer = new KinesisProducer();
        serializer = new ReadingSerializer();
    }

    @Override
    public void send(Reading reading) {
        producer.addUserRecord(
                "readings",
                Integer.toString(reading.getMeterId()),
                ByteBuffer.wrap(serializer.toByteArray(reading))
        );
    }

    @Override
    public void send(Iterable<Reading> readings) {
        readings.forEach(this::send);
    }

}
