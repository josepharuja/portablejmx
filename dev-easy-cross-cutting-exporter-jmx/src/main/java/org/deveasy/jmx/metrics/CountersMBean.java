package org.deveasy.jmx.metrics;

/**
 * Read-only counters view.
 */
public interface CountersMBean {
    /** Canonical IDs for counters: name{tags} */
    String[] names();
    /** Current value for the given canonical counter id (or 0 if absent). */
    double getValue(String id);
    /** Dump as id=value pairs for convenience. */
    String[] dump();
}
