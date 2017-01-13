package com.plotwatt.data_ingestion.receive;
import com.plotwatt.data_ingestion.Reading;

/**
 * Created by liam on 1/13/17.
 */
public interface IReadingsReceiver {
    public void receive(Reading reading);
    public void receive(Iterable<Reading> readings);
}
