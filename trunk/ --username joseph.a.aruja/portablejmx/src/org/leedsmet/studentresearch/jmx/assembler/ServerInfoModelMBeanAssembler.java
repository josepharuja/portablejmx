package org.leedsmet.studentresearch.jmx.assembler;

import java.util.HashMap;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.leedsmet.studentresearch.jmx.valueobjects.ServerInfoVO;

/**
 * Class to create management interface metadata specific for a managed resource <code>ServerInfoVO</code>.
 *
 * <p>Used by the <code>ServerInfoModelMBeanManager</code> to generate the management
 * interface
 *
 * @author Joseph Aruja
 * @see org.leedsmet.studentresearch.jmx.valueobjects.ServerInfoVO
 * @see org.leedsmet.studentresearch.jmx.export.ServerInfoModelMBeanManager
 * 
 */
public class ServerInfoModelMBeanAssembler {
	
	
	private HashMap<String, Descriptor> attrDesc = new HashMap<String, Descriptor> ();
	
	
	private HashMap<String, Descriptor> methDesc = new HashMap<String, Descriptor> ();
	
	private static final String DEFAULT_PUBLIC_CONSTRUCTOR = "Default public constructor";

	private static final String SERVER_INFO_VO = "ServerInfoVO";

	private static final String OBJECT_REFERENCE = "objectReference";

	public interface ServerInfoVODesc {

		public static final String TO_SHOW_THE_START_STOP_DETAILS = "To show the start stop details";

		public static final String TO_HIDE_THE_START_STOP_DETAILS = "To hide the start stop details";

		public static final String TO_STOP_THE_SERVER = "To stop the server.";

		public static final String TO_START_THE_SERVER = "To start the server.";

		public static final String THE_STOP_TIME_OF_THE_SERVER = "The stop time of the Server";

		public static final String THE_START_TIME_OF_THE_SERVER = "The start time of the Server";

	}

	public interface ServerInfoVOAttributeNames {

		String STOP_TIME = "StopTime";

		String START_TIME = "StartTime";

	}

	public interface ServerInfoVOOperations {

		String STOP_SERVER = "stopServer";

		String START_SERVER = "startServer";

		String SHOW_START_STOP_TIME = "showStartStopTime";

		String HIDE_START_STOP_TIME = "hideStartStopTime";

	}

