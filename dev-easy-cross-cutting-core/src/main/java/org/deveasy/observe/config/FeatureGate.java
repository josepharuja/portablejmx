package org.deveasy.observe.config;

/**
 * Feature gate API for enabling/disabling observability features at runtime.
 * Implementations should be thread-safe.
 */
public interface FeatureGate {
    /**
     * Returns whether a feature is enabled.
     * @param featureKey canonical key, e.g. "obs.metrics.enabled"
     * @param defaultValue value to return when feature is not configured
     */
    boolean isEnabled(String featureKey, boolean defaultValue);

    /**
     * Optional listener to observe feature flag changes.
     */
    interface FeatureListener {
        void onChange(String featureKey, boolean enabled);
    }

    /**
     * Register a listener for the given feature key. Implementations may choose
     * to invoke the callback immediately with the current state.
     */
    default void addListener(String featureKey, FeatureListener listener) {
        // default: no-op
    }
}
