package org.leedsmet.studentresearch.jmx.web.impl;

import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter;
import org.leedsmet.studentresearch.jmx.support.ObjectNameManager;
import org.leedsmet.studentresearch.jmx.valueobjects.UserVO;
import org.leedsmet.studentresearch.util.Constants;
import org.leedsmet.studentresearch.util.Constants.Exporters;
import org.leedsmet.studentresearch.util.Constants.Misc;

/**
 * The sesssion Listener implemented to create MBeans for a usersession<code>HttpSession</code>. 
 * <p>
 * Since the framework implementation is based on model MBeans, the same value objects used by the application 
 * can  be exposed to management interface
 * <p>
 * The value object is registered to JMX <code>MBeanServer</code> using <code>GenericMBeanExporter</code>
 * @author Joseph Aruja
 * @see HttpSession
 * @see HttpSessionBindingListener
 * @see HttpSessionAttributeListener
 * @see HttpSessionBindingEvent
 * @see GenericMBeanExporter
 * @see UserVO
 * @see #removeUserAttribute(HttpSessionBindingEvent)
 */
public class JMXSessionImplListener implements HttpSessionBindingListener,
		HttpSessionAttributeListener {

	protected final Log logger = LogFactory.getLog(getClass());

	public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
		//Not Implemented

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 * @see rem
	 */
	public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
		logger.info("valueUnbound start");
		if (httpSessionBindingEvent.getSession().getAttribute(Misc.VALID_USER) != null) {
			removeUserAttribute(httpSessionBindingEvent);
		}
		logger.info("valueUnbound end");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
		logger.info("attributeAdded start");
		String attributeName = httpSessionBindingEvent.getName();
		logger.info("attributeName : " + attributeName);
		if (attributeName.equals(Misc.VALID_USER)) {
			UserVO userVO = (UserVO) httpSessionBindingEvent.getValue();
			logger.info("userVO : " + userVO);
			HttpSession httpSession = httpSessionBindingEvent.getSession();
			ServletContext servletContext = httpSession.getServletContext();
			GenericMBeanExporter genericMBeanExporter = (GenericMBeanExporter) servletContext
					.getAttribute(Exporters.GENERIC_JMX_EXPORTER);

			ObjectName mBeanObjectName = null;

			try {
				mBeanObjectName = ObjectNameManager
						.getObjectNameForUserSession(
								Constants.NameKeyList.DOMAIN_NAME_FRAMEWORK,
								Constants.TypesAndNames.USER_VO_TYPE, null,
								httpSession);

				logger.info("mBeanObjectName : " + mBeanObjectName);

				genericMBeanExporter.exportMBean(userVO, mBeanObjectName);

			} catch (MalformedObjectNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReflectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MBeanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			logger.info("exported MBean : "
					+ Constants.TypesAndNames.USER_VO_TYPE + "with name ->"
					+ mBeanObjectName);

		}
		logger.info("attributeAdded start");

	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
		logger.info("attributeRemoved start");
		removeUserAttribute(httpSessionBindingEvent);
		logger.info("attributeRemoved end");
	}

	public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
		//Not Implemented

	}

	/**
	 * This method removes unregisters the MBean <code>UserVO</code> when the user logs out of the application
	 * 
	 * @param httpSessionBindingEvent
	 */
	private void removeUserAttribute(HttpSessionBindingEvent httpSessionBindingEvent) {
		logger.info("removeUserAttribute start");
		HttpSession httpSession = httpSessionBindingEvent.getSession();
		ServletContext servletContext = httpSession.getServletContext();
		GenericMBeanExporter genericMBeanExporter = (GenericMBeanExporter) servletContext
				.getAttribute(Exporters.GENERIC_JMX_EXPORTER);
		String sessionId = httpSession.getId();

		logger.info("sessionId : " + sessionId);
		ObjectName mBeanObjectName = null;
		try {
			mBeanObjectName = ObjectNameManager.getObjectNameForUserSession(
					Constants.NameKeyList.DOMAIN_NAME_FRAMEWORK,
					Constants.TypesAndNames.USER_VO_TYPE, null, httpSession);

			logger.info("mBeanObjectName : " + mBeanObjectName);

			genericMBeanExporter.removeMBean(mBeanObjectName);
			logger.info("removed MBean : "
					+ Constants.TypesAndNames.USER_VO_TYPE + "with name ->"
					+ mBeanObjectName);
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("removeUserAttribute end");
	}

}
