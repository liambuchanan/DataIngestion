package com.plotwatt.data_ingestion.stage.consumer;

/**
 * Created by liam on 1/13/17.
 */
public interface IReadingsStageConsumer {
    public void persistReadings();
    public void persistReadingsForMeters(Iterable<Integer> meterIds);
}