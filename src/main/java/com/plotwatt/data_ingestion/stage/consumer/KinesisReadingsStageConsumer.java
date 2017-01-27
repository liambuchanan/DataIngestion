package com.plotwatt.data_ingestion.stage.consumer;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Created by liam on 1/13/17.
 */
public class KinesisReadingsStageConsumer implements IReadingsStageConsumer {
    @Override
    public void persistReadings() {
        persistReadingsForMeters(null);
    }

    @Override
    public void persistReadingsForMeters(Iterable<Integer> meterIds) {
        final KinesisClientLibConfiguration config;
        try {
            config = new KinesisClientLibConfiguration(
                    "readings_processors.plotwatt.com",
                    "readings",
                    DefaultAWSCredentialsProviderChain.getInstance(),
                    InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID()
            );
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        final Worker worker = new Worker.Builder()
                .recordProcessorFactory(new KinesisRecordProcessorFactory())
                .config(config)
                .build();
        Thread t = new Thread(worker);
        worker.shutdown();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class KinesisRecordProcessorFactory implements IRecordProcessorFactory {
        private final Iterable<Integer> watchedMeterIds;

        public KinesisRecordProcessorFactory(Iterable<Integer> watchedMeterIds) {
            this.watchedMeterIds = watchedMeterIds;
        }

        @Override
        public IRecordProcessor createProcessor() {
            return new KinesisRecordProcessor(watchedMeterIds);
        }
    }

    private class KinesisRecordProcessor implements IRecordProcessor {
        private final Iterable<Integer> watchedMeterIds;

        public KinesisRecordProcessor(Iterable<Integer> watchedMeterIds) {
            this.watchedMeterIds = watchedMeterIds;
        }

        @Override
        public void initialize(InitializationInput initializationInput) {

        }

        @Override
        public void processRecords(ProcessRecordsInput processRecordsInput) {

        }

        @Override
        public void shutdown(ShutdownInput shutdownInput) {

        }
    }
}
