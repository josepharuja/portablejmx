package org.leedsmet.studentresearch.jmx.mbeans;

/**
 * @author arujajo
 *
 */
public interface ServerInfoStandardMBean {

	public java.util.Date getStartTime();

	public java.util.Date getStopTime();

	public void startServer();

	public void stopServer();
	
	public String getJVMStatistics();
}
