package org.deveasy.jmx.metrics;

/**
 * Read-only timers view.
 */
public interface TimersMBean {
    /** Canonical IDs for timers: name{tags} */
    String[] names();
    /** Returns [count, totalNanos] for the given timer id, or [0,0] if absent. */
    long[] getStats(String id);
    /** Dump as id=count,totalNanos pairs. */
    String[] dump();
}
