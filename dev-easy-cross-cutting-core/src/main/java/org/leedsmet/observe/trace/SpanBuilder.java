package org.leedsmet.observe.trace;

import org.leedsmet.observe.trace.Span.Kind;

/**
 * Builder for spans. Implementations should be cheap; building may capture current context.
 */
public interface SpanBuilder {
    SpanBuilder setParent(Span parent);
    SpanBuilder setKind(Kind kind);
    SpanBuilder setAttribute(String key, String value);

    Span startSpan();
}
