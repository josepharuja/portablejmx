package org.leedsmet.studentresearch.jmx.export;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.jmx.assembler.ServerInfoModelMBeanAssembler;

/**
 * JMX exporter that allows for exposing any <code>ServerInfoVO</code>
 * and <code>
 * to a JMX <code>MBeanServer</code>
 *
 * <p>If the bean implements one of the JMX management interfaces,
 * then MBeanExporter can simply register the MBean with the server
 * automatically, through its autodetection process.
 *
 * @author Joseph Aruja
 * @see org.leedsmet.studentresearch.jmx.ServerInfoModelMBeanAssembler 
 */
public class ServerInfoModelMBeanManager {

	/**
	 * Constant indicating that registration should fail when attempting to
	 * register an MBean under a name that already exists.
	 * <p>
	 * This is the default registration behavior.
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

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * The action take when registering an MBean and finding that it already
	 * exists. By default an exception is raised.
	 */
	private int registrationBehavior = REGISTRATION_FAIL_ON_EXISTING;

	/**
	 * The beans that have been registered by this exporter.
	 */
	protected Set<ObjectName> registeredMBeans = new HashSet<ObjectName>();

	/**
	 * The assembler for building the ModelMBeanInfo
	 */
	private ServerInfoModelMBeanAssembler assembler = new ServerInfoModelMBeanAssembler();

	private ModelMBean modelMBean;

	private MBeanServer server;

	public MBeanServer getServer() {
		return server;
	}

	public void setServer(MBeanServer server) {
		this.server = server;
	}

	public ModelMBean getModelMBean() {
		return modelMBean;
	}

	public void setModelMBean(ModelMBean modelMBean) {
		this.modelMBean = modelMBean;
	}

	/**
	 * Unregisters all the registered MBeans
	 * @see unregisterMBeans()
	 */
	public void removeMBeans() {
		unregisterMBeans();
	}

	/**
	 * Registers the MBean created by <code>ServerInfoModelMBeanAssembler</code>
	 * @param mBeanObjectName
	 * @param mBeanObject
	 * @throws JMException
	 * @throws RuntimeOperationsException
	 * @throws InvalidTargetObjectTypeException
	 * @see #ServerInfoModelMBeanAssembler.createAndConfigureModelMBean(mBeanObjectName, mBeanObject)
	 * @see #doRegister
	 */
	public void exportMBean(ObjectName mBeanObjectName, Object mBeanObject)
			throws JMException, RuntimeOperationsException,
			InvalidTargetObjectTypeException {
		logger.info("exportMBean start");

		modelMBean = assembler.createAndConfigureModelMBean(mBeanObjectName,
				mBeanObject);
		doRegister(modelMBean, mBeanObjectName);

		logger.info("exportMBean end");

	}

	/**
	 * Actually register the MBean with the server. The behavior when
	 * encountering an existing MBean can be configured using the
	 * {@link #setRegistrationBehavior(int)} and
	 * {@link #setRegistrationBehaviorName(String)} methods.
	 * 
	 * @throws JMException
	 *             if the registration failed
	 */
	private void doRegister(Object mbean, ObjectName objectName)
			throws JMException {

		ObjectInstance registeredBean = null;
		try {
			logger.info("doRegister start");

			registeredBean = this.server.registerMBean(mbean, objectName);

			logger.info("doRegister end 1");
		} catch (InstanceAlreadyExistsException ex) {
			if (this.registrationBehavior == REGISTRATION_IGNORE_EXISTING) {
				if (logger.isDebugEnabled()) {
					logger.debug("Ignoring existing MBean at [" + objectName
							+ "]");
				}
			} else if (this.registrationBehavior == REGISTRATION_REPLACE_EXISTING) {
				try {
					if (logger.isDebugEnabled()) {
						logger.debug("Replacing existing MBean at ["
								+ objectName + "]");
					}
					this.server.unregisterMBean(objectName);
					registeredBean = this.server.registerMBean(mbean,
							objectName);
					logger.info("doRegister end 2");
				} catch (InstanceNotFoundException ex2) {
					logger.error("Unable to replace existing MBean at ["
							+ objectName + "]", ex2);
					throw ex;
				}
			} else {
				throw ex;
			}
		}

		// Track registration and notify listeners.
		ObjectName actualObjectName = (registeredBean != null ? registeredBean
				.getObjectName() : null);
		if (actualObjectName == null) {
			actualObjectName = objectName;
		}
		this.registeredMBeans.add(actualObjectName);
		logger.info("Adding the MBean :" + actualObjectName
				+ " to the set of registeredMBeans");
	}

	/**
	 * Unregisters all beans that have been registered by an instance of this
	 * class.
	 */
	private void unregisterMBeans() {
		logger.info("unregisterBeans start");
		for (Iterator<ObjectName> it = this.registeredMBeans.iterator(); it
				.hasNext();) {
			ObjectName objectName = it.next();
			try {
				// MBean might already have been unregistered by an external
				// process.
				if (this.server.isRegistered(objectName)) {
					this.server.unregisterMBean(objectName);
					logger.info("unregistering MBean :" + objectName);
				} else {
					if (logger.isWarnEnabled()) {
						logger
								.warn("Could not unregister MBean ["
										+ objectName
										+ "] as said MBean "
										+ "is not registered (perhaps already unregistered by an external process)");
					}
				}
			} catch (JMException ex) {
				if (logger.isErrorEnabled()) {
					logger.error("Could not unregister MBean [" + objectName
							+ "]", ex);
				}
			}
		}
		this.registeredMBeans.clear();
		logger.info("unregisterBeans end");
	}

}
