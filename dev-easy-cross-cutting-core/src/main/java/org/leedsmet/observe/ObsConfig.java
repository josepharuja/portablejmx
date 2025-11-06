package org.leedsmet.observe;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Lightweight configuration holder for Observability.
 * Sources precedence: System properties > Env vars > defaults.
 * Keys are documented in DESIGN.md; commonly used keys are exposed as constants here.
 */
public final class ObsConfig {
    public static final String KEY_ENABLED = "obs.enabled";
    public static final String KEY_SERVICE_NAME = "obs.service.name";
    public static final String KEY_EXPORTERS = "obs.metrics.exporter"; // comma-separated: prometheus,jmx,otlp
    public static final String KEY_PROM_PORT = "obs.prometheus.port";
    public static final String KEY_PROM_PATH = "obs.prometheus.path";
    public static final String KEY_JMX_DOMAIN = "obs.jmx.domain";
    public static final String KEY_COMMON_TAGS = "obs.tags.common"; // comma-separated key:value
    public static final String KEY_JVM_ENABLED = "obs.jvm.enabled";
    public static final String KEY_METRICS_ENABLED = "obs.metrics.enabled";
    public static final String KEY_TRACE_ENABLED = "obs.trace.enabled";

    private final Map<String, String> values;

    private ObsConfig(Map<String, String> values) {
        this.values = Collections.unmodifiableMap(new HashMap<>(values));
    }

    public static Builder builder() { return new Builder(); }

    public static ObsConfig loadDefaults() {
        return builder().build();
    }

    public String get(String key, String defaultValue) {
        String sys = System.getProperty(key);
        if (sys != null) return sys;
        String env = System.getenv(toEnvKey(key));
        if (env != null) return env;
        return values.getOrDefault(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String v = get(key, null);
        if (v == null) return defaultValue;
        return "1".equals(v) || "true".equalsIgnoreCase(v) || "yes".equalsIgnoreCase(v);
    }

    public int getInt(String key, int defaultValue) {
        String v = get(key, null);
        if (v == null) return defaultValue;
        try { return Integer.parseInt(v.trim()); } catch (NumberFormatException e) { return defaultValue; }
    }

    public Map<String, String> asMap() { return values; }

    private static String toEnvKey(String key) {
        return key.replace('.', '_').toUpperCase();
    }

    public static final class Builder {
        private final Map<String, String> values = new HashMap<>();

        public Builder set(String key, String value) {
            Objects.requireNonNull(key, "key");
            if (value == null) values.remove(key); else values.put(key, value);
            return this;
        }

        public Builder enabled(boolean enabled) { return set(KEY_ENABLED, String.valueOf(enabled)); }
        public Builder serviceName(String name) { return set(KEY_SERVICE_NAME, name); }
        public Builder exporters(String exporters) { return set(KEY_EXPORTERS, exporters); }
        public Builder prometheus(int port, String path) {
            set(KEY_PROM_PORT, String.valueOf(port));
            set(KEY_PROM_PATH, path);
            return this;
        }
        public Builder jmxDomain(String domain) { return set(KEY_JMX_DOMAIN, domain); }
        public Builder commonTags(String keyValuesCsv) { return set(KEY_COMMON_TAGS, keyValuesCsv); }

        public ObsConfig build() { return new ObsConfig(values); }
    }
}
