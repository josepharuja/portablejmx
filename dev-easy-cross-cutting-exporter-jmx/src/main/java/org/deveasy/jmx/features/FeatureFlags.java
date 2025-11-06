package org.deveasy.jmx.features;

import org.deveasy.jmx.support.JmxPlatform;
import org.leedsmet.observe.Observability;

import javax.management.ObjectName;

/**
 * Basic runtime feature switches exposed via JMX. Currently toggles metrics on/off
 * by swapping the global MeterRegistry between Default and Noop implementations.
 *
 * ObjectName: org.deveasy.obs:type=Features
 */
public final class FeatureFlags implements FeatureFlagsMBean {
    private static final String DOMAIN = "org.deveasy.obs";
    private static final String TYPE = "Features";

    private volatile boolean obsEnabled = true;   // master gate (placeholder)
    private volatile boolean metricsEnabled = true;
    private volatile boolean traceEnabled = false; // placeholder for future

    public FeatureFlags() {
        // initialize according to current Observability (best-effort)
        // We don't have read APIs yet; keep defaults and let setter drive behavior.
    }

    public static ObjectName objectName() {
        try {
            return new ObjectName(DOMAIN + ":type=" + TYPE);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid ObjectName for FeatureFlags", e);
        }
    }

    /** Register the FeatureFlags MBean using the platform server. */
    public static void registerMBean() {
        JmxPlatform.register(new FeatureFlags(), objectName(), JmxPlatform.RegistrationBehavior.REPLACE_EXISTING);
    }

    // --- MBean methods ---
    @Override
    public boolean isObsEnabled() {
        return obsEnabled;
    }

    @Override
    public void setObsEnabled(boolean enabled) {
        this.obsEnabled = enabled;
        // For now, master switch delegates only to metrics state when turning off.
        if (!enabled) {
            setMetricsEnabled(false);
        }
    }

    @Override
    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    @Override
    public void setMetricsEnabled(boolean enabled) {
        this.metricsEnabled = enabled;
        if (enabled) {
            Observability.enableMetrics();
        } else {
            Observability.disableMetrics();
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return traceEnabled;
    }

    @Override
    public void setTraceEnabled(boolean enabled) {
        this.traceEnabled = enabled;
        // Tracer not implemented yet; placeholder for future wiring.
    }
}
