package org.deveasy.jmx.metrics;

import org.deveasy.jmx.support.JmxPlatform;
import org.leedsmet.observe.Observability;
import org.leedsmet.observe.meter.MeterRegistry;

import javax.management.ObjectName;

/**
 * Read-only MBean exposing high-level registry stats.
 * ObjectName: org.deveasy.obs:type=Registry
 */
public final class Registry implements RegistryMBean {
    public static final String DOMAIN = "org.deveasy.obs";

    public static ObjectName objectName() {
        try {
            return new ObjectName(DOMAIN + ":type=Registry");
        } catch (Exception e) {
            throw new IllegalStateException("Invalid ObjectName for Registry", e);
        }
    }

    public static void registerMBean() {
        JmxPlatform.register(new Registry(), objectName(), JmxPlatform.RegistrationBehavior.REPLACE_EXISTING);
    }

    private MeterRegistry reg() { return Observability.globalRegistry(); }

    @Override public int getTotalMeters() { return safe(reg().totalMeters()); }
    @Override public int getCountersCount() { return safe(reg().countersCount()); }
    @Override public int getTimersCount() { return safe(reg().timersCount()); }
    @Override public int getSummariesCount() { return safe(reg().summariesCount()); }

    private int safe(int v) { return Math.max(0, v); }
}
