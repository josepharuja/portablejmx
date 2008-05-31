package org.leedsmet.studentresearch.jmx.assembler.tests;

import javax.management.Attribute;
import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ObjectInstance;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

import org.leedsmet.studentresearch.jmx.assembler.GenericMBeanAssembler;

/**
 * @author Joseph Aruja
 */
public  class GenericAssemblerTests extends AbstractMBeanCreator {

	protected static final String AGE_ATTRIBUTE = "Age";

	protected static final String NAME_ATTRIBUTE = "Name";



	public void testMBeanRegistration() throws Exception {
		// beans are registered at this point - just grab them from the server
		ObjectInstance instance = getObjectInstance();
		assertNotNull("Bean should not be null", instance);
	}

	public void testRegisterOperations() throws Exception {
		MBeanInfo inf = getMBeanInfo();
		assertEquals("Incorrect number of operations registered",
				getExpectedOperationCount(), inf.getOperations().length);
	}

	public void testRegisterAttributes() throws Exception {
		MBeanInfo inf = getMBeanInfo();
		assertEquals("Incorrect number of attributes registered",
				getExpectedAttributeCount(), inf.getAttributes().length);
	}

	public void testGetMBeanInfo() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		assertNotNull("MBeanInfo should not be null", info);
	}

	public void testGetMBeanAttributeInfo() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		MBeanAttributeInfo[] inf = info.getAttributes();
		assertEquals("Invalid number of Attributes returned",
				getExpectedAttributeCount(), inf.length);

		for (int x = 0; x < inf.length; x++) {
			assertNotNull("MBeanAttributeInfo should not be null", inf[x]);
			assertNotNull(
					"Description for MBeanAttributeInfo should not be null",
					inf[x].getDescription());
		}
	}

	public void testGetMBeanOperationInfo() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();
		MBeanOperationInfo[] inf = info.getOperations();
		assertEquals("Invalid number of Operations returned",
				getExpectedOperationCount(), inf.length);

		for (int x = 0; x < inf.length; x++) {
			System.out.println(inf[x]);
			assertNotNull("MBeanOperationInfo should not be null", inf[x]);
			assertNotNull(
					"Description for MBeanOperationInfo should not be null",
					inf[x].getDescription());
		}
	}

	public void testDescriptionNotNull() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();

		assertNotNull("The MBean description should not be null",
				info.getDescription());
	}

	public void testSetAttribute() throws Exception {
		
		getServer().setAttribute(getObjectName(), new Attribute(NAME_ATTRIBUTE, "Martin Justin"));
		
		assertEquals("Martin Justin", studentMBean.getName());
	}

	public void testGetAttribute() throws Exception {
		
		studentMBean.setName("John Smith");
		Object val = getServer().getAttribute(getObjectName(), NAME_ATTRIBUTE);
		assertEquals("Incorrect result", "John Smith", val);
	}

	public void testOperationInvocation() throws Exception{
		
		Object result = getServer().invoke(getObjectName(), "addMarks",
				new Object[] {new Integer(20), new Integer(30)}, new String[] {"int", "int"});
	assertEquals("Incorrect result", new Integer(50), result);
	}

	public void testAttributeInfoHasDescriptors() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();

		ModelMBeanAttributeInfo attr = info.getAttribute(NAME_ATTRIBUTE);
		Descriptor desc = attr.getDescriptor();
		assertNotNull("getMethod field should not be null",
				desc.getFieldValue("getMethod"));
		assertNotNull("setMethod field should not be null",
				desc.getFieldValue("setMethod"));
		assertEquals("getMethod field has incorrect value", "getName",
				desc.getFieldValue("getMethod"));
		assertEquals("setMethod field has incorrect value", "setName",
				desc.getFieldValue("setMethod"));
	}

	public void testAttributeHasCorrespondingOperations() throws Exception {
		ModelMBeanInfo info = getMBeanInfoFromAssembler();

		ModelMBeanOperationInfo get = info.getOperation("getName");
		assertNotNull("get operation should not be null", get);
		assertEquals("get operation should have visibility of four",
				(Integer) get.getDescriptor().getFieldValue("visibility"),
				new Integer(4));
		assertEquals("get operation should have role \"getter\"", "getter", get.getDescriptor().getFieldValue("role"));

		ModelMBeanOperationInfo set = info.getOperation("setName");
		assertNotNull("set operation should not be null", set);
		assertEquals("set operation should have visibility of four",
				(Integer) set.getDescriptor().getFieldValue("visibility"),
				new Integer(4));
		assertEquals("set operation should have role \"setter\"", "setter", set.getDescriptor().getFieldValue("role"));
	}

	

	protected ModelMBeanInfo getMBeanInfoFromAssembler() throws Exception {
		
		ModelMBeanInfo info = genericMBeanExporter.getAssembler().getMBeanInfo(studentMBean, getObjectName().toString());
		return info;
	}



	protected MBeanInfo getMBeanInfo() throws Exception {
		return getServer().getMBeanInfo(getObjectName());
	}

	protected ObjectInstance getObjectInstance() throws Exception {
		return getServer().getObjectInstance(getObjectName());
	}

	/**
	 * @return the no of operations in the StudentMBean
	 */
	protected int getExpectedOperationCount() {
		return 1;
	}

	/**
	 * @return the no of attributes in the StudentMBean
	 */
	protected int getExpectedAttributeCount() {
		return 2;
	}

	protected GenericMBeanAssembler getAssembler() {
		return new GenericMBeanAssembler();
	}

}
