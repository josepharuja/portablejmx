/*
 * Created on 22-Jul-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.leedsmet.studentresearch.jmx.mbeans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author josepha
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestPOJO{
	
	/**
	 * <code>Log</code> instance for this class.
	 */
	protected final Log logger = LogFactory.getLog(getClass());
	
	public  TestPOJO()
	{
		
	}

	public void test()
	{		
		logger.info("Invoked method test of TestPOJO :"+getClass().getName());
		
	}

}
