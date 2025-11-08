package org.deveasy.jmx.metrics;

import org.deveasy.jmx.support.JmxPlatform;
import org.leedsmet.observe.Observability;

import javax.management.ObjectName;
import java.util.Map;

/**
 * Read-only distribution summaries collection MBean.
 * ObjectName: org.deveasy.obs:type=Summaries
 */
public final class Summaries implements SummariesMBean {
    public static final String DOMAIN = "org.deveasy.obs";

    public static ObjectName objectName() {
        try {
            return new ObjectName(DOMAIN + ":type=Summaries");
        } catch (Exception e) {
            throw new IllegalStateException("Invalid ObjectName for Summaries", e);
        }
    }

    public static void registerMBean() {
        JmxPlatform.register(new Summaries(), objectName(), JmxPlatform.RegistrationBehavior.REPLACE_EXISTING);
    }

    @Override
    public String[] names() {
        Map<String, double[]> snap = Observability.globalRegistry().summariesSnapshot();
        return snap.keySet().toArray(new String[0]);
    }

    @Override
    public double[] getStats(String id) {
        double[] v = Observability.globalRegistry().summariesSnapshot().get(id);
        return v == null ? new double[]{0d, 0d} : v;
    }

    @Override
    public String[] dump() {
        Map<String, double[]> snap = Observability.globalRegistry().summariesSnapshot();
        String[] arr = new String[snap.size()];
        int i = 0;
        for (Map.Entry<String, double[]> e : snap.entrySet()) {
            double[] a = e.getValue();
            arr[i++] = e.getKey() + "=" + (long)a[0] + "," + a[1];
        }
        return arr;
    }
}
