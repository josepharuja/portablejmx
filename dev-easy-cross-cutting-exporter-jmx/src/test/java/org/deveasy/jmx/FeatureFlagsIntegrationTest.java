package org.deveasy.jmx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leedsmet.observe.Observability;
import org.leedsmet.observe.meter.MeterRegistry;
import org.leedsmet.observe.meter.impl.DefaultMeterRegistry;
import org.leedsmet.observe.meter.impl.NoopMeterRegistry;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test to verify FeatureFlags MBean registration and runtime toggling of metrics,
 * as well as presence of registry/meter collection MBeans.
 */
public class FeatureFlagsIntegrationTest {

    private MBeanServer mbs;

    @BeforeEach
    void setUp() throws Exception {
        mbs = ManagementFactory.getPlatformMBeanServer();
        // Start with features so core attempts to register FeatureFlags and metrics MBeans via reflection
        Observability.startWithFeatures();
        // Ensure metrics are enabled to start
        Observability.enableMetrics();
    }

    @Test
    void featureFlagsMBeanIsRegistered() throws Exception {
        ObjectName name = new ObjectName("org.deveasy.obs:type=Features");
        // Give a tiny window for registration in case startWithFeatures was just called
        awaitRegistered(name, 1000L);
        assertTrue(mbs.isRegistered(name), "FeatureFlags MBean should be registered");
    }

    @Test
    void toggleMetricsViaJmxSwapsRegistry() throws Exception {
        ObjectName name = new ObjectName("org.deveasy.obs:type=Features");
        awaitRegistered(name, 1000L);

        MeterRegistry reg = Observability.globalRegistry();
        assertTrue(reg instanceof DefaultMeterRegistry, "Default registry expected before toggle");

        // Disable metrics via JMX
        mbs.setAttribute(name, new Attribute("MetricsEnabled", Boolean.FALSE));
        MeterRegistry afterDisable = Observability.globalRegistry();
        assertTrue(afterDisable instanceof NoopMeterRegistry, "Noop registry expected after disabling");

        // Enable metrics again via JMX
        mbs.setAttribute(name, new Attribute("MetricsEnabled", Boolean.TRUE));
        MeterRegistry afterEnable = Observability.globalRegistry();
        assertTrue(afterEnable instanceof DefaultMeterRegistry, "Default registry expected after enabling");
    }

    @Test
    void metricsMBeansAreRegistered() throws Exception {
        // Bridge registers these under org.deveasy.obs
        ObjectName reg = new ObjectName("org.deveasy.obs:type=Registry");
        ObjectName counters = new ObjectName("org.deveasy.obs:type=Counters");
        ObjectName timers = new ObjectName("org.deveasy.obs:type=Timers");
        ObjectName summaries = new ObjectName("org.deveasy.obs:type=Summaries");

        awaitRegistered(reg, 1000L);
        awaitRegistered(counters, 1000L);
        awaitRegistered(timers, 1000L);
        awaitRegistered(summaries, 1000L);

        assertTrue(mbs.isRegistered(reg));
        assertTrue(mbs.isRegistered(counters));
        assertTrue(mbs.isRegistered(timers));
        assertTrue(mbs.isRegistered(summaries));
    }

    private void awaitRegistered(ObjectName name, long timeoutMs) throws InterruptedException {
        long end = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < end) {
            if (mbs.isRegistered(name)) return;
            Thread.sleep(25L);
        }
    }
}
