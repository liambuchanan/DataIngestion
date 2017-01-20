package com.plotwatt.data_ingestion.serialize;

import com.google.protobuf.InvalidProtocolBufferException;
import com.plotwatt.data_ingestion.DataIngestionProtos;
import com.plotwatt.data_ingestion.Reading;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liam on 1/20/17.
 */
public class ReadingDeserializer {

    public Reading fromByteArray(byte[] bytes) throws InvalidProtocolBufferException {
        return fromReadingProtoObj(DataIngestionProtos.Reading.parseFrom(bytes));
    }
    private Reading fromReadingProtoObj(DataIngestionProtos.Reading reading) {
        return new Reading(
                reading.getMeterId(),
                Instant.ofEpochSecond(reading.getIntervalStart().getSeconds(), reading.getIntervalStart().getNanos()),
                Instant.ofEpochSecond(reading.getIntervalEnd().getSeconds(), reading.getIntervalEnd().getNanos()),
                reading.getValue()
        );
    }

    public List<Reading> fromInputStream(InputStream in) throws IOException {
        return DataIngestionProtos.ReadingsList.parseFrom(in).getReadingsList().stream()
                .map(this::fromReadingProtoObj)
                .collect(Collectors.toList());
    }
}
