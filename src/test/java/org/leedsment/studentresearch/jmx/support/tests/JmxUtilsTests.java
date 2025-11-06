package org.leedsment.studentresearch.jmx.support.tests;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import junit.framework.TestCase;

import org.leedsmet.studentresearch.core.JdkVersion;
import org.leedsmet.studentresearch.jmx.mbeans.Hello;
import org.leedsmet.studentresearch.jmx.mbeans.HelloMBean;
import org.leedsmet.studentresearch.jmx.mbeans.ServerInfoDynamicMBean;
import org.leedsmet.studentresearch.jmx.support.JmxUtils;

/**
 * @author Joseph Aruja
 */
public class JmxUtilsTests extends TestCase {

	public void testIsMBeanWithDynamicMBean() throws Exception {
		ServerInfoDynamicMBean mbean = new ServerInfoDynamicMBean();
		assertTrue("Dynamic MBean not detected correctly", JmxUtils
				.isMBean(mbean.getClass()));
	}

	public void testIsMBeanWithStandardMBeanWrapper() throws Exception {
		StandardMBean mbean = new StandardMBean(new Hello(), HelloMBean.class);
		assertTrue("Standard MBean not detected correctly", JmxUtils
				.isMBean(mbean.getClass()));
	}

	public void testIsMBeanWithStandardMBeanInherited() throws Exception {
		StandardMBean mbean = new StandardMBeanImpl();
		assertTrue("Standard MBean not detected correctly", JmxUtils
				.isMBean(mbean.getClass()));
	}

	public void testNotAnMBean() throws Exception {
		assertFalse("Object incorrectly identified as an MBean", JmxUtils
				.isMBean(Object.class));
	}

	public void testSimpleMBean() throws Exception {
		Foo foo = new Foo();
		assertTrue("Simple MBean not detected correctly", JmxUtils.isMBean(foo
				.getClass()));
	}

	public void testSimpleMBeanThroughInheritance() throws Exception {
		Bar bar = new Bar();
		Abc abc = new Abc();
		assertTrue("Simple MBean (through inheritance) not detected correctly",
				JmxUtils.isMBean(bar.getClass()));
		assertTrue(
				"Simple MBean (through 2 levels of inheritance) not detected correctly",
				JmxUtils.isMBean(abc.getClass()));
	}

	public void testLocatePlatformMBeanServer() {
		if (JdkVersion.getMajorJavaVersion() < JdkVersion.JAVA_15) {
			return;
		}

		MBeanServer server = null;
		try {
			server = JmxUtils.locateMBeanServer();
		} finally {
			if (server != null) {
				MBeanServerFactory.releaseMBeanServer(server);
			}
		}
	}

	public static class StandardMBeanImpl extends StandardMBean implements
			HelloMBean {

		public StandardMBeanImpl() throws NotCompliantMBeanException {
			super(HelloMBean.class);
		}

		public int add(int x, int y) {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getCacheSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void sayHello() {
			// TODO Auto-generated method stub

		}

		public void setCacheSize(int size) {
			// TODO Auto-generated method stub

		}

	}

	public static interface FooMBean {

		String getName();
	}

	public static class Foo implements FooMBean {

		public String getName() {
			return "Joe";
		}
	}

	public static class Bar extends Foo {

	}

	public static class Abc extends Bar {

	}

}
