package org.leedsmet.observe.meter.impl;

import org.leedsmet.observe.meter.*;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

/**
 * A simple in-memory MeterRegistry suitable for early MVP use.
 * Thread-safe and low allocation, but not optimized for high-cardinality workloads yet.
 */
public final class DefaultMeterRegistry implements MeterRegistry {
    private final ConcurrentMap<Id, Counter> counters = new ConcurrentHashMap<>();
    private final ConcurrentMap<Id, Timer> timers = new ConcurrentHashMap<>();
    private final ConcurrentMap<Id, DistributionSummary> summaries = new ConcurrentHashMap<>();

    @Override
    public Counter counter(String name) { return counter(name, Tags.empty()); }

    @Override
    public Counter counter(String name, Tags tags) {
        Id id = new Id(name, tags);
        return counters.computeIfAbsent(id, k -> new SimpleCounter());
    }

    @Override
    public Timer timer(String name) { return timer(name, Tags.empty()); }

    @Override
    public Timer timer(String name, Tags tags) {
        Id id = new Id(name, tags);
        return timers.computeIfAbsent(id, k -> new SimpleTimer());
    }

    @Override
    public DistributionSummary summary(String name) { return summary(name, Tags.empty()); }

    @Override
    public DistributionSummary summary(String name, Tags tags) {
        Id id = new Id(name, tags);
        return summaries.computeIfAbsent(id, k -> new SimpleDistributionSummary());
    }

    // --- ID ---
    private static final class Id {
        final String name;
        final Tags tags;
        Id(String name, Tags tags) {
            this.name = Objects.requireNonNull(name, "name");
            this.tags = tags == null ? Tags.empty() : tags;
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;
            Id id = (Id) o;
            return name.equals(id.name) && tags.asMap().equals(id.tags.asMap());
        }
        @Override public int hashCode() { return 31 * name.hashCode() + tags.asMap().hashCode(); }
    }

    // --- Simple meters ---
    private static final class SimpleCounter implements Counter {
        private final DoubleAdder value = new DoubleAdder();
        @Override public void increment() { value.add(1.0); }
        @Override public void increment(double amount) {
            if (amount > 0) value.add(amount);
        }
        double get() { return value.sum(); }
    }

    private static final class SimpleTimer implements Timer {
        private final LongAdder count = new LongAdder();
        private final LongAdder totalNanos = new LongAdder();
        @Override public Sample start() { return new SampleImpl(this, System.nanoTime()); }
        @Override public void record(long durationNanos) {
            if (durationNanos <= 0) return;
            count.increment();
            totalNanos.add(durationNanos);
        }
        @Override public long count() { return count.sum(); }
        @Override public double totalTimeNanos() { return (double) totalNanos.sum(); }
        private static final class SampleImpl implements Sample {
            private final SimpleTimer timer; private final long start;
            private SampleImpl(SimpleTimer timer, long start) { this.timer = timer; this.start = start; }
            @Override public void stop() { timer.record(System.nanoTime() - start); }
        }
    }

    private static final class SimpleDistributionSummary implements DistributionSummary {
        private final LongAdder count = new LongAdder();
        private final DoubleAdder total = new DoubleAdder();
        @Override public void record(double amount) {
            if (amount <= 0) return;
            count.increment();
            total.add(amount);
        }
        @Override public long count() { return count.sum(); }
        @Override public double totalAmount() { return total.sum(); }
    }
}
