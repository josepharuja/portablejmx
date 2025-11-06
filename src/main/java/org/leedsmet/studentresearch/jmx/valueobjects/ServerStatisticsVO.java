package org.leedsmet.studentresearch.jmx.valueobjects;

/**
 * 
 * This class stores the information about the JVM statistics.
 * 
 * @author Joseph Aruja
 */
public class ServerStatisticsVO {
	private String jvmStatistics;

	public ServerStatisticsVO() {

	}

	/**
	 * @return the jvmStatistics
	 */
	public String getJvmStatistics() {
		return jvmStatistics;
	}

	/**
	 * @param jvmStatistics
	 *            the jvmStatistics to set
	 */
	public void setJvmStatistics(String jvmStatistics) {
		this.jvmStatistics = jvmStatistics;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final String TAB = "    ";

		String retValue = "";

		retValue = "ServerStatisticsVO ( " + super.toString() + TAB
				+ "jvmStatistics = " + this.jvmStatistics + TAB + " )";

		return retValue;
	}

}
