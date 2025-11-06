package org.leedsmet.observe.meter;

/**
 * Monotonic counter.
 */
public interface Counter {
    void increment();
    void increment(double amount);
}
