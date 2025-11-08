package org.deveasy.jmx.metrics;

/**
 * Bootstrap to register registry and meter collection MBeans.
 * Invoked reflectively from core Observability to avoid hard dependency.
 */
public final class JmxMetricsBridge {
    private JmxMetricsBridge() {}

    public static void registerMBeans() {
        try { Registry.registerMBean(); } catch (Throwable ignored) {}
        try { Counters.registerMBean(); } catch (Throwable ignored) {}
        try { Timers.registerMBean(); } catch (Throwable ignored) {}
        try { Summaries.registerMBean(); } catch (Throwable ignored) {}
    }
}
