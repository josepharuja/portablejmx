package org.leedsmet.studentresearch.jmx.mbeans;

import org.leedsmet.studentresearch.jmx.valueobjects.ServerInfoVO;
import org.leedsmet.studentresearch.jmx.valueobjects.ServerStatisticsVO;

/**
 * @author arujajo
 * This class implements a Standard MBean design pattern to expose and manage
 * ServerInfo.
 *
 */
public class ServerInfoStandard implements ServerInfoStandardMBean {

	private ServerInfoVO serverInfoVO;
	private ServerStatisticsVO serverStatisticsVO;

	public ServerInfoStandard() {
		serverInfoVO = new ServerInfoVO();
		serverStatisticsVO = new ServerStatisticsVO();
	}

	public java.util.Date getStartTime() {
		// TODO Auto-generated method stub
		return serverInfoVO.getStartTime();
	}

	public java.util.Date getStopTime() {
		// TODO Auto-generated method stub
		return serverInfoVO.getStopTime();
	}

	public void startServer() {
		serverInfoVO.startServer();
	}

	public void stopServer() {
		serverInfoVO.stopServer();
	}

	public String getJVMStatistics() {
		// TODO Auto-generated method stub
		return serverStatisticsVO.getJvmStatistics();
	}
	
	

}
