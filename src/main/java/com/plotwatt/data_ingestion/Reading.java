package com.plotwatt.data_ingestion;

import java.time.Instant;
import java.util.Comparator;

/**
 * Created by liam on 1/13/17.
 */
public class Reading implements Comparable<Reading> {
    private int meterId;
    private Instant intervalStart;
    private Instant intervalEnd;
    private float value;

    public Reading(int meterId, Instant intervalStart, Instant intervalEnd, float value) {
        this.meterId = meterId;
        this.intervalStart = intervalStart;
        this.intervalEnd = intervalEnd;
        this.value = value;
    }

    public int getMeterId() {
        return meterId;
    }

    public Instant getIntervalStart() {
        return intervalStart;
    }

    public Instant getIntervalEnd() {
        return intervalEnd;
    }

    public float getValue() {
        return value;
    }

    public String toString() {
        return String.format(
                "Reading(meterId: %d, intervalStart: %s, intervalEnd: %s, value: %s)",
                this.meterId, this.intervalStart, this.intervalEnd, this.value
        );
    }

    @Override
    public int compareTo(Reading reading) {
        // TODO is constructing a Comparator every time an efficient way to do this?
        return Comparator.comparing(Reading::getMeterId)
                .thenComparing(Reading::getIntervalStart)
                .thenComparing(Reading::getIntervalEnd)
                .compare(this, reading);
    }
}
