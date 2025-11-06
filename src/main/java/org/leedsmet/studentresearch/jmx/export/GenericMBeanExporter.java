package org.leedsmet.studentresearch.jmx.export;

import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.beans.BeanUtils;
import org.leedsmet.studentresearch.core.JdkVersion;
import org.leedsmet.studentresearch.jmx.assembler.GenericMBeanAssembler;
import org.leedsmet.studentresearch.jmx.assembler.NoGetterMBeanInfo;
import org.leedsmet.studentresearch.jmx.support.GenericMBeanRegistrationSupport;
import org.leedsmet.studentresearch.jmx.support.JmxUtils;
import org.leedsmet.studentresearch.util.Assert;
import org.leedsmet.studentresearch.util.ClassUtils;
import org.leedsmet.studentresearch.util.ObjectUtils;

/**
 * JMX exporter that allows for exposing any <i>managed bean</i> to a JMX
 * <code>MBeanServer</code>
 * 
 * <p>
 * If the bean implements one of the JMX management interfaces, then
 * MBeanExporter can simply register the MBean with the server.
 * 
 * <p>
 * If the bean does not implement one of the JMX management interfaces, then
 * GenericMBeanExporter will create the management information using the
 * supplied mBeanType information
 * 
 * @author Joseph Aruja
 * @see org.leedsmet.studentresearch.jmx.assembler.GenericMBeanAssembler
 */
public class GenericMBeanExporter extends GenericMBeanRegistrationSupport {

	protected final Log logger = LogFactory.getLog(getClass());

	/** Stores all the MBeans that are to be registered. */
	private Map<Object, Object> mBeans = null;

	/** Stores the MBeanInfoAssembler to use for this exporter */
	private GenericMBeanAssembler assembler = new GenericMBeanAssembler();

	/** Constant for the JMX <code>mr_type</code> "ObjectReference" */
	private static final String MR_TYPE_OBJECT_REFERENCE = "ObjectReference";

	/**
	 * Indicates whether framework should expose the managed resource
	 * ClassLoader in the MBean
	 */
	private boolean exposeManagedResourceClassLoader = false;

	/**
	 * @return
	 */
	public Map<Object, Object> getMBeans() {
		return mBeans;
	}

	/**
	 * @param beans
	 */
	public void setMBeans(Map<Object, Object> beans) {
		mBeans = beans;
	}

	/**
	 * This method is used for creating and registering the MBean The creating
	 * of the MBean class is achieved using the utility method of
	 * <code>ClassUtils</code> and it is instantiate using
	 * <code>BeanUtils</code> instead of using instantiate() of
	 * <code>MBeanServer#</code>
	 * 
	 * @param mBeanClassName
	 * @param mBeanObjectName
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @see javax.management.MBeanServer#instantiate(String)
	 * @see org.leedsmet.studentresearch.util.ClassUtils#forName(String)
	 * @see org.leedsmet.studentresearch.beans.BeanUtils#instantiateClass(Class)
	 * 
	 */
	public void exportMBean(String mBeanClassName, ObjectName mBeanObjectName)
			throws ReflectionException, MBeanException {
		logger.info("exportMBean start");
		Assert.notNull(mBeanClassName, "mBeanClassName must not be null");
		Assert.notNull(mBeanObjectName, "mBeanObjectName must not be null");

		Object objectToBeManaged = null;
		Object mBean = null;
		// ---------------------------Can't be used--------------------------/
		// mBean = this.server.instantiate(mBeanClassName);
		// This method may or may not work on all servers due
		// ---------------------------Can't be used--------------------------/
		try {
			Class clazz = ClassUtils.forName(mBeanClassName);
			logger.info("Retrieved class using ClassUtils : " + clazz);
			objectToBeManaged = BeanUtils.instantiateClass(clazz);
			logger.info("mBean instantiated with BeanUtils: "
					+ objectToBeManaged);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LinkageError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isMBean(objectToBeManaged.getClass())) {
			mBean = objectToBeManaged;

		} else {
			mBean = createAndConfigureMBean(objectToBeManaged,
					objectToBeManaged.getClass().getName());
		}

		try {
			doRegister(mBean, mBeanObjectName);
		} catch (JMException ex) {
			throw new UnableToRegisterMBeanException(
					"Unable to register as Standard MBean ["
							+ objectToBeManaged + "] with object name ["
							+ mBeanObjectName + "]", ex);
		}
		logger.info("exportMBean end");
	}

	/**
	 * This method is used for creating and registering the MBean The creating
	 * of the MBean class is achieved using the utility method of
	 * <code>ClassUtils</code> and it is instantiate using
	 * <code>BeanUtils</code> instead of using instantiate() of
	 * <code>MBeanServer#</code>
	 * 
	 * @param objectToBeManaged
	 * @param mBeanObjectName
	 * @throws ReflectionException
	 * @throws MBeanException
	 */
	public void exportMBean(Object objectToBeManaged, ObjectName mBeanObjectName)
			throws ReflectionException, MBeanException {
		logger.info("exportMBean start");
		Assert.notNull(objectToBeManaged, "mBeanObject must not be null");
		Assert.notNull(mBeanObjectName, "mBeanObjectName must not be null");

		Object mBean = null;

		if (isMBean(objectToBeManaged.getClass())) {
			mBean = objectToBeManaged;

		} else {
			mBean = createAndConfigureMBean(objectToBeManaged,
					objectToBeManaged.getClass().getName());
		}

		try {
			doRegister(mBean, mBeanObjectName);
		} catch (JMException ex) {
			throw new UnableToRegisterMBeanException(
					"Unable to register as Standard MBean ["
							+ objectToBeManaged + "] with object name ["
							+ mBeanObjectName + "]", ex);
		}

		logger.info("exportMBean end");
	}

