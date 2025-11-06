package org.deveasy.observe.config;

import java.util.Locale;
import java.util.Objects;

/**
 * Default FeatureGate that reads from system properties and environment variables.
 * Precedence: System properties > Environment variables > default value.
 *
 * Keys are expected in canonical dotted form for system properties
 * (e.g., "obs.metrics.enabled"), and in upper-snake for environment variables
 * (e.g., "OBS_METRICS_ENABLED").
 */
public final class ConfigBackedFeatureGate implements FeatureGate {

    @Override
    public boolean isEnabled(String featureKey, boolean defaultValue) {
        Objects.requireNonNull(featureKey, "featureKey");
        // 1) System properties
        String prop = System.getProperty(featureKey);
        if (prop != null) {
            return parseBoolean(prop.trim(), defaultValue);
        }
        // 2) Env vars
        String envKey = toEnvKey(featureKey);
        String env = System.getenv(envKey);
        if (env != null) {
            return parseBoolean(env.trim(), defaultValue);
        }
        // 3) Default
        return defaultValue;
    }

    private static boolean parseBoolean(String value, boolean defaultValue) {
        if (value.isEmpty()) return defaultValue;
        switch (value.toLowerCase(Locale.ROOT)) {
            case "1":
            case "true":
            case "yes":
            case "on":
            case "enabled":
                return true;
            case "0":
            case "false":
            case "no":
            case "off":
            case "disabled":
                return false;
            default:
                return defaultValue;
        }
    }

    private static String toEnvKey(String key) {
        StringBuilder sb = new StringBuilder(key.length());
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '.') sb.append('_');
            else sb.append(Character.toUpperCase(c));
        }
        return sb.toString();
    }
}
