package org.deveasy.jmx.features;

/**
 * JMX MBean interface for flipping observability features at runtime.
 * Implementations should delegate to the core Observability facade.
 */
public interface FeatureFlagsMBean {
    // Master
    boolean isObsEnabled();
    void setObsEnabled(boolean enabled);

    // Metrics
    boolean isMetricsEnabled();
    void setMetricsEnabled(boolean enabled);

    // Tracing (placeholder for future)
    boolean isTraceEnabled();
    void setTraceEnabled(boolean enabled);
}