	public void exportMBeans(Map<Object, Object> mbMap) {
		// Unimplemented
	}

	public void removeMBean(ObjectName mBeanObjectName) {
		unregisterMBean(mBeanObjectName);
	}

	public void removeMBeans() {
		unregisterMBeans();
	}

	/**
	 * Creates an MBean that is configured with the appropriate management
	 * interface for the supplied managed resource.
	 * 
	 * @param managedResource
	 *            the resource that is to be exported as an MBean
	 * @param beanKey
	 *            the key associated with the managed bean
	 * @see #createModelMBean()
	 * @see #getMBeanInfo(Object, String)
	 */
	protected ModelMBean createAndConfigureMBean(Object managedResource,
			String beanKey) throws MBeanExportException {

		logger.info("createAndConfigureMBean start");
		try {
			ModelMBean mbean = createModelMBean();

			logger.info("Java Version : " + JdkVersion.getJavaVersion() + " : "
					+ JdkVersion.getMajorJavaVersion());

			if (!JdkVersion.isAtLeastJava17()) {
				mbean.setModelMBeanInfo(new NoGetterMBeanInfo(getMBeanInfo(
						managedResource, beanKey)));
			} else {
				mbean.setModelMBeanInfo(getMBeanInfo(managedResource, beanKey));
			}

			mbean.setManagedResource(managedResource, MR_TYPE_OBJECT_REFERENCE);

			logger.info("createAndConfigureMBean end");
			return mbean;
		} catch (Exception ex) {
			throw new MBeanExportException(
					"Could not create ModelMBean for managed resource ["
							+ managedResource + "] with key '" + beanKey + "'",
					ex);
		}

	}

	/**
	 * Create an instance of a class that implements <code>ModelMBean</code>.
	 * <p>
	 * This method is called to obtain a <code>ModelMBean</code> instance to
	 * use when registering a bean. This method is called once per bean during
	 * the registration phase and must return a new instance of
	 * <code>ModelMBean</code>
	 * 
	 * @return a new instance of a class that implements <code>ModelMBean</code>
	 * @throws javax.management.MBeanException
	 *             if creation of the ModelMBean failed
	 */
	protected ModelMBean createModelMBean() throws MBeanException {
		return (this.exposeManagedResourceClassLoader ? new GenericModelMBean()
				: new RequiredModelMBean());
	}

	/**
	 * Gets the <code>ModelMBeanInfo</code> for the bean with the supplied key
	 * and of the supplied type.
	 */
	private ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey)
			throws JMException {
		ModelMBeanInfo modelMBeanInfo = this.assembler.getMBeanInfo(
				managedBean, beanKey);
		logger.info("modelMBeanInfo : " + modelMBeanInfo);
		if (logger.isWarnEnabled()
				&& ObjectUtils.isEmpty(modelMBeanInfo.getAttributes())
				&& ObjectUtils.isEmpty(modelMBeanInfo.getOperations())) {
			logger
					.warn("Bean with key '"
							+ beanKey
							+ "' has been registered as an MBean but has no exposed attributes or operations");
		}
		return modelMBeanInfo;
	}

	/**
	 * @return the exposeManagedResourceClassLoader
	 */
	public boolean isExposeManagedResourceClassLoader() {
		return exposeManagedResourceClassLoader;
	}

	/**
	 * @param exposeManagedResourceClassLoader
	 *            the exposeManagedResourceClassLoader to set
	 */
	public void setExposeManagedResourceClassLoader(
			boolean exposeManagedResourceClassLoader) {
		this.exposeManagedResourceClassLoader = exposeManagedResourceClassLoader;
	}

	/**
	 * Determine whether the given bean class qualifies as an MBean as-is.
	 * <p>
	 * The default implementation delegates to {@link JmxUtils#isMBean}, which
	 * checks for {@link javax.management.DynamicMBean} classes as well as
	 * classes with corresponding "*MBean" interface (Standard MBeans). This can
	 * be overridden in subclasses, for example to check for JDK 1.6 MXBeans as
	 * well.
	 * 
	 * @param beanClass
	 *            the bean class to analyze
	 * @see org.springframework.jmx.support.JmxUtils#isMBean(Class)
	 */
	protected boolean isMBean(Class beanClass) {
		return JmxUtils.isMBean(beanClass);
	}

	/**
	 * @return the assembler
	 */
	public GenericMBeanAssembler getAssembler() {
		return assembler;
	}

	/**
	 * @param assembler
	 *            the assembler to set
	 */
	public void setAssembler(GenericMBeanAssembler assembler) {
		this.assembler = assembler;
	}

}
