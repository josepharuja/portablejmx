package org.deveasy.jmx.metrics;

/**
 * High-level view of the global MeterRegistry.
 * Read-only counts for meter categories.
 */
public interface RegistryMBean {
    int getTotalMeters();
    int getCountersCount();
    int getTimersCount();
    int getSummariesCount();
}
