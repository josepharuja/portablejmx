package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter;
import org.leedsmet.studentresearch.jmx.export.ServerInfoModelMBeanManager;
import org.leedsmet.studentresearch.jmx.support.ObjectNameManager;
import org.leedsmet.studentresearch.jmx.valueobjects.ServerInfoVO;
import org.leedsmet.studentresearch.util.Constants.Exporters;
import org.leedsmet.studentresearch.util.Constants.Misc;
import org.leedsmet.studentresearch.util.Constants.NameKeyList;
import org.leedsmet.studentresearch.util.Constants.PageNames;
import org.leedsmet.studentresearch.util.Constants.TypesAndNames;
import org.leedsmet.studentresearch.util.Constants.UserActions;

/**
 * This Servlet is used for exporting MBeans during application startup after retrieving
 * the <code>GenericMBeanExporter</code> from <code>ServletContext</code>
 * 
 * The <code>GenericMBeanExporter</code> is created and stored in servlet context by
 * <code>JMXServletContextImplListener</code>
 * 
 * @see #org.leedsmet.studentresearch.jmx.export.GenericMBeanExporter
 * @see #javax.servlet.ServletContext
 * @see #init(ServletConfig)
 * @see #exportMBeans(ServletConfig)
 * 
 * @author Joseph Aruja
 *
 */
public class JMXPortabilityImplServlet extends HttpServlet {

	private static final long serialVersionUID = 663492521275995848L;

	protected final org.apache.commons.logging.Log logger = LogFactory
			.getLog(getClass());

