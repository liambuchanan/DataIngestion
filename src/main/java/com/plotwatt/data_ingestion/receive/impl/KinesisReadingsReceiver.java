package com.plotwatt.data_ingestion.receive.impl;

import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.receive.IReadingsReceiver;

/**
 * Created by liam on 1/13/17.
 */
public class KinesisReadingsReceiver implements IReadingsReceiver {
    @Override
    public void receive(Reading reading) {

    }

    public void receive(Iterable<Reading> readings) {

    }

}
