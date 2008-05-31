package org.leedsmet.studentresearch.jmx.assembler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.management.Descriptor;
import javax.management.JMException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.beans.BeanUtils;
import org.leedsmet.studentresearch.core.JdkVersion;
import org.leedsmet.studentresearch.jmx.support.JmxUtils;

/**
 * Class to create management interface metadata for a managed resource.
 *
 * <p>Used by the <code>MBeanExporter</code> to generate the management
 * interface for any bean that is not an MBean.
 *
 * @author Joseph Aruja
 * @see org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter
 */
public class GenericMBeanAssembler 
{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	/**
	 * Indicates whether or not strict casing is being used for attributes.
	 */
	private boolean useStrictCasing = true;
	
	
	private boolean exposeClassDescriptor = false;
	
	/**
	 * Default value for the JMX field "currencyTimeLimit".
	 */
	private Integer defaultCurrencyTimeLimit;	
	
	/**
	 * Identifies a getter method in a JMX {@link Descriptor}.
	 */
	protected static final String FIELD_GET_METHOD = "getMethod";

	/**
	 * Identifies a setter method in a JMX {@link Descriptor}.
	 */
	protected static final String FIELD_SET_METHOD = "setMethod";
	
	
	/**
	 * Constant identifier for the role field in a JMX {@link Descriptor}.
	 */
	protected static final String FIELD_ROLE = "role";

	/**
	 * Constant identifier for the getter role field value in a JMX {@link Descriptor}.
	 */
	protected static final String ROLE_GETTER = "getter";

	/**
	 * Constant identifier for the setter role field value in a JMX {@link Descriptor}.
	 */
	protected static final String ROLE_SETTER = "setter";
	
	

	/**
	 * Constant identifier for the visibility field in a JMX {@link Descriptor}.
	 */
	protected static final String FIELD_VISIBILITY = "visibility";
	
	
	/**
	 * Constant identifier for the class field in a JMX {@link Descriptor}.
	 */
	protected static final String FIELD_CLASS = "class";

	/**
	 * Lowest visibility, used for operations that correspond to
	 * accessors or mutators for attributes.
	 * @see #FIELD_VISIBILITY
	 */
	protected static final Integer ATTRIBUTE_OPERATION_VISIBILITY = new Integer(4);
	
	
	/**
	 * Identifies an operation (method) in a JMX {@link Descriptor}.
	 */
	protected static final String ROLE_OPERATION = "operation";
	
	/**
	 * Constant identifier for the currency time limit field in a JMX {@link Descriptor}.
	 */
	protected static final String FIELD_CURRENCY_TIME_LIMIT = "currencyTimeLimit";
	
	
	
	
	
	
	
