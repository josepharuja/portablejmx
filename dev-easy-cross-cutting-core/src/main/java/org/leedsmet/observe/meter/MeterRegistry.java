package org.leedsmet.observe.meter;

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
}
