package org.leedsmet.studentresearch.jmx.assembler.tests;

import javax.management.ObjectName;

import org.leedsmet.studentresearch.jmx.BaseMBeanServerTests;
import org.leedsmet.studentresearch.jmx.assembler.tests.mbeans.Student;
import org.leedsmet.studentresearch.jmx.assembler.tests.mbeans.StudentMBean;
import org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter;
import org.leedsmet.studentresearch.jmx.support.ObjectNameManager;

/***
 * This class registers an MBean for Assembler Test.
 *
 * @author arujajo
 * @see BaseMBeanServerTests
 */
public abstract class AbstractMBeanCreator extends
		org.leedsmet.studentresearch.jmx.BaseMBeanServerTests {
	protected GenericMBeanExporter genericMBeanExporter;

	private final String domainName = "jmxportabiliy";
	
	ObjectName objectName = null;

	protected StudentMBean studentMBean = new Student();

	protected final void onSetUp() throws Exception {
		genericMBeanExporter = new GenericMBeanExporter();
		genericMBeanExporter.setServer(server);
		objectName = ObjectNameManager.getObjectNameforApplicationLevel(domainName,Student.class.getName(), null);
		genericMBeanExporter.exportMBean(studentMBean, getObjectName());
	}

	protected final void onTearDown() throws Exception {
		genericMBeanExporter.removeMBeans();
		genericMBeanExporter = null;
	}

	protected ObjectName getObjectName() {
		
		return objectName;
	}

}
