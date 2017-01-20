package com.plotwatt.data_ingestion.persist;

import com.plotwatt.data_ingestion.Reading;

/**
 * Created by liam on 1/17/17.
 */
public interface IReadingsPersister {
    public void persistReadings(Iterable<Reading> readings);
}
