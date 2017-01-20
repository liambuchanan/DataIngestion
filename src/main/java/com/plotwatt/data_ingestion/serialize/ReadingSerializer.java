package com.plotwatt.data_ingestion.serialize;

import com.google.protobuf.Timestamp;
import com.plotwatt.data_ingestion.DataIngestionProtos;
import com.plotwatt.data_ingestion.Reading;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liam on 1/20/17.
 */
public class ReadingSerializer {
    public DataIngestionProtos.Reading toReadingProto(Reading reading) {
        return DataIngestionProtos.Reading.newBuilder()
                .setMeterId(reading.getMeterId())
                .setIntervalStart(
                        Timestamp.newBuilder()
                                .setSeconds(reading.getIntervalStart().getEpochSecond())
                                .setNanos(reading.getIntervalStart().getNano())
                                .build()
                )
                .setIntervalEnd(
                        Timestamp.newBuilder()
                                .setSeconds(reading.getIntervalEnd().getEpochSecond())
                                .setNanos(reading.getIntervalEnd().getNano())
                                .build()
                )
                .setValue(reading.getValue())
                .build();
    }

    public byte[] toByteArray(Reading reading) {
        return toReadingProto(reading).toByteArray();
    }

    public void writeReadings(List<Reading> readings, OutputStream os) throws IOException {
        DataIngestionProtos.ReadingsList.newBuilder()
                .addAllReadings(readings.stream().map(this::toReadingProto).collect(Collectors.toList()))
                .build()
                .writeTo(os);
    }
}
