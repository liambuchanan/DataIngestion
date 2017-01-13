package com.plotwatt.data_ingestion;

import java.time.Instant;

/**
 * Created by liam on 1/13/17.
 */
public class Reading {
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
        return "Reading(" +
                "meterId: " + this.meterId + ", " +
                "intervalStart: " + this.intervalStart + ", " +
                "intervalEnd: " + this.intervalEnd + ", " +
                "value: " + this.value
                + ")";
    }
}
