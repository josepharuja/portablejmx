package org.deveasy.jmx.support;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Objects;

/**
 * Java 17-friendly JMX utilities with safe defaults.
 * - Always uses the in-process Platform MBeanServer (does not enable/assume remote JMX).
 * - Provides idempotent register/unregister helpers with replace/ignore/fail behaviors.
 */
public final class JmxPlatform {

    public enum RegistrationBehavior {
        FAIL_ON_EXISTING,
        IGNORE_EXISTING,
        REPLACE_EXISTING
    }

    private JmxPlatform() {}

    /**
     * Return the in-process Platform MBeanServer.
     */
    public static MBeanServer server() {
        return ManagementFactory.getPlatformMBeanServer();
    }

    /**
     * Register the given MBean with the provided name using the desired behavior.
     *
     * @return true if a registration happened (new or replaced), false if ignored.
     */
    public static boolean register(Object mbean, ObjectName name, RegistrationBehavior behavior) {
        Objects.requireNonNull(mbean, "mbean");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(behavior, "behavior");
        MBeanServer srv = server();
        try {
            if (srv.isRegistered(name)) {
                switch (behavior) {
                    case IGNORE_EXISTING:
                        return false;
                    case REPLACE_EXISTING:
                        srv.unregisterMBean(name);
                        break;
                    case FAIL_ON_EXISTING:
                    default:
                        throw new InstanceAlreadyExistsException("MBean already registered: " + name);
                }
            }
            srv.registerMBean(mbean, name);
            return true;
        } catch (InstanceAlreadyExistsException e) {
            throw new JmxRuntimeException("MBean exists: " + name, e);
        } catch (MBeanRegistrationException | NotCompliantMBeanException | InstanceNotFoundException e) {
            throw new JmxRuntimeException("Failed to register MBean: " + name, e);
        }
    }

    /**
     * Unregister the MBean if present; returns true if an MBean was actually unregistered.
     */
    public static boolean unregister(ObjectName name) {
        Objects.requireNonNull(name, "name");
        MBeanServer srv = server();
        try {
            if (!srv.isRegistered(name)) return false;
            srv.unregisterMBean(name);
            return true;
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            throw new JmxRuntimeException("Failed to unregister MBean: " + name, e);
        }
    }

    /** Simple unchecked wrapper for JMX errors. */
    public static class JmxRuntimeException extends RuntimeException {
        public JmxRuntimeException(String message, Throwable cause) { super(message, cause); }
    }
}
