package com.plotwatt.data_ingestion.stage.producer;

import com.plotwatt.data_ingestion.stage.producer.impl.KafkaReadingsStageProducer;

/**
 * Created by liam on 1/13/17.
 */
public class ReadingsStageProducerFactory {
    public static IReadingsStageProducer create(String type) {
        IReadingsStageProducer readingsStageProducer;
        switch (type) {
            case "kafka":
                readingsStageProducer = new KafkaReadingsStageProducer();
                break;
            default:
                throw new IllegalArgumentException("Unrecognized type.");
        }
        return readingsStageProducer;
    }
}
