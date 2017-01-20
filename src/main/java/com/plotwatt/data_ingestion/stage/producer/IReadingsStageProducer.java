package com.plotwatt.data_ingestion.stage.producer;
import com.plotwatt.data_ingestion.Reading;

/**
 * Created by liam on 1/13/17.
 */
public interface IReadingsStageProducer {
    public void send(Reading reading);
    public void send(Iterable<Reading> readings);
}
