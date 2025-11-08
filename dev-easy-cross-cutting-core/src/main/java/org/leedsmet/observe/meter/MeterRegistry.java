package org.leedsmet.observe.meter;

import java.util.Map;

/**
 * Minimal metrics registry interface. Implementations should be thread-safe.
 */
public interface MeterRegistry {
    Counter counter(String name);
    Counter counter(String name, Tags tags);

    Timer timer(String name);
    Timer timer(String name, Tags tags);

    DistributionSummary summary(String name);
    DistributionSummary summary(String name, Tags tags);

    // Read-only query API for JMX/exporters
    int totalMeters();
    int countersCount();
    int timersCount();
    int summariesCount();

    /** Snapshot of counters: key is canonical meter id (name{tags}), value is current count. */
    Map<String, Double> countersSnapshot();

    /** Snapshot of timers: value array is [count, totalNanos]. */
    Map<String, long[]> timersSnapshot();

    /** Snapshot of summaries: value array is [count, totalAmount] (both as double). */
    Map<String, double[]> summariesSnapshot();
}