	/**
	 * Create the ModelMBeanInfo for the given managed resource.
	 * @param managedBean the bean that will be exposed (might be an AOP proxy)
	 * @param beanKey the key associated with the managed bean
	 * @return the ModelMBeanInfo metadata object
	 * @throws JMException in case of errors
	 */
	public ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey) throws JMException {
		
		logger.info("getMBeanInfo start");
		logger.info("className : "+getClassName(managedBean, beanKey));
		logger.info("Description : "+getDescription(managedBean, beanKey));
		logger.info("AttributeInfo : "+getAttributeInfo(managedBean, beanKey));
		logger.info("ConstructorInfo : "+getConstructorInfo(managedBean, beanKey));
		logger.info("OperationInfo : "+getOperationInfo(managedBean, beanKey));
		logger.info("NotificationInfo : "+getNotificationInfo(managedBean, beanKey));
		
		ModelMBeanInfo info = new ModelMBeanInfoSupport(
				getClassName(managedBean, beanKey), getDescription(managedBean, beanKey),
				getAttributeInfo(managedBean, beanKey), getConstructorInfo(managedBean, beanKey),
				getOperationInfo(managedBean, beanKey), getNotificationInfo(managedBean, beanKey));
		
		Descriptor desc = info.getMBeanDescriptor();
		populateMBeanDescriptor(desc, managedBean, beanKey);
		info.setMBeanDescriptor(desc);
		logger.info("getMBeanInfo end");
		return info;
	}
	
	
	
	
	
	
	
	
	
	/**
	 * @param managedBean
	 * @param beanKey
	 * @return
	 * @throws JMException
	 */
	protected ModelMBeanAttributeInfo[] getAttributeInfo(Object managedBean, String beanKey) throws JMException {
		PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(managedBean.getClass());
		List<ModelMBeanAttributeInfo> infos = new ArrayList<ModelMBeanAttributeInfo>();

		for (int i = 0; i < props.length; i++) {
			Method getter = props[i].getReadMethod();
			if (getter != null && getter.getDeclaringClass() == Object.class) {
				continue;
			}
			if (getter != null && !true) {
				getter = null;
			}

			Method setter = props[i].getWriteMethod();
			if (setter != null && !true) {
				setter = null;
			}

			if (getter != null || setter != null) {
				// If both getter and setter are null, then this does not need exposing.
				String attrName = JmxUtils.getAttributeName(props[i], isUseStrictCasing());
				String description = getAttributeDescription(props[i], beanKey);
				ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(attrName, description, getter, setter);

				Descriptor desc = info.getDescriptor();
				if (getter != null) {
					desc.setField(FIELD_GET_METHOD, getter.getName());
				}
				if (setter != null) {
					desc.setField(FIELD_SET_METHOD, setter.getName());
				}

				populateAttributeDescriptor(desc, getter, setter, beanKey);
				info.setDescriptor(desc);
				infos.add(info);
			}
		}

		return infos.toArray(new ModelMBeanAttributeInfo[infos.size()]);
	}
	
	/**
	 * Return whether strict casing for attributes is enabled.
	 */
	protected boolean isUseStrictCasing() {
		return useStrictCasing;
	}
	
	/**
	 * @param propertyDescriptor
	 * @param beanKey
	 * @return
	 */
	protected String getAttributeDescription(PropertyDescriptor propertyDescriptor, String beanKey) {
		return propertyDescriptor.getDisplayName();
	}
	
	
	
	protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey) {
		applyDefaultCurrencyTimeLimit(descriptor);
	}
	/**
	 * @param desc
	 * @param getter
	 * @param setter
	 * @param beanKey
	 */
	protected void populateAttributeDescriptor(Descriptor desc, Method getter, Method setter, String beanKey) {
		applyDefaultCurrencyTimeLimit(desc);
	}
	
	protected final void applyDefaultCurrencyTimeLimit(Descriptor desc) {
		if (getDefaultCurrencyTimeLimit() != null) {
			desc.setField(FIELD_CURRENCY_TIME_LIMIT, getDefaultCurrencyTimeLimit().toString());
		}
	}
	
	/**
	 * Return default value for the JMX field "currencyTimeLimit", if any.
	 */
	protected Integer getDefaultCurrencyTimeLimit() {
		return this.defaultCurrencyTimeLimit;
	}

	
	
	
	
	
	
	/**
	 * @param managedBean
	 * @param beanKey
	 * @return
	 * @throws JMException
	 */
	protected String getDescription(Object managedBean, String beanKey) throws JMException 
	{
		String targetClassName = getTargetClass(managedBean).getName();
		return targetClassName;
	}
	
	
	/**
	 * @param managedBean
	 * @param beanKey
	 * @return
	 * @throws JMException
	 */
	protected String getClassName(Object managedBean, String beanKey) throws JMException {
		return getTargetClass(managedBean).getName();
	}
	
	
	/**
	 * @param managedBean
	 * @return
	 */
	protected Class<? extends Object> getTargetClass(Object managedBean) 
	{
		return managedBean.getClass();
	}
	
	/**
	 * @param managedBean
	 * @param beanKey
	 * @return
	 * @throws JMException
	 */
	protected ModelMBeanConstructorInfo[] getConstructorInfo(Object managedBean, String beanKey)
	throws JMException 
	{
			return new ModelMBeanConstructorInfo[0];
	}
	
	/**
	 * @param managedBean
	 * @param beanKey
	 * @return
	 */
	protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey) {
	return  new ModelMBeanNotificationInfo[0];
	}
	
	
	/**
	 * @param managedBean
	 * @param beanKey
	 * @return
	 */
	protected ModelMBeanOperationInfo[] getOperationInfo(Object managedBean, String beanKey) {
		Method[] methods = getTargetClass(managedBean).getMethods();
		List<ModelMBeanOperationInfo> infos = new ArrayList<ModelMBeanOperationInfo>();

		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (JdkVersion.isAtLeastJava15() && method.isSynthetic()) {
				continue;
			}
			if (method.getDeclaringClass().equals(Object.class)) {
				continue;
			}

			ModelMBeanOperationInfo info = null;
			PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
			if (pd != null) {
				if ((method.equals(pd.getReadMethod()) && true) ||
						(method.equals(pd.getWriteMethod()) && true)) {
					// Attributes need to have their methods exposed as
					// operations to the JMX server as well.
					info = createModelMBeanOperationInfo(method, pd.getName(), beanKey);
					Descriptor desc = info.getDescriptor();
					if (method.equals(pd.getReadMethod())) {
						desc.setField(FIELD_ROLE, ROLE_GETTER);
					}
					else {
						desc.setField(FIELD_ROLE, ROLE_SETTER);
					}
					desc.setField(FIELD_VISIBILITY, ATTRIBUTE_OPERATION_VISIBILITY);
					if (isExposeClassDescriptor()) {
						desc.setField(FIELD_CLASS, getTargetClass(managedBean).getName());
					}
					info.setDescriptor(desc);
				}
			}
			else if (includeOperation(method, beanKey)) {
				info = createModelMBeanOperationInfo(method, method.getName(), beanKey);
				Descriptor desc = info.getDescriptor();
				desc.setField(FIELD_ROLE, ROLE_OPERATION);
				if (isExposeClassDescriptor()) {
					desc.setField(FIELD_CLASS, getTargetClass(managedBean).getName());
				}
				populateOperationDescriptor(desc, method, beanKey);
				info.setDescriptor(desc);
			}

			if (info != null) {
				infos.add(info);
			}
		}
		
		return infos.toArray(new ModelMBeanOperationInfo[infos.size()]);
	}
	
	
	/**
	 * @param desc
	 * @param method
	 * @param beanKey
	 */
	protected void populateOperationDescriptor(Descriptor desc, Method method, String beanKey) {
		applyDefaultCurrencyTimeLimit(desc);
	}
	/**
	 * Always returns <code>true</code>.
	 */
	protected boolean includeOperation(Method method, String beanKey) {
		return true;
	}
	
	
	/**
	 * Return whether to expose the JMX descriptor field "class" for managed operations.
	 */
	protected boolean isExposeClassDescriptor() {
		return exposeClassDescriptor;
	}
	
	/**
	 * @param method
	 * @param name
	 * @param beanKey
	 * @return
	 */
	protected ModelMBeanOperationInfo createModelMBeanOperationInfo(Method method, String name, String beanKey) {
		MBeanParameterInfo[] params = getOperationParameters(method, beanKey);
		if (params.length == 0) {
			return new ModelMBeanOperationInfo(getOperationDescription(method, beanKey), method);
		}
		else {
			return new ModelMBeanOperationInfo(name,
				getOperationDescription(method, beanKey),
				getOperationParameters(method, beanKey),
				method.getReturnType().getName(),
				MBeanOperationInfo.UNKNOWN);
		}
	}
	
	/**
	 * @param method
	 * @param beanKey
	 * @return
	 */
	protected MBeanParameterInfo[] getOperationParameters(Method method, String beanKey) {
		return new MBeanParameterInfo[0];
	}
	
	/**
	 * @param method
	 * @param beanKey
	 * @return
	 */
	protected String getOperationDescription(Method method, String beanKey) {
		return method.getName();
	}

	
}
