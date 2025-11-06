package org.leedsmet.observe.meter;

/**
 * Distribution summary for recording arbitrary samples (e.g., sizes).
 */
public interface DistributionSummary {
    void record(double amount);

    /** Number of recorded events (optional for no-op). */
    default long count() { return 0L; }

    /** Sum of recorded amounts (optional for no-op). */
    default double totalAmount() { return 0d; }
}
