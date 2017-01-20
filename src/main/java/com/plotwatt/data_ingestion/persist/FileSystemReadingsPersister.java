package com.plotwatt.data_ingestion.persist;

import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.serialize.ReadingDeserializer;
import com.plotwatt.data_ingestion.serialize.ReadingSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by liam on 1/18/17.
 */
public class FileSystemReadingsPersister implements IReadingsPersister {
    private final Path readingsDirectory;
    private final DateTimeFormatter readingsKeyDateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE.withZone(ZoneOffset.UTC);
    private final ReadingDeserializer readingDeserializer = new ReadingDeserializer();
    private final ReadingSerializer readingSerializer = new ReadingSerializer();

    public FileSystemReadingsPersister() {
        this(Paths.get(System.getProperty("user.home") + File.separator + "readings"));
    }

    public FileSystemReadingsPersister(Path readingsDirectory) {
        this.readingsDirectory = readingsDirectory;
        if (!Files.exists(this.readingsDirectory)) {
            try {
                Files.createDirectories(this.readingsDirectory);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void persistReadings(Iterable<Reading> readings) {
        StreamSupport.stream(readings.spliterator(), false)
                .collect(Collectors.groupingBy(this::getReadingsKey))
                .forEach(this::persistKeyReadings);
    }

    private List<Reading> weaveReadings(List<Reading> existingReadings, List<Reading> newReadings) {
        List<Reading> allReadings = new ArrayList<>();
        if (existingReadings != null) allReadings.addAll(existingReadings);
        if (newReadings != null) allReadings.addAll(newReadings);
        return allReadings;
    }

    private void persistKeyReadings(String key, List<Reading> readings) {
        Path keyPath = readingsDirectory.resolve(key);
        List<Reading> existingReadings = null, newReadings = null;
        if (Files.exists(keyPath)) {
            try {
                existingReadings = readingDeserializer.fromInputStream(Files.newInputStream(keyPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (existingReadings != null) {
            newReadings = weaveReadings(existingReadings, newReadings);
        } else {
            newReadings = readings;
        }
        try {
            readingSerializer.writeReadings(newReadings, Files.newOutputStream(keyPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getReadingsKey(Reading reading) {
        return String.format("%d,%s", reading.getMeterId(), readingsKeyDateTimeFormatter.format(reading.getIntervalStart()));
    }
}
