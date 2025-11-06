package org.leedsmet.studentresearch.jmx.valueobjects;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * This class stores the information about the server.
 * @author Joseph Aruja
 */
public class ServerInfoVO implements Serializable {

	/**
	 * <code>Log</code> instance for this class.
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	private static final long serialVersionUID = 1L;
    
	
	private boolean isStarted = true;

	private boolean isStartedAndStopped = false;

	boolean showDetails = true;

	private java.util.Date startTime;

	private java.util.Date stopTime;

	public ServerInfoVO() {
		logger.info("Constructing the ServerInfoVO start");
		setStartTime(new java.util.Date(System.currentTimeMillis()));
		logger.info("Constructing the ServerInfoVO end");
	}
	
	/**
	 * @return
	 * StartTime
	 * Model MBeans mandates ModelMBeanOperationInfo as well for attibutes
	 */
	public java.util.Date getStartTime() {
		return startTime;
	}

	/**
	 * @return
	 * StopTime
	 * Model MBeans mandates ModelMBeanOperationInfo as well for attibutes
	 */
	public java.util.Date getStopTime() {
		return stopTime;
	}

	/**
	 * @param startTime
	 * 
	 */
	public void setStartTime(Date startTime) {

		this.startTime = startTime;
		this.stopTime = null;
		isStartedAndStopped = !isStartedAndStopped;

	}

	/**
	 * @param stopTime
	 */
	public void setStopTime(Date stopTime) {
		// TODO Auto-generated method stub
		this.stopTime = stopTime;
		isStartedAndStopped = !isStartedAndStopped;
	}

	/**
	 * This method starts the server  and record the start time.
	 */
	public void startServer() {
		logger.info("Server is starting");
		setStartTime(new java.util.Date(System.currentTimeMillis()));
		isStarted = !isStarted;

	}
    
	/**
	 * This method stops the server  and record the stop time.
	 */
	public void stopServer() {
		logger.info("Thread is Stoping");
		setStopTime(new java.util.Date(System.currentTimeMillis()));
		isStarted = !isStarted;

	}

	/**
	 * Shows the server details if it is not visible
	 * Works only for dynamic MBeans
	 */
	public void showStartStopTime() {
		logger.info("Making the attributes visible");
		showDetails = !showDetails;
	}

	/**
	 * Hides the server details if it is  visible
	 * Works only for dynamic MBeans
	 */
	public void hideStartStopTime() {

		logger.info("Hiding the attributes");
		showDetails = !showDetails;
	}

	/**
	 * @return
	 * Returns true if the server is started
	 */
	public boolean isStarted() {
		return isStarted;
	}

	/**
	 * @return
	 * returns true if the server is started and stopped.
	 */
	public boolean isStartedAndStopped() {
		return isStartedAndStopped;
	}

	/**
	 * @return
	 * Returns true if the current status is true
	 */
	public boolean isShowDetails() {
		return showDetails;
	}

	
	

	/**
	 * @param isStarted
	 */
	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	/**
	 * @param isStartedAndStopped
	 */
	public void setStartedAndStopped(boolean isStartedAndStopped) {
		this.isStartedAndStopped = isStartedAndStopped;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "ServerInfoVO ( "
	        + super.toString() + TAB
	        + "logger = " + this.logger + TAB
	        + "isStarted = " + this.isStarted + TAB
	        + "isStartedAndStopped = " + this.isStartedAndStopped + TAB
	        + "showDetails = " + this.showDetails + TAB
	        + "startTime = " + this.startTime + TAB
	        + "stopTime = " + this.stopTime + TAB
	        + " )";
	
	    return retValue;
	}

}
