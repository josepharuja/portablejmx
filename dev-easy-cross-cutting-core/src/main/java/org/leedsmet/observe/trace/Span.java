package org.leedsmet.observe.trace;

import java.util.Map;

/**
 * Minimal span API for request traceability.
 */
public interface Span extends AutoCloseable {
    String traceId();
    String spanId();
    String parentSpanId();

    Span setAttribute(String key, String value);
    Span addEvent(String name);

    void end();

    @Override
    default void close() { end(); }

    enum Kind { INTERNAL, SERVER, CLIENT, PRODUCER, CONSUMER }
}
