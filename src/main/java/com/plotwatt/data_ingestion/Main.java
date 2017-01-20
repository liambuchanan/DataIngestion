package com.plotwatt.data_ingestion;

import com.plotwatt.data_ingestion.stage.consumer.IReadingsStageConsumer;
import com.plotwatt.data_ingestion.stage.consumer.KafkaReadingsStageConsumer;
import com.plotwatt.data_ingestion.stage.producer.IReadingsStageProducer;
import com.plotwatt.data_ingestion.stage.producer.ReadingsStageProducerFactory;

import java.time.Instant;
import java.util.stream.IntStream;

/**
 * Created by liam on 1/13/17.
 */
public class Main {
    public static void main(String[] args) {
        // IReadingsStageProducer producer = ReadingsStageProducerFactory.create("kafka");
        // IntStream.range(0, 10000)
        //         .mapToObj(i -> new Reading(
        //                 i/100,
        //                 Instant.ofEpochSecond(i%100*10),
        //                 Instant.ofEpochSecond((i%100+1)*10),
        //                 (float)Math.cos((double) i))
        //         )
        //         .forEach(r -> {
        //             producer.send(r);
        //             System.out.println(r);
        //         });
        IReadingsStageConsumer consumer = new KafkaReadingsStageConsumer();
        consumer.persistReadings();
    }
}
