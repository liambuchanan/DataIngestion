package com.plotwatt.data_ingestion.persist;

/**
 * Created by liam on 1/13/17.
 */
public interface IReadingsPersister {
    public void persistMeterReadings(Iterable<Integer> meter_ids);
}