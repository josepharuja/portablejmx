/*
 * Source : Constants.java
 *
 * Changes log
 * --------------------------------------------------------------------------------------
 * Sr No | Description           		| Date            | By        | Ver No. | Remarks |.
 * --------------------------------------------------------------------------------------
 *
 */

package org.leedsmet.studentresearch.util;

/**
 * This class stores all the constants needed for the framework
 * @author Joseph Aruja
 * 
 */
public class Constants {

	public static final String LOG4J_PROPERTIES = "/log4j.properties";

	public static final String RESOURCE_BUNDLE_NAME = "portability.properties";

	public interface Exporters {

		public static final String GENERIC_JMX_EXPORTER = "GENERIC_JMX_EXPORTER";
		public static final String ServerInfo_ModelMBeanManager = "ServerInfo_ModelMBeanManager";

	}

	public interface TypesAndNames {
		
		//MBean used for demonstrating instrumentation using model MBean
		//It can be a normal POJO
		public static final String TEST_POJO_TYPE = "org.leedsmet.studentresearch.jmx.mbeans.TestPOJO";

		//MBean used for demonstrating instrumentation using standard MBean
		//GenericMBeanExporter has the flexibility of specifying the MBean Types
		public static final String HELLO_MBEAN_TYPE = "org.leedsmet.studentresearch.jmx.mbeans.HelloMBean";

		public static final String HELLO_MBEAN_IMPL_NAME = "org.leedsmet.studentresearch.jmx.mbeans.Hello";		

		//MBean used for demonstrating instrumentation using dynamic MBean
		//GenericMBeanExporter has the flexibility of specifying the MBean Types
		public static final String SERVERINFO_DYNAMICMBEAN_TYPE = "org.leedsmet.studentresearch.jmx.mbeans.ServerInfoDynamicMBean";
		
		//Value used for exposing session data at user level
		public static final String USER_VO_TYPE = "org.leedsmet.studentresearch.jmx.valueobjects.UserVO";
		
		
		public static final String SERVERINFO_MODELMBEAN_TYPE = "PortableDynamicMBean";

	}

	public interface NameKeyList {
		public static final String DOMAIN_NAME_FRAMEWORK = "jmxportability-framework";
		
		public static final String DOMAIN_NAME_INST_EXAMPLES = "jmxportability-instr-examples";

		String TYPE = "type";

		String CREATION_TIME = "creationTime";

		String SESSION_ID = "sessionId";

	}

	public interface Misc {

		public static final String ACTION = "action";

		public static final String VALID_USER = "validUser";

		public static final String USER_MSG = "UserMsg";

		public static final String USER_PASSWORD = "userPassword";

		public static final String USER_NAME = "userName";

		public static final String VALID_USERS = "VALID_USERS";

		public static final String ORIG_URL = "origURL";

	}

	public interface UserActions {
		public static final String LOGIN = "login";

		public static final String LOGOUT = "logout";

		public static final String AUTHENTICATE = "authenticate";

		public static final String UPDATE_DTLS = "updateDetails";

		public static final String SHOW_PAGE = "showPage";

		public static final String JAMON_ADMIN = "jamonadmin";

	}
	
	public interface MBeanTypes
	{
		public static final int STANDARD_MBEAN = 0;
		
		public static final int DYNAMIC_MBEAN = 1;
		
		public static final int MODEL_MBEAN = 2;
		
		public static final int PLATFORM_MBEAN = 3;
		
		
	}
	
	public interface PageNames
	{

		public static final String LOGIN_JSP = "Login.jsp";
		
		public static final String UPDATE_DETAILS_JSP = "UpdateDetails_test.jsp";
		public static final String LOGOUT_JSP = "Logout.jsp";

		public static final String JAMON_ADMIN = "jamonadmin.jsp";
		
	}

}