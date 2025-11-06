package org.leedsmet.observe.internal;

import org.leedsmet.observe.meter.*;

/**
 * No-op implementation of MeterRegistry and meters. Safe to use when observability is disabled.
 */
public final class NoopMeterRegistry implements MeterRegistry {
    public static final NoopMeterRegistry INSTANCE = new NoopMeterRegistry();

    private NoopMeterRegistry() {}

    @Override
    public Counter counter(String name) { return NoopCounter.INSTANCE; }

    @Override
    public Counter counter(String name, Tags tags) { return NoopCounter.INSTANCE; }

    @Override
    public Timer timer(String name) { return NoopTimer.INSTANCE; }

    @Override
    public Timer timer(String name, Tags tags) { return NoopTimer.INSTANCE; }

    @Override
    public DistributionSummary summary(String name) { return NoopDistributionSummary.INSTANCE; }

    @Override
    public DistributionSummary summary(String name, Tags tags) { return NoopDistributionSummary.INSTANCE; }

    // --- Noop meter singletons ---
    public static final class NoopCounter implements Counter {
        static final NoopCounter INSTANCE = new NoopCounter();
        private NoopCounter() {}
        public void increment() {}
        public void increment(double amount) {}
    }

    public static final class NoopTimer implements Timer {
        static final NoopTimer INSTANCE = new NoopTimer();
        private NoopTimer() {}
        public Sample start() { return NoopSample.INSTANCE; }
        public void record(long durationNanos) {}
        private static final class NoopSample implements Sample {
            static final NoopSample INSTANCE = new NoopSample();
            public void stop() {}
        }
    }

    public static final class NoopDistributionSummary implements DistributionSummary {
        static final NoopDistributionSummary INSTANCE = new NoopDistributionSummary();
        private NoopDistributionSummary() {}
        public void record(double amount) {}
    }
}
