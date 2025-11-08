package org.deveasy.jmx.metrics;

import org.deveasy.jmx.support.JmxPlatform;
import org.leedsmet.observe.Observability;

import javax.management.ObjectName;
import java.util.Map;

/**
 * Read-only timers collection MBean.
 * ObjectName: org.deveasy.obs:type=Timers
 */
public final class Timers implements TimersMBean {
    public static final String DOMAIN = "org.deveasy.obs";

    public static ObjectName objectName() {
        try {
            return new ObjectName(DOMAIN + ":type=Timers");
        } catch (Exception e) {
            throw new IllegalStateException("Invalid ObjectName for Timers", e);
        }
    }

    public static void registerMBean() {
        JmxPlatform.register(new Timers(), objectName(), JmxPlatform.RegistrationBehavior.REPLACE_EXISTING);
    }

    @Override
    public String[] names() {
        Map<String, long[]> snap = Observability.globalRegistry().timersSnapshot();
        return snap.keySet().toArray(new String[0]);
    }

    @Override
    public long[] getStats(String id) {
        long[] v = Observability.globalRegistry().timersSnapshot().get(id);
        return v == null ? new long[]{0L, 0L} : v;
    }

    @Override
    public String[] dump() {
        Map<String, long[]> snap = Observability.globalRegistry().timersSnapshot();
        String[] arr = new String[snap.size()];
        int i = 0;
        for (Map.Entry<String, long[]> e : snap.entrySet()) {
            long[] a = e.getValue();
            arr[i++] = e.getKey() + "=" + a[0] + "," + a[1];
        }
        return arr;
    }
}
