package org.leedsmet.studentresearch.jmx.support;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.core.Constants;

/**
 * Provides supporting infrastructure for registering MBeans with an
 * {@link javax.management.MBeanServer}. The behavior when encountering
 * an existing MBean at a given {@link ObjectName} is fully configurable
 * allowing for flexible registration settings.
 *
 * <p>All registered MBeans are tracked and can be unregistered by calling
 * the #{@link #unregisterBeans()} method.
 *
 * <p>Sub-classes can receive notifications when an MBean is registered or
 * unregistered by overriding the {@link #onRegister(ObjectName)} and
 * {@link #onUnregister(ObjectName)} methods respectively.
 *
 * <p>By default, the registration process will fail if attempting to
 * register an MBean using a {@link javax.management.ObjectName} that is
 * already used.
 *
 * <p>By setting the {@link #setRegistrationBehaviorName(String) registrationBehaviorName}
 * property to <code>REGISTRATION_IGNORE_EXISTING</code> the registration process
 * will simply ignore existing MBeans leaving them registered. This is useful in settings
 * where multiple applications want to share a common MBean in a shared {@link MBeanServer}.
 *
 *
 * @author Joseph Aruja
 * @see #setServer
 * @see #setRegistrationBehaviorName
 * @see org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter
 */
public class GenericMBeanRegistrationSupport {

	/**
	 * Constant indicating that registration should fail when
	 * attempting to register an MBean under a name that already exists.
	 * <p>This is the default registration behavior.
	 */
	public static final int REGISTRATION_FAIL_ON_EXISTING = 0;

	/**
	 * Constant indicating that registration should ignore the affected MBean
	 * when attempting to register an MBean under a name that already exists.
	 */
	public static final int REGISTRATION_IGNORE_EXISTING = 1;

	/**
	 * Constant indicating that registration should replace the affected MBean
	 * when attempting to register an MBean under a name that already exists.
	 */
	public static final int REGISTRATION_REPLACE_EXISTING = 2;


	/**
	 * Constants for this class.
	 */
	private static final Constants constants = new Constants(GenericMBeanRegistrationSupport.class);

	/**
	 * <code>Log</code> instance for this class.
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * The <code>MBeanServer</code> instance being used to register beans.
	 */
	protected MBeanServer server;

	/**
	 * The beans that have been registered by this exporter.
	 */
	protected Set<ObjectName> registeredMBeans = new HashSet<ObjectName>();

	/**
	 * The action take when registering an MBean and finding that it already exists.
	 * By default an exception is raised.
	 */
	private int registrationBehavior = REGISTRATION_FAIL_ON_EXISTING;


	/**
	 * Specify the <code>MBeanServer</code> instance with which all beans should
	 * be registered. The <code>MBeanExporter</code> will attempt to locate an
	 * existing <code>MBeanServer</code> if none is supplied.
	 */
	public void setServer(MBeanServer server) {
		this.server = server;
	}

	/**
	 * Set the registration behavior by the name of the corresponding constant,
	 * e.g. "REGISTRATION_IGNORE_EXISTING".
	 * @see #setRegistrationBehavior
	 * @see #REGISTRATION_FAIL_ON_EXISTING
	 * @see #REGISTRATION_IGNORE_EXISTING
	 * @see #REGISTRATION_REPLACE_EXISTING
	 */
	public void setRegistrationBehaviorName(String registrationBehavior) {
		setRegistrationBehavior(constants.asNumber(registrationBehavior).intValue());
	}

	/**
	 * Specify  what action should be taken when attempting to register an MBean
	 * under an {@link javax.management.ObjectName} that already exists.
	 * <p>Default is REGISTRATION_FAIL_ON_EXISTING.
	 * @see #setRegistrationBehaviorName(String)
	 * @see #REGISTRATION_FAIL_ON_EXISTING
	 * @see #REGISTRATION_IGNORE_EXISTING
	 * @see #REGISTRATION_REPLACE_EXISTING
	 */
	public void setRegistrationBehavior(int registrationBehavior) {
		this.registrationBehavior = registrationBehavior;
	}


