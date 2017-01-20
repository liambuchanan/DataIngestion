package com.plotwatt.data_ingestion.stage.consumer;


import com.plotwatt.data_ingestion.Reading;
import com.plotwatt.data_ingestion.persist.FileSystemReadingsPersister;
import com.plotwatt.data_ingestion.persist.IReadingsPersister;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.errors.WakeupException;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by liam on 1/13/17.
 */
public class KafkaReadingsStageConsumer implements IReadingsStageConsumer {
    @Override
    public void persistReadings() throws InterruptException {
        this.persistReadingsForMeters(null);
    }

    @Override
    public void persistReadingsForMeters(Iterable<Integer> watchedMeterIds) {
        final HashSet<Integer> watchedMeterIdsSet = new HashSet<>();
        for (Integer meterId : watchedMeterIds) {
            watchedMeterIdsSet.add(meterId);
        }
        persistReadingsForMeters(watchedMeterIdsSet);
    }

    private void persistReadingsForMeters(HashSet<Integer> watchedMeterIds) {
        final int numConsumers = 1; // Runtime.getRuntime().availableProcessors();
        final List<_KafkaReadingsConsumer> kafkaReadingsConsumers = new ArrayList<>();
        final ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
        final List<Future> futures = new ArrayList<>();
        for (int i = 0; i < numConsumers; i++) {
            _KafkaReadingsConsumer kafkaReadingsConsumer = new _KafkaReadingsConsumer(watchedMeterIds);
            kafkaReadingsConsumers.add(kafkaReadingsConsumer);
            futures.add(executor.submit(kafkaReadingsConsumer));
        }
        // TODO there is no restarting or alerting when threads die!!!
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (_KafkaReadingsConsumer kafkaReadingsConsumer : kafkaReadingsConsumers) {
                    kafkaReadingsConsumer.shutdown();
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for (Future future : futures) {
            // TODO resubmit? count errors in quick succession? poll all futures (don't wait on first)?
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    private class _KafkaReadingsConsumer implements Runnable {
        private final IReadingsPersister readingsPersister;
        private final KafkaConsumer<Integer, Reading> consumer;
        private final Map<Integer, SortedSet<Reading>> accumulatedMeterReadings;
        private final HashSet<Integer> watchedMeterIds;

        public _KafkaReadingsConsumer(HashSet<Integer> watchedMeterIds) {
            this.readingsPersister = new FileSystemReadingsPersister();
            Properties properties = new Properties();
            properties.put("bootstrap.servers", "localhost:9092");
            properties.put("group.id", "persist_readings");
            properties.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
            properties.put("value.deserializer", "com.plotwatt.data_ingestion.serialize.KafkaReadingDeserializer");
            properties.put("auto.offset.reset", "earliest");
            this.consumer = new KafkaConsumer<>(properties); // one thread per consumer
            this.accumulatedMeterReadings = new HashMap<>();
            this.watchedMeterIds = watchedMeterIds;
        }

        private void persistAccumulatedReadings(boolean persistAll) {
            Iterator<Map.Entry<Integer, SortedSet<Reading>>> iterator = this.accumulatedMeterReadings.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, SortedSet<Reading>> entry = iterator.next();
                SortedSet<Reading> readings = entry.getValue();
                if (persistAll
                        || readings.size() > 2 * 3600
                        || (readings.size() > 0
                        && readings.first().getIntervalStart().isBefore(Instant.now().minusSeconds(4 * 3600)))) {
                    iterator.remove();
                    this.readingsPersister.persistReadings(readings);
                }
            }
        }

        @Override
        public void run() throws RuntimeException {
            try {
                List<String> topics = new LinkedList<>();
                topics.add("readings");
                this.consumer.subscribe(topics);
                while (true) {
                    ConsumerRecords<Integer, Reading> records = this.consumer.poll(5 * 1000);
                    for (ConsumerRecord<Integer, Reading> record : records) {
                        if (this.watchedMeterIds == null || this.watchedMeterIds.contains(record.key())) {
                            if (!this.accumulatedMeterReadings.containsKey(record.key())) {
                                this.accumulatedMeterReadings.put(record.key(), new TreeSet<>());
                            }
                            this.accumulatedMeterReadings.get(record.key()).add(record.value());
                        }
                    }
                    this.persistAccumulatedReadings(true);
                }
            } catch (WakeupException e) {
                //ignore
                e.printStackTrace();
            } finally {
                this.consumer.close();
                this.persistAccumulatedReadings(true);
            }
        }

        public void shutdown() {
            this.consumer.wakeup();
        }
    }
}
