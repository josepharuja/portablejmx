package org.leedsmet.studentresearch.jmx.valueobjects;

import java.util.Date;

/**
 * This class stores the details of the user who logs in
 * @author arujajo
 *
 */
public class UserVO {

	

	private String userName;

	private String password;
	
	private Date logInTime;
	
	private Date logOutTime;
	
	
   /**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the logInTime
	 */
	public Date getLogInTime() {
		return logInTime;
	}

	/**
	 * @param logInTime the logInTime to set
	 */
	public void setLogInTime(Date logInTime) {
		this.logInTime = logInTime;
	}

	/**
	 * @return the logOutTime
	 */
	public Date getLogOutTime() {
		return logOutTime;
	}

	/**
	 * @param logOutTime the logOutTime to set
	 */
	public void setLogOutTime(Date logOutTime) {
		this.logOutTime = logOutTime;
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
	    
	    retValue = "UserVO ( "
	        + super.toString() + TAB
	        + "userName = " + this.userName + TAB
	        //+ "password = " + this.password + TAB
	        + "logInTime = " + this.logInTime + TAB
	        + "logOutTime = " + this.logOutTime + TAB
	        + " )";
	
	    return retValue;
	}

	

}
