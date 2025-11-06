package org.deveasy.observe.config;

/**
 * Canonical feature keys used by the observability library.
 */
public final class FeatureKeys {
    private FeatureKeys() {}

    // Master and core features
    public static final String OBS_ENABLED = "obs.enabled";
    public static final String METRICS_ENABLED = "obs.metrics.enabled";
    public static final String TRACE_ENABLED = "obs.trace.enabled";
    public static final String JVM_METRICS_ENABLED = "obs.jvm.enabled";

    // Exporters (individual gates)
    public static final String EXPORTER_PROMETHEUS = "obs.exporter.prometheus";
    public static final String EXPORTER_JMX = "obs.exporter.jmx";
    public static final String EXPORTER_OTLP = "obs.exporter.otlp";

    // Polling for dynamic updates
    public static final String FEATURES_POLL_INTERVAL_SEC = "obs.features.poll-interval-sec"; // default 0 = disabled
}