	private Hashtable<String, Action> actions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		doProcess(httpServletRequest, httpServletResponse);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		doProcess(httpServletRequest, httpServletResponse);
	}

	/**
	 * @param httpServletRequest
	 * @param httpServletResponse
	 */
	private void doProcess(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		logger.info("doProcess start");

		String actionName = httpServletRequest.getParameter(Misc.ACTION);
		logger.info("actionName : " + actionName);
		if (actionName == null) {

			/*
			 * httpServletResponse
			 * .sendError(HttpServletResponse.SC_NOT_ACCEPTABLE); return;
			 */
			ActionUtils utils = new ActionUtils();
			utils.forward(PageNames.LOGIN_JSP, httpServletRequest,
					httpServletResponse);
			return;
		}

		Action action = (Action) actions.get(actionName);
		logger.info("Action class retrived :" + action.getClass().getName());
		if (action == null) {
			httpServletResponse
					.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}

		// Use the login action if the user is not authenticated
		if (!isAuthenticated(httpServletRequest)
				&& !(UserActions.AUTHENTICATE.equals(actionName) || UserActions.LOGOUT
						.equals(actionName))) {
			action = (Action) actions.get(UserActions.LOGIN);
		}
		action.perform(this, httpServletRequest, httpServletResponse);

		logger.info("doProcess end");

	}

	private static void addValidUsersInContext(ServletContext servletContext) {
		HashMap<String, String> validUsers = new HashMap<String, String>();
		validUsers.put("joseph", "aruja");
		validUsers.put("eamonn", "mcmanus");
		validUsers.put("steve", "cockerill");
		validUsers.put("ebbi", "shaghouei");
		validUsers.put("graham", "orange");

		servletContext.setAttribute(Misc.VALID_USERS, validUsers);
	}

	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * @see #exportMBeans(ServletConfig)
	 * @see #addValidUsersInContext(ServletContext)
	 * @see #init(ServletConfig)
	 * 
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.info("init start");
		try {
			exportMBeans(servletConfig);
			addValidUsersInContext(servletConfig.getServletContext());
			initUserActions();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("init end");
	}

	/**
	 * Put all the Handlers for User Interaction
	 */
	private void initUserActions() {
		actions = new Hashtable<String, Action>();
		actions.put(UserActions.LOGOUT, new LogoutAction());
		actions.put(UserActions.LOGIN, new LoginAction());
		actions.put(UserActions.AUTHENTICATE, new AuthenticateAction());
		actions.put(UserActions.SHOW_PAGE, new ShowPageAction());

		actions.put(UserActions.JAMON_ADMIN, new JAMonAdminAction());

	}

	/**
	 * This method is used to read servlet init parameters or a config file
	 * to read and configuring the MBeans
	 * 
	 * @param servletConfig
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws NotCompliantMBeanException
	 * @throws InstanceAlreadyExistsException
	 */
	private void exportMBeans(ServletConfig servletConfig)
			throws MalformedObjectNameException,
			InstanceAlreadyExistsException, NotCompliantMBeanException,
			NullPointerException {

		try {
			ServletContext servletContext = servletConfig.getServletContext();
			logger.info("servletContext : " + servletContext);
			// ---Retrieve the Exporter from the ServletContext
			GenericMBeanExporter genericMBeanExporter = (GenericMBeanExporter) servletContext
					.getAttribute(Exporters.GENERIC_JMX_EXPORTER);
			logger.info("genericMBeanExporter : " + genericMBeanExporter);
			demonstrateFramework(genericMBeanExporter);
			// ------------------------------------------------------------------------------------
			ServerInfoModelMBeanManager serverInfoModelMBeanManager = (ServerInfoModelMBeanManager) servletContext
					.getAttribute(Exporters.ServerInfo_ModelMBeanManager);
			demonstrateInstrumentation(serverInfoModelMBeanManager);

		} catch (ReflectionException e) {
			e.printStackTrace();
		} catch (MBeanException e) {
			e.printStackTrace();
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeOperationsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTargetObjectTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param genericMBeanExporter
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws ReflectionException
	 * @throws MBeanException
	 */
	private void demonstrateFramework(GenericMBeanExporter genericMBeanExporter)
			throws MalformedObjectNameException, NullPointerException,
			ReflectionException, MBeanException {
		logger.info("demonstrateFramework start");

		ObjectName mBeanObjectName = null;
		// Creating a Model MBean
		mBeanObjectName = ObjectNameManager.getObjectNameforApplicationLevel(
				NameKeyList.DOMAIN_NAME_FRAMEWORK,
				TypesAndNames.TEST_POJO_TYPE, null);
		genericMBeanExporter.exportMBean(TypesAndNames.TEST_POJO_TYPE,
				mBeanObjectName);

		// Creating a Standard MBean
		mBeanObjectName = ObjectNameManager.getObjectNameforApplicationLevel(
				NameKeyList.DOMAIN_NAME_FRAMEWORK,
				TypesAndNames.HELLO_MBEAN_TYPE, null);
		genericMBeanExporter.exportMBean(TypesAndNames.HELLO_MBEAN_IMPL_NAME,
				mBeanObjectName);

		// Creating a Dynamic MBean
		mBeanObjectName = ObjectNameManager.getObjectNameforApplicationLevel(
				NameKeyList.DOMAIN_NAME_FRAMEWORK,
				TypesAndNames.SERVERINFO_DYNAMICMBEAN_TYPE, null);
		genericMBeanExporter.exportMBean(
				TypesAndNames.SERVERINFO_DYNAMICMBEAN_TYPE, mBeanObjectName);
		logger.info("demonstrateFramework end");

	}

	/**
	 * This method is used for demonstrataion of various instrumentation using
	 * standard, dynamic and model MBeans without using the framework support
	 * using it's own Assembler and things
	 * 
	 * @param genericMBeanExporter
	 * @throws NullPointerException
	 * @throws JMException
	 * @throws InvalidTargetObjectTypeException 
	 * @throws RuntimeOperationsException 
	 */
	private void demonstrateInstrumentation(
			ServerInfoModelMBeanManager serverInfoModelMBeanManager)
			throws NullPointerException, JMException, RuntimeOperationsException, InvalidTargetObjectTypeException {

		logger.info("demonstrateInstrumentation start");
		
		ObjectName mBeanObjectName = ObjectNameManager
				.getObjectNameforApplicationLevel(
						NameKeyList.DOMAIN_NAME_INST_EXAMPLES,
						TypesAndNames.SERVERINFO_MODELMBEAN_TYPE, null);

		ServerInfoVO serverInfoVO = new ServerInfoVO();
		serverInfoModelMBeanManager.exportMBean(mBeanObjectName, serverInfoVO);
		
		logger.info("demonstrateInstrumentation end");

	}

	/**
	 * Returns true if the session contains the authentication token.
	 */
	private boolean isAuthenticated(HttpServletRequest request) {
		boolean isAuthenticated = false;
		HttpSession session = request.getSession();
		if (session.getAttribute(Misc.VALID_USER) != null) {
			isAuthenticated = true;
		}
		return isAuthenticated;
	}

}
