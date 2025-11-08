package org.deveasy.jmx.metrics;

/**
 * Read-only distribution summaries view.
 */
public interface SummariesMBean {
    /** Canonical IDs for summaries: name{tags} */
    String[] names();
    /** Returns [count, totalAmount] for the given summary id, or [0,0] if absent. */
    double[] getStats(String id);
    /** Dump as id=count,totalAmount pairs. */
    String[] dump();
}
