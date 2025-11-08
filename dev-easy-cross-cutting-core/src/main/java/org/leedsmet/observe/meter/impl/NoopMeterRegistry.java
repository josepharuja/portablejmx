package org.leedsmet.observe.meter.impl;

import org.leedsmet.observe.meter.*;

/**
 * No-op MeterRegistry used when metrics are disabled. Returns inert meters
 * that accept calls but do not record any data.
 */
public final class NoopMeterRegistry implements MeterRegistry {
    public static final NoopMeterRegistry INSTANCE = new NoopMeterRegistry();

    private static final Counter NOOP_COUNTER = new Counter() {
        @Override public void increment() { }
        @Override public void increment(double amount) { }
    };

    private static final Timer NOOP_TIMER = new Timer() {
        @Override public Sample start() { return NOOP_SAMPLE; }
        @Override public void record(long durationNanos) { }
        @Override public long count() { return 0L; }
        @Override public double totalTimeNanos() { return 0.0d; }
    };

    private static final Timer.Sample NOOP_SAMPLE = new Timer.Sample() {
        @Override public void stop() { }
    };

    private static final DistributionSummary NOOP_SUMMARY = new DistributionSummary() {
        @Override public void record(double amount) { }
        @Override public long count() { return 0L; }
        @Override public double totalAmount() { return 0.0d; }
    };

    private NoopMeterRegistry() {}

    @Override public int totalMeters() { return 0; }
    @Override public int countersCount() { return 0; }
    @Override public int timersCount() { return 0; }
    @Override public int summariesCount() { return 0; }

    @Override public java.util.Map<String, Double> countersSnapshot() { return java.util.Collections.emptyMap(); }
    @Override public java.util.Map<String, long[]> timersSnapshot() { return java.util.Collections.emptyMap(); }
    @Override public java.util.Map<String, double[]> summariesSnapshot() { return java.util.Collections.emptyMap(); }

    @Override public Counter counter(String name) { return NOOP_COUNTER; }
    @Override public Counter counter(String name, Tags tags) { return NOOP_COUNTER; }

    @Override public Timer timer(String name) { return NOOP_TIMER; }
    @Override public Timer timer(String name, Tags tags) { return NOOP_TIMER; }

    @Override public DistributionSummary summary(String name) { return NOOP_SUMMARY; }
    @Override public DistributionSummary summary(String name, Tags tags) { return NOOP_SUMMARY; }
}
