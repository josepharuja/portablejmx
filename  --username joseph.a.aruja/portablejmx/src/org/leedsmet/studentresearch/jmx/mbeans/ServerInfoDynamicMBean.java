/**
 * 
 */
package org.leedsmet.studentresearch.jmx.mbeans;

import java.util.Iterator;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.jmx.valueobjects.ServerInfoVO;
import org.leedsmet.studentresearch.jmx.valueobjects.ServerStatisticsVO;

/**
 * This class implements a DynamicMBean to expose and manage
 * <code>ServerInfoVO</code>
 * @author Joseph Aruja 
 * @see org.leedsmet.studentresearch.jmx.valueobjects.ServerInfoVO
 * @see javax.management.DynamicMBean
 *  
 */
public class ServerInfoDynamicMBean implements DynamicMBean {

	private MBeanInfo _MBeanInfo;

	private ServerInfoVO serverInfoVO;
	
	private ServerStatisticsVO serverStatisticsVO;

	/**
	 * <code>Log</code> instance for this class.
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	interface ServerInfoDesc {

		String TO_SHOW_THE_START_STOP_DETAILS = "To show the start stop details";

		String TO_HIDE_THE_START_STOP_DETAILS = "To hide the start stop details";

		String DYNAMICMBEAN_CLASS_NAME = ServerInfoDynamicMBean.class.getName();

		String TO_STOP_THE_SERVER = "To stop the server.";

		String TO_START_THE_SERVER = "To start the server.";

		String THE_STOP_TIME_OF_THE_SERVER = "The stop time of the Server";

		String THE_START_TIME_OF_THE_SERVER = "The start time of the Server";

		String DEFAULT_PUBLIC_CONSTRUCTOR = "Default public constructor";

		String SERVER_INFO_DYNAMIC_MBEAN = "ServerInfoDynamicMBean";

	}

	interface AttributeNames {

		String STOP_TIME = "StopTime";

		String START_TIME = "StartTime";

	}

	interface Operations {

		String STOP_SERVER = "stopServer";

		String START_SERVER = "startServer";

		String SHOW_START_STOP_TIME = "showStartStopTime";

		String HIDE_START_STOP_TIME = "hideStartStopTime";

	}

	public ServerInfoDynamicMBean() {
		logger.info("Constructing the ServerInfoDynamicMBean start");
		// Currently there is no underlying server to manage and hence setting
		// the startTime
		serverInfoVO = new ServerInfoVO();
		serverStatisticsVO = new ServerStatisticsVO();
		buildMBeanInfo(serverInfoVO.isStarted(), serverInfoVO.isShowDetails());
		logger.info("Constructing the ServerInfoDynamicMBean end");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#setAttribute(javax.management.Attribute)
	 */
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {

		logger.info("setAttribute start");

		// Check attribute to avoid NullPointerException later on
		if (attribute == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"Attribute cannot be null"), "Cannot invoke a setter of "
					+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME
					+ " with null attribute");
		}

		String name = attribute.getName();
		if (name.equals(AttributeNames.START_TIME)) {
			serverInfoVO.setStartTime(new java.util.Date(System
					.currentTimeMillis()));
		} else if (name.equals(AttributeNames.STOP_TIME)) {

			serverInfoVO.setStopTime(new java.util.Date(System
					.currentTimeMillis()));
		}
		// unrecognized attribute name
		else {
			throw new AttributeNotFoundException("Attribute " + name
					+ " not found in " + ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME);
		}
		logger.info("setAttribute end");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String attributeName)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {

		logger.info("getAttribute start");
		java.util.Date startStopTime = null;

		if (attributeName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"Attribute name cannot be null"),
					"Cannot invoke a getter of "
							+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME
							+ " with null attribute name");
		}

		// Call the corresponding getter for a recognized attribute_name
		if (attributeName.equals(AttributeNames.START_TIME)) {
			startStopTime = serverInfoVO.getStartTime();
		} else if (attributeName.equals(AttributeNames.STOP_TIME)) {
			startStopTime = serverInfoVO.getStopTime();
		} else {
			// If attribute_name has not been recognized
			throw (new AttributeNotFoundException("Cannot find "
					+ attributeName + " attribute in "
					+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME));
		}
		logger.info("getAttribute end");
		return startStopTime;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getAttributes(java.lang.String[])
	 */
	public AttributeList getAttributes(String[] attributeNames) {

		logger.info("getAttributes start");
		// Check attributeNames to avoid NullPointerException later on
		if (attributeNames == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"attributeNames[] cannot be null"),
					"Cannot invoke a getter of "
							+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME);
		}
		AttributeList resultList = new AttributeList();

		// if attributeNames is empty, return an empty result list
		if (attributeNames.length == 0)
			return resultList;

		// build the result attribute list
		for (int i = 0; i < attributeNames.length; i++) {
			try {
				Object value = getAttribute((String) attributeNames[i]);
				resultList.add(new Attribute(attributeNames[i], value));
			} catch (Exception e) {
				// print debug info but continue processing list
				e.printStackTrace();
			}
		}
		logger.info("getAttributes end");
		return (resultList);
	}

	public AttributeList setAttributes(AttributeList attributes) {

		logger.info("setAttributes start");

		// Check attributes to avoid NullPointerException later on
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"AttributeList attributes cannot be null"),
					"Cannot invoke a setter of "
							+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME);
		}
		AttributeList resultList = new AttributeList();

		// if attributeNames is empty, nothing more to do
		if (attributes.isEmpty())
			return resultList;

		// try to set each attribute and add to result list if successful
		for (Iterator i = attributes.iterator(); i.hasNext();) {
			Attribute attr = (Attribute) i.next();
			try {
				setAttribute(attr);
				String name = attr.getName();
				Object value = getAttribute(name);
				resultList.add(new Attribute(name, value));
			} catch (Exception e) {
				// print debug info but keep processing list
				e.printStackTrace();
			}
		}
		logger.info("setAttributes end");
		return (resultList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#getMBeanInfo()
	 */
	public MBeanInfo getMBeanInfo() {
		// TODO Auto-generated method stub
		return _MBeanInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.DynamicMBean#invoke(java.lang.String,
	 *      java.lang.Object[], java.lang.String[])
	 */
	public Object invoke(String actionName, Object[] params, String[] signature)
			throws MBeanException, ReflectionException {

		logger.info("invoke start");

		Object ret = Void.TYPE;
		// Check operationName to avoid NullPointerException later on
		if (actionName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"Operation name cannot be null"),
					"Cannot invoke a null operation in "
							+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME);
		}

		// Call the corresponding operation for a recognized name
		if (actionName.equals(Operations.START_SERVER)) {
			// this code is specific to the internal "startServer" method:
			startServer(); // no parameters to check
		} else if (actionName.equals(Operations.STOP_SERVER)) {
			// this code is specific to the internal "stopServer" method:
			stopServer(); // no parameters to check
		} else if (actionName.equals(Operations.HIDE_START_STOP_TIME)) {
			// this code is specific to the internal "hideStartStopTime" method:
			hideStartStopTime(); // no parameters to check
		} else if (actionName.equals(Operations.SHOW_START_STOP_TIME)) {
			// this code is specific to the internal "showStartStopTime" method:
			showStartStopTime(); // no parameters to check
		}

		else {
			// unrecognized operation name:
			throw new ReflectionException(
					new NoSuchMethodException(actionName),
					"Cannot find the operation " + actionName + " in "
							+ ServerInfoDesc.DYNAMICMBEAN_CLASS_NAME);
		}

		logger.info("invoke end");

		return ret;

	}

	private void buildMBeanInfo(boolean isServerStarted, boolean showDetails) {

		// -----Construct the MBean with the following details
		// --------------Construct MBeanConstructorInfo-------------------
		MBeanConstructorInfo[] constructorInfo = new MBeanConstructorInfo[1];

		constructorInfo[0] = new MBeanConstructorInfo(
				ServerInfoDesc.SERVER_INFO_DYNAMIC_MBEAN,
				ServerInfoDesc.DEFAULT_PUBLIC_CONSTRUCTOR, null);
		// ---------------------------------------------------------------
		// ------------Construct MBeanAttributeInfo---------------------------
		MBeanAttributeInfo[] attributeInfo = null;
		if (showDetails) {
			attributeInfo = new MBeanAttributeInfo[2];
			// - StartTime
			attributeInfo[0] = new MBeanAttributeInfo(
					AttributeNames.START_TIME, // name
					Long.TYPE.getName(), // type
					ServerInfoDesc.THE_START_TIME_OF_THE_SERVER,// description
					true, // isReadable
					false, // isWriteable
					false); // isIs
			// - StopTime
			attributeInfo[1] = new MBeanAttributeInfo(AttributeNames.STOP_TIME, // name
					Long.TYPE.getName(), // type
					ServerInfoDesc.THE_STOP_TIME_OF_THE_SERVER,// description
					true, // isReadable
					false, // isWriteable
					false); // isIs
		} else {
			attributeInfo = new MBeanAttributeInfo[0];
		}

		// ---------------------------------------------------------------
		// --------------Construct MBeanOperationInfo-------------------
		MBeanParameterInfo[] voidSignature = null;
		MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[3];
		if (isServerStarted) {
			// - stopServer
			operationInfo[0] = new MBeanOperationInfo(Operations.STOP_SERVER, // name
					ServerInfoDesc.TO_STOP_THE_SERVER, // description
					voidSignature, // MBeanParameterInfo[]
					Void.TYPE.getName(), // type
					MBeanOperationInfo.ACTION // impact
			);

		} else {
			// - startServer
			operationInfo[0] = new MBeanOperationInfo(Operations.START_SERVER, // name
					ServerInfoDesc.TO_START_THE_SERVER, // description
					voidSignature, // MBeanParameterInfo[]
					Void.TYPE.getName(), // type
					MBeanOperationInfo.ACTION // impact
			);

		}
		operationInfo[1] = new MBeanOperationInfo(
				Operations.HIDE_START_STOP_TIME, // name
				ServerInfoDesc.TO_HIDE_THE_START_STOP_DETAILS, // description
				voidSignature, // MBeanParameterInfo[]
				Void.TYPE.getName(), // type
				MBeanOperationInfo.ACTION // impact
		);
		operationInfo[2] = new MBeanOperationInfo(
				Operations.SHOW_START_STOP_TIME, // name
				ServerInfoDesc.TO_SHOW_THE_START_STOP_DETAILS, // description
				voidSignature, // MBeanParameterInfo[]
				Void.TYPE.getName(), // type
				MBeanOperationInfo.ACTION // impact
		);

		// --------------------------------------------------------------
		// --------------Construct MBeanNotificationInfo-------------------
		MBeanNotificationInfo[] notificationInfo = new MBeanNotificationInfo[0];
		// -------------------------------------------------------------
		// --------------Construct MBeanInfo-------------------
		_MBeanInfo = new MBeanInfo(this.getClass().getName(), // DynamicMBean
				// implementing
				// class name
				ServerInfoDesc.SERVER_INFO_DYNAMIC_MBEAN, // description
				attributeInfo, // MBeanAttributeInfo[]
				constructorInfo, // MBeanConstructorInfo[]
				operationInfo, // MBeanOperationInfo[]
				notificationInfo // MBeanNotificationInfo[]
		);
		// ---------------------------------------------------------

	}

	// ===============================================================
	// Internal methods

	public void startServer() {
		logger.info("Server is starting");
		serverInfoVO.startServer();
		buildMBeanInfo(serverInfoVO.isStarted(), serverInfoVO.isShowDetails());
	}

	public void stopServer() {
		logger.info("Server is stopping");
		serverInfoVO.stopServer();
		buildMBeanInfo(serverInfoVO.isStarted(), serverInfoVO.isShowDetails());
	}

	public void showStartStopTime() {
		logger.info("Making the attributes visible");
		serverInfoVO.showStartStopTime();
		buildMBeanInfo(serverInfoVO.isStarted(), serverInfoVO.isShowDetails());
	}

	public void hideStartStopTime() {

		logger.info("Hiding the attributes");
		serverInfoVO.hideStartStopTime();
		buildMBeanInfo(serverInfoVO.isStarted(), serverInfoVO.isShowDetails());
	}
	
	public String getJVMStatistics()
	{
		return serverStatisticsVO.getJvmStatistics();
	}

	/**
	 * @return the serverInfoVO
	 */
	public ServerInfoVO getServerInfoVO() {
		return serverInfoVO;
	}

	/**
	 * @param serverInfoVO the serverInfoVO to set
	 */
	public void setServerInfoVO(ServerInfoVO serverInfoVO) {
		this.serverInfoVO = serverInfoVO;
	}

	/**
	 * @return the serverStatisticsVO
	 */
	public ServerStatisticsVO getServerStatisticsVO() {
		return serverStatisticsVO;
	}

	/**
	 * @param serverStatisticsVO the serverStatisticsVO to set
	 */
	public void setServerStatisticsVO(ServerStatisticsVO serverStatisticsVO) {
		this.serverStatisticsVO = serverStatisticsVO;
	}

	// ===============================================================
	
	

}
