package org.deveasy.jmx.metrics;

import org.deveasy.jmx.support.JmxPlatform;
import org.leedsmet.observe.Observability;

import javax.management.ObjectName;
import java.util.Map;

/**
 * Read-only counters collection MBean.
 * ObjectName: org.deveasy.obs:type=Counters
 */
public final class Counters implements CountersMBean {
    public static final String DOMAIN = "org.deveasy.obs";

    public static ObjectName objectName() {
        try {
            return new ObjectName(DOMAIN + ":type=Counters");
        } catch (Exception e) {
            throw new IllegalStateException("Invalid ObjectName for Counters", e);
        }
    }

    public static void registerMBean() {
        JmxPlatform.register(new Counters(), objectName(), JmxPlatform.RegistrationBehavior.REPLACE_EXISTING);
    }

    @Override
    public String[] names() {
        Map<String, Double> snap = Observability.globalRegistry().countersSnapshot();
        return snap.keySet().toArray(new String[0]);
    }

    @Override
    public double getValue(String id) {
        Double v = Observability.globalRegistry().countersSnapshot().get(id);
        return v == null ? 0.0d : v;
    }

    @Override
    public String[] dump() {
        Map<String, Double> snap = Observability.globalRegistry().countersSnapshot();
        String[] arr = new String[snap.size()];
        int i = 0;
        for (Map.Entry<String, Double> e : snap.entrySet()) {
            arr[i++] = e.getKey() + "=" + e.getValue();
        }
        return arr;
    }
}
