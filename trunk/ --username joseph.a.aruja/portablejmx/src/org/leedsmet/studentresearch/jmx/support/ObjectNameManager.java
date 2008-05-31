package org.leedsmet.studentresearch.jmx.support;

import java.util.Hashtable;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.http.HttpSession;

import org.leedsmet.studentresearch.util.Constants;

/**
 * Helper class for the creation of {@link javax.management.ObjectName}
 * instances for application and user session level
 * @author Joseph Aruja
 * @see javax.management.ObjectName#getInstance(String)
 */
public class ObjectNameManager {

	/**
	 * This method creates an unique object name for an application
	 * Creates a default name based on the type and class name
	 * @param domainName
	 * @param className
	 * @return
	 * @throws NullPointerException
	 * @throws MalformedObjectNameException
	 */
	public static ObjectName getObjectNameforApplicationLevel(
			String domainName, String className, Hashtable<String, String> properties)
			throws MalformedObjectNameException, NullPointerException {
		if (properties == null || properties.size() == 0) {
			properties = new Hashtable<String, String>();
			properties.put(Constants.NameKeyList.TYPE, className);
			properties.put(Constants.NameKeyList.CREATION_TIME, ""
					+ System.currentTimeMillis());
		}
		ObjectName mbName = new ObjectName(domainName,properties);
		return mbName;
	}
	
	/**
	 * This method creates an unique object name for an usersession
	 * @param domainName
	 * @param className
	 * @param properties
	 * @param httpSession
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public static ObjectName getObjectNameForUserSession(
			String domainName, String className, Hashtable<String, String> properties,HttpSession httpSession)
			throws MalformedObjectNameException, NullPointerException {
		if (properties == null || properties.size() == 0) {
			properties = new Hashtable<String, String>();
			properties.put(Constants.NameKeyList.TYPE, className);
			if(httpSession != null)
			{
				properties.put(Constants.NameKeyList.SESSION_ID,httpSession.getId());
			}
		}
		ObjectName mbName = new ObjectName(domainName,properties);
		return mbName;
	}

}
