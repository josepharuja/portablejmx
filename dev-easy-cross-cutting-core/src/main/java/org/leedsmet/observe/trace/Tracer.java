package org.leedsmet.observe.trace;

/**
 * Tracer facade for creating spans and accessing the current context.
 */
public interface Tracer {
    SpanBuilder spanBuilder(String name);

    /** Returns a no-op current span if none is active. */
    Span currentSpan();
}
