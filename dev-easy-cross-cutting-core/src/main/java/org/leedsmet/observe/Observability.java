package org.leedsmet.observe;

import org.deveasy.observe.config.ConfigBackedFeatureGate;
import org.deveasy.observe.config.FeatureGate;
import org.deveasy.observe.config.FeatureKeys;
import org.leedsmet.observe.meter.MeterRegistry;
import org.leedsmet.observe.meter.impl.DefaultMeterRegistry;
import org.leedsmet.observe.meter.impl.NoopMeterRegistry;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Global facade for observability features. Minimal MVP focused on metrics.
 * Tracing hooks are placeholders and will be introduced in a later phase.
 */
public final class Observability {
    private static final AtomicReference<MeterRegistry> REG = new AtomicReference<>(NoopMeterRegistry.INSTANCE);
    private static volatile FeatureGate featureGate = new ConfigBackedFeatureGate();

    private Observability() {}

    /** Returns the global meter registry (never null). */
    public static MeterRegistry globalRegistry() { return REG.get(); }

    /** Swap in a custom registry (advanced usage). */
    public static void setRegistry(MeterRegistry registry) {
        REG.set(Objects.requireNonNull(registry, "registry"));
    }

    /** Enable metrics by installing the default in-memory registry. */
    public static void enableMetrics() {
        REG.set(new DefaultMeterRegistry());
    }

    /** Disable metrics by swapping to a no-op registry. */
    public static void disableMetrics() {
        REG.set(NoopMeterRegistry.INSTANCE);
    }

    /** Initialize based on feature flags. Safe to call multiple times. */
    public static void startWithFeatures() {
        boolean enabled = featureGate.isEnabled(FeatureKeys.OBS_ENABLED, true);
        boolean metrics = enabled && featureGate.isEnabled(FeatureKeys.METRICS_ENABLED, true);
        if (metrics) enableMetrics(); else disableMetrics();
    }

    /** Allow plugging a different feature gate implementation. */
    public static void setFeatureGate(FeatureGate gate) {
        featureGate = Objects.requireNonNull(gate, "gate");
    }
}
