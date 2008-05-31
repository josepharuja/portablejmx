package org.leedsmet.studentresearch.jmx.web.impl;

import javax.management.MBeanServer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter;
import org.leedsmet.studentresearch.jmx.export.ServerInfoModelMBeanManager;
import org.leedsmet.studentresearch.jmx.support.JmxUtils;
import org.leedsmet.studentresearch.util.Constants.Exporters;

/**
 * Bootstrap listener to initialise <code>GenericMBeanExporter</code> and stores it in
 * <code>ServletContext</code>
 * @author Joseph Aruja
 * @see javax.servlet.ServletContextListener
 * @see org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter
 */
public class JMXServletContextImplListener implements ServletContextListener {
	
	protected final Log logger = LogFactory.getLog(getClass());

	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.info("----contextDestroyed start----");
		ServletContext servletContext = servletContextEvent.getServletContext();
		logger.info("servletContext :"+servletContext);
		GenericMBeanExporter genericMBeanExporter = (GenericMBeanExporter)servletContext.getAttribute(Exporters.GENERIC_JMX_EXPORTER);
		//---Removing the MBeans
		genericMBeanExporter.removeMBeans();
		//---Remove the GenericMBeanExporter
		servletContext.removeAttribute(Exporters.GENERIC_JMX_EXPORTER);
		
		//--Remove the ServerInfoModelMBeanManager and MBeans from the ServletContext
		ServerInfoModelMBeanManager serverInfoModelMBeanManager =(ServerInfoModelMBeanManager)servletContext.getAttribute(Exporters.ServerInfo_ModelMBeanManager);
		serverInfoModelMBeanManager.removeMBeans();
		servletContext.removeAttribute(Exporters.ServerInfo_ModelMBeanManager);
		logger.info("----contextDestroyed end----");
	}


	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.info("---contextInitialized start---");
		//--------Set the Framework GenericMBeanExporter in the ServletContext
		ServletContext servletContext = servletContextEvent.getServletContext();
		logger.info("servletContext :"+servletContext);
		GenericMBeanExporter genericMBeanExporter = new GenericMBeanExporter();
		MBeanServer mBeanServer = JmxUtils.locateMBeanServer();		
		logger.info("Located mBeanServer :"+mBeanServer);
		genericMBeanExporter.setServer(mBeanServer);
		//---Set the GenericMBeanExporter in the ServletContext
		servletContext.setAttribute(Exporters.GENERIC_JMX_EXPORTER, genericMBeanExporter);
		
		//--------Set the ServerInfoModelMBeanManager in the ServletContext 
		ServerInfoModelMBeanManager serverInfoModelMBeanManager = new ServerInfoModelMBeanManager();
		serverInfoModelMBeanManager.setServer(mBeanServer);
		servletContext.setAttribute(Exporters.ServerInfo_ModelMBeanManager, serverInfoModelMBeanManager);
		
		logger.info("---contextInitialized end---");
	}

}
