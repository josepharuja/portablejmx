package org.leedsmet.studentresearch.jmx;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.leedsment.studentresearch.jmx.export.tests.GenericMBeanExporterTests;
import org.leedsment.studentresearch.jmx.support.tests.JmxUtilsTests;
import org.leedsmet.studentresearch.jmx.assembler.tests.GenericAssemblerTests;

/**
 * @author arujajo
 * Suite for combining all test cases
 * @see JmxUtilsTests
 * @see GenericAssemblerTests
 * @see GenericMBeanExporterTests
 */
public class JMXTestSuite extends TestSuite {
	
	public static Test suite() {

		TestSuite suite = new TestSuite();  
        suite.addTestSuite(JmxUtilsTests.class);
        suite.addTestSuite(GenericAssemblerTests.class);
        suite.addTestSuite(GenericMBeanExporterTests.class);
        return suite;
    }

    /**
     * Runs the test suite using the textual runner.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }


}
