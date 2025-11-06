package org.leedsmet.observe.meter;

/**
 * Timer for recording durations.
 */
public interface Timer {
    /** Start a timing sample to be stopped later. */
    Sample start();

    /** Record a duration in nanoseconds. */
    void record(long durationNanos);

    /** Number of recorded events (optional for no-op). */
    default long count() { return 0L; }

    /** Total time in nanoseconds (optional for no-op). */
    default double totalTimeNanos() { return 0d; }

    interface Sample {
        /** Stop the timing sample and record the elapsed time. */
        void stop();
    }
}
