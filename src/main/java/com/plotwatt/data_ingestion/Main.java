package com.plotwatt.data_ingestion;

import com.plotwatt.data_ingestion.receive.IReadingsReceiver;
import com.plotwatt.data_ingestion.receive.ReadingsReceiverFactory;

import java.time.Instant;
import java.util.stream.IntStream;

/**
 * Created by liam on 1/13/17.
 */
public class Main {
    public static void main(String[] args) {
        IReadingsReceiver receiver = ReadingsReceiverFactory.createReadingsReceiver("KafkaReadingsReceiver");
        IntStream.range(0, 10000)
                .mapToObj(i -> new Reading(
                        i/100,
                        Instant.ofEpochSecond(i%100*10),
                        Instant.ofEpochSecond((i%100+1)*10),
                        (float)Math.cos((double) i))
                )
                .forEach(r -> {
                    receiver.receive(r);
                    System.out.println(r);
                });
    }
}
