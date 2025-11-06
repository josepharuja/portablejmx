package org.leedsmet.observe.internal;

import org.leedsmet.observe.trace.Span;
import org.leedsmet.observe.trace.SpanBuilder;
import org.leedsmet.observe.trace.Tracer;

/**
 * No-op tracer implementation.
 */
public final class NoopTracer implements Tracer {
    public static final NoopTracer INSTANCE = new NoopTracer();
    private static final NoopSpanBuilder BUILDER = new NoopSpanBuilder();
    private static final NoopSpan NOOP_SPAN = new NoopSpan();

    private NoopTracer() {}

    @Override
    public SpanBuilder spanBuilder(String name) { return BUILDER; }

    @Override
    public Span currentSpan() { return NOOP_SPAN; }

    private static final class NoopSpanBuilder implements SpanBuilder {
        @Override public SpanBuilder setParent(Span parent) { return this; }
        @Override public SpanBuilder setKind(Span.Kind kind) { return this; }
        @Override public SpanBuilder setAttribute(String key, String value) { return this; }
        @Override public Span startSpan() { return NOOP_SPAN; }
    }

    private static final class NoopSpan implements Span {
        @Override public String traceId() { return ""; }
        @Override public String spanId() { return ""; }
        @Override public String parentSpanId() { return ""; }
        @Override public Span setAttribute(String key, String value) { return this; }
        @Override public Span addEvent(String name) { return this; }
        @Override public void end() { /* no-op */ }
    }
}
