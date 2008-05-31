package org.leedsment.studentresearch.jmx.export.tests;

import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.leedsmet.studentresearch.jmx.BaseMBeanServerTests;
import org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter;
import org.leedsmet.studentresearch.jmx.support.ObjectNameManager;
import org.leedsmet.studentresearch.util.Constants;

/**
 * Integration tests for the MBeanExporter class.
 * 
 * @author Joseph Aruja
 * 
 */
public class GenericMBeanExporterTests extends BaseMBeanServerTests {

	private final String domainName = "jmxportabiliy";

	/**
	 * @throws Exception
	 */
	public void testStandardMBean() throws Exception {
		GenericMBeanExporter genericMBeanExporter = new GenericMBeanExporter();
		genericMBeanExporter.setServer(server);
		ObjectName objectName = ObjectNameManager
				.getObjectNameforApplicationLevel(domainName,
						Constants.TypesAndNames.HELLO_MBEAN_IMPL_NAME, null);
		genericMBeanExporter.exportMBean(
				Constants.TypesAndNames.HELLO_MBEAN_IMPL_NAME, objectName);
		assertIsRegistered("The bean was not registered with the MBeanServer",
				objectName);
	}

	/**
	 * @throws Exception
	 */
	public void testDynamicMBean() throws Exception {
		GenericMBeanExporter genericMBeanExporter = new GenericMBeanExporter();
		genericMBeanExporter.setServer(server);
		ObjectName objectName = ObjectNameManager
				.getObjectNameforApplicationLevel(domainName,
						Constants.TypesAndNames.SERVERINFO_DYNAMICMBEAN_TYPE,
						null);
		genericMBeanExporter.exportMBean(
				Constants.TypesAndNames.SERVERINFO_DYNAMICMBEAN_TYPE,
				objectName);
		assertIsRegistered("The bean was not registered with the MBeanServer",
				objectName);
	}

	/**
	 * @throws Exception
	 */
	public void testModelMBean() throws Exception {
		GenericMBeanExporter genericMBeanExporter = new GenericMBeanExporter();
		genericMBeanExporter.setServer(server);
		ObjectName objectName = ObjectNameManager
				.getObjectNameforApplicationLevel(domainName,
						Constants.TypesAndNames.USER_VO_TYPE, null);
		genericMBeanExporter.exportMBean(Constants.TypesAndNames.USER_VO_TYPE,
				objectName);
		assertIsRegistered("The bean was not registered with the MBeanServer",
				objectName);
	}

	public void testRegisterIgnoreExisting() throws Exception {

		ObjectName objectName1 = ObjectNameManager
				.getObjectNameforApplicationLevel(domainName, Employee.class
						.getName(), null);

		Employee preRegisteredObject = new Employee();
		preRegisteredObject.setName("Joseph Aruja");
		preRegisteredObject.setAge(31);
		server.registerMBean(preRegisteredObject, objectName1);

		Employee frameworkRegisteredObject = new Employee();
		frameworkRegisteredObject.setName("Martin Justin");
		frameworkRegisteredObject.setAge(28);

		ObjectName objectName2 = ObjectNameManager
				.getObjectNameforApplicationLevel("test", Employee.class
						.getName(), null);

		GenericMBeanExporter genericMBeanExporter = new GenericMBeanExporter();
		genericMBeanExporter.setServer(server);
		genericMBeanExporter
				.setRegistrationBehavior(GenericMBeanExporter.REGISTRATION_IGNORE_EXISTING);

		genericMBeanExporter.exportMBean(preRegisteredObject, objectName1);

		genericMBeanExporter
				.exportMBean(frameworkRegisteredObject, objectName2);

		ObjectInstance instance1 = server.getObjectInstance(objectName1);
		assertNotNull(instance1);
		ObjectInstance instance2 = server.getObjectInstance(objectName2);
		assertNotNull(instance2);

		// should still be the first bean with name Joseph Aruja
		assertEquals("Joseph Aruja", server.getAttribute(objectName1, "Name"));
		assertEquals("31", server.getAttribute(objectName1, "Age").toString());
	}

	public void testRegisterReplaceExisting() throws Exception {
		ObjectName objectName1 = ObjectNameManager
				.getObjectNameforApplicationLevel(domainName, Employee.class
						.getName(), null);

		Employee preRegisteredObject = new Employee();
		preRegisteredObject.setName("Joseph Aruja");
		preRegisteredObject.setAge(31);
		server.registerMBean(preRegisteredObject, objectName1);

		Employee frameworkRegisteredObject = new Employee();
		frameworkRegisteredObject.setName("Martin Justin");
		frameworkRegisteredObject.setAge(28);

		GenericMBeanExporter exporter = new GenericMBeanExporter();
		exporter.setServer(server);
		exporter
				.setRegistrationBehavior(GenericMBeanExporter.REGISTRATION_REPLACE_EXISTING);
		exporter.exportMBean(frameworkRegisteredObject, objectName1);

		ObjectInstance instance = server.getObjectInstance(objectName1);
		assertNotNull(instance);

		// should still be the new bean with name Sally Greenwood
		assertEquals("Martin Justin", server.getAttribute(objectName1, "Name"));
	}

	public static interface EmployeeMBean {

		String getName();

		int getAge();
	}

	public static class Employee implements EmployeeMBean {

		private String name;

		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @param age
		 *            the age to set
		 */
		public void setAge(int age) {
			this.age = age;
		}

		/**
		 * @return the age
		 */
		public int getAge() {
			return age;
		}

	}

}