	/**
	 * Actually register the MBean with the server. The behavior when encountering
	 * an existing MBean can be configured using the {@link #setRegistrationBehavior(int)}
	 * and {@link #setRegistrationBehaviorName(String)} methods.
	 * @throws JMException if the registration failed
	 */
	protected void doRegister(Object mbean, ObjectName objectName) throws JMException {
		
		
		ObjectInstance registeredBean = null;
		try 
		{
			logger.info("doRegister start");
			
			registeredBean = this.server.registerMBean(mbean, objectName);
			
			logger.info("doRegister end 1");
		}
		catch (InstanceAlreadyExistsException ex) 
		{
			if (this.registrationBehavior == REGISTRATION_IGNORE_EXISTING) 
			{
				if (logger.isDebugEnabled()) {
					logger.debug("Ignoring existing MBean at [" + objectName + "]");
				}
			}
			else if (this.registrationBehavior == REGISTRATION_REPLACE_EXISTING) 
			{
				try 
				{
					if (logger.isDebugEnabled()) 
					{
						logger.debug("Replacing existing MBean at [" + objectName + "]");
					}
					this.server.unregisterMBean(objectName);
					registeredBean = this.server.registerMBean(mbean, objectName);
					logger.info("doRegister end 2");
				}
				catch (InstanceNotFoundException ex2) {
					logger.error("Unable to replace existing MBean at [" + objectName + "]", ex2);
					throw ex;
				}
			}
			else 
			{
				throw ex;
			}
		}

		// Track registration and notify listeners.
		ObjectName actualObjectName = (registeredBean != null ? registeredBean.getObjectName() : null);
		if (actualObjectName == null) {
			actualObjectName = objectName;
		}
		this.registeredMBeans.add(actualObjectName);
		logger.info("Adding the MBean :"+actualObjectName +" to the set of registeredMBeans");
		onRegister(actualObjectName);
	}

	/**
	 * Unregisters all beans that have been registered by an instance of this class.
	 */
	protected void unregisterMBeans() {
		logger.info("unregisterBeans start");
		for (Iterator<ObjectName> it = this.registeredMBeans.iterator(); it.hasNext();) {
			ObjectName objectName = it.next();
			try {
				// MBean might already have been unregistered by an external process.
				if (this.server.isRegistered(objectName)) {
					this.server.unregisterMBean(objectName);
					logger.info("unregistering MBean :"+objectName);
					onUnregister(objectName);
				}
				else {
					if (logger.isWarnEnabled()) {
						logger.warn("Could not unregister MBean [" + objectName + "] as said MBean " +
								"is not registered (perhaps already unregistered by an external process)");
					}
				}
			}
			catch (JMException ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Could not unregister MBean [" + objectName + "]", ex);
				}
			}
		}
		this.registeredMBeans.clear();
		logger.info("unregisterBeans end");
	}
	
	
	
	/**
	 * @param objectName
	 */
	protected void unregisterMBean(ObjectName objectName) 
	{
		logger.info("unregisterMBean start");

		try 
		{
			// MBean might already have been unregistered by an external
			// process.
			if (this.server.isRegistered(objectName)) 
			{
				this.server.unregisterMBean(objectName);
				logger.debug("unregistering MBean :" + objectName);
				onUnregister(objectName);
			} 
			else 
			{
				if (logger.isWarnEnabled()) {
					logger
							.warn("Could not unregister MBean ["
									+ objectName
									+ "] as said MBean "
									+ "is not registered (perhaps already unregistered by an external process)");
				}
			}
		} 
		catch (JMException ex) 
		{
			if (logger.isErrorEnabled()) 
			{
				logger.error("Could not unregister MBean [" + objectName + "]",
						ex);
			}
		}
		
		if(this.registeredMBeans.contains(objectName))
		{
			this.registeredMBeans.remove(objectName);
		}
		logger.info("unregisterMBean end");
	}


	/**
	 * Called when an MBean is registered under the given {@link ObjectName}. Allows
	 * subclasses to perform additional processing when an MBean is registered.
	 * @param objectName the {@link ObjectName} of the MBean that was registered.
	 */
	protected void onRegister(ObjectName objectName) {
	}

	/**
	 * Called when an MBean is unregistered under the given {@link ObjectName}. Allows
	 * subclasses to perform additional processing when an MBean is unregistered.
	 * @param objectName the {@link ObjectName} of the MBean that was unregistered.
	 */
	protected void onUnregister(ObjectName objectName) {
	}

	public MBeanServer getServer() {
		return server;
	}

}
