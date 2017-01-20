package com.plotwatt.data_ingestion.stage.producer.impl;

import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.stage.producer.IReadingsStageProducer;

/**
 * Created by liam on 1/13/17.
 */
public class RedisReadingsStageProducer implements IReadingsStageProducer {
    @Override
    public void send(Reading reading) {

    }

    @Override
    public void send(Iterable<Reading> readings) {

    }
}