	/**
	 * Creates an MBean that is configured with the appropriate management
	 * interface for the supplied managed resource.
	 * @param managedResource the resource that is to be exported as an MBean
	 * @param beanKey the key associated with the managed bean
	 * @see #getMBeanInfo(Object, String) 
	 */
	public  ModelMBean createAndConfigureModelMBean(
			ObjectName mBeanObjectName, Object mBeanObject) {

		try {
			// Instantiate javax.management.modelmbean.RequiredModelMBean
			ModelMBean modelmbean = new RequiredModelMBean();
			modelmbean.setModelMBeanInfo(getModelMBeanInfoForServerInfoVO());
			// Associate it with the resource (a TestBean instance)
			modelmbean.setManagedResource(mBeanObject,
					ServerInfoModelMBeanAssembler.OBJECT_REFERENCE);
			return modelmbean;

		} catch (Exception e) {
			System.out
					.println("\t!!! ModelAgent: Could not create the model MBean !!!");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates the <code>ModelMBeanInfo</code> for the bean <code>ServerInfoVO</code>
	 */
	private  ModelMBeanInfo getModelMBeanInfoForServerInfoVO() {

		// -----Construct the MBean with the following details
		// --------------Construct MBeanConstructorInfo-------------------
		ModelMBeanConstructorInfo[] constructorInfo = new ModelMBeanConstructorInfo[1];

		constructorInfo[0] = new ModelMBeanConstructorInfo(
				ServerInfoModelMBeanAssembler.SERVER_INFO_VO,
				ServerInfoModelMBeanAssembler.DEFAULT_PUBLIC_CONSTRUCTOR, null);
		// ---------------------------------------------------------------
		// ------------Construct MBeanAttributeInfo---------------------------
		ModelMBeanAttributeInfo[] attributeInfo = new ModelMBeanAttributeInfo[2];
		// - StartTime
		Descriptor startTimeDesc = new DescriptorSupport();
		startTimeDesc.setField("name", "StartTime");
		startTimeDesc.setField("descriptorType", "attribute");
		startTimeDesc.setField("displayName", "Server StartTime");
		startTimeDesc.setField("getMethod", "getStartTime");
		startTimeDesc.setField("currencyTimeLimit", "20");
		attributeInfo[0] = new ModelMBeanAttributeInfo(
				ServerInfoVOAttributeNames.START_TIME, // name
				java.util.Date.class.getName(), // type
				ServerInfoVODesc.THE_START_TIME_OF_THE_SERVER,// description
				true, // isReadable
				false, // isWriteable
				false,// isIs
				startTimeDesc// descriptor
		);
		// - StopTime
		Descriptor stopTimeDesc = new DescriptorSupport();
		stopTimeDesc.setField("name", "StopTime");
		stopTimeDesc.setField("descriptorType", "attribute");
		stopTimeDesc.setField("displayName", "Server StopTime");
		stopTimeDesc.setField("getMethod", "getStopTime");
		stopTimeDesc.setField("currencyTimeLimit", "20");
		attributeInfo[1] = new ModelMBeanAttributeInfo(
				ServerInfoVOAttributeNames.STOP_TIME, // name
				java.util.Date.class.getName(), // type
				ServerInfoVODesc.THE_STOP_TIME_OF_THE_SERVER,// description
				true, // isReadable
				false, // isWriteable
				false,// isIs
				stopTimeDesc// descriptor
		);

		// ---------------------------------------------------------------

		// --------------Construct MBeanOperationInfo-------------------
		MBeanParameterInfo[] voidSignature = null;
		ModelMBeanOperationInfo[] operationInfo = new ModelMBeanOperationInfo[4];

		// - getStartTime
		Descriptor getStartTimeDesc = new DescriptorSupport(
				new String[] { "name=getStartTime", "descriptorType=operation",
						"role=getter", "enabled=true" });
		operationInfo[0] = new ModelMBeanOperationInfo("getStartTime", // name
				"Retrieves the Start Time", // description
				voidSignature, // MBeanParameterInfo[]
				java.util.Date.class.getName(), // type
				ModelMBeanOperationInfo.INFO, // impact
				getStartTimeDesc // desc
		);

		// - getStopTime
		Descriptor getStopTimeDesc = new DescriptorSupport(new String[] {
				"name=getStopTime", "descriptorType=operation", "role=getter", "enabled=true"  });
		operationInfo[1] = new ModelMBeanOperationInfo("getStopTime", // name
				"Retrieves the Stop Time", // description
				voidSignature, // MBeanParameterInfo[]
				java.util.Date.class.getName(), // type
				ModelMBeanOperationInfo.INFO, // impact
				getStopTimeDesc // desc
		);

		// - stopServer
		Descriptor stopServerDesc = new DescriptorSupport(
				new String[] { "name=stopServer", "descriptorType=operation",
						"role=operation", "enabled=true"});
		operationInfo[2] = new ModelMBeanOperationInfo(
				ServerInfoVOOperations.STOP_SERVER, // name
				ServerInfoVODesc.TO_STOP_THE_SERVER, // description
				voidSignature, // MBeanParameterInfo[]
				Void.TYPE.getName(), // type
				ModelMBeanOperationInfo.ACTION, // impact
				stopServerDesc // desc
		);

		// - startServer
		Descriptor startServerDesc = new DescriptorSupport(new String[] {
				"name=startServer", "descriptorType=operation",
				"role=operation", "enabled=true"});
		operationInfo[3] = new ModelMBeanOperationInfo(
				ServerInfoVOOperations.START_SERVER, // name
				ServerInfoVODesc.TO_START_THE_SERVER, // description
				voidSignature, // MBeanParameterInfo[]
				Void.TYPE.getName(), // type
				ModelMBeanOperationInfo.ACTION, // impact
				startServerDesc // desc
		);

		// --------------------------------------------------------------
		// --------------Construct MBeanNotificationInfo-------------------
		ModelMBeanNotificationInfo[] notificationInfo = new ModelMBeanNotificationInfo[0];
		// -------------------------------------------------------------
		// --------------Construct MBeanInfo-------------------
		ModelMBeanInfo _MBeanInfo = new ModelMBeanInfoSupport(
				ServerInfoVO.class.getName(), //
				ServerInfoVO.class.getName(), // description
				attributeInfo, // MBeanAttributeInfo[]
				constructorInfo, // MBeanConstructorInfo[]
				operationInfo, // MBeanOperationInfo[]
				notificationInfo // MBeanNotificationInfo[]
		);
		// ---------------------------------------------------------

		return _MBeanInfo;

	}
	
	private void createAttributeInfo()
	{
		
	}
	
	private void createOperationInfo()
	{
		
	}

}
