package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.LogFactory;
import org.leedsmet.studentresearch.jmx.valueobjects.UserVO;
import org.leedsmet.studentresearch.util.Constants.Misc;
import org.leedsmet.studentresearch.util.Constants.PageNames;

public class AuthenticateAction implements Action {

	private ActionUtils actionUtils = new ActionUtils();

	protected final org.apache.commons.logging.Log logger = LogFactory
			.getLog(getClass());

	@SuppressWarnings("unchecked")
	public void perform(HttpServlet httpServlet,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException,
			ServletException {

		String userName = httpServletRequest.getParameter(Misc.USER_NAME);
		String userPassword = httpServletRequest
				.getParameter(Misc.USER_PASSWORD);

		logger.info("userName : " + userName);
		logger.info("userPassword : " + userPassword);

		if (Validator.isValueSet(userName)
				&& Validator.isValueSet(userPassword)) {
			
			//Forcing to create a new session for demonstration
			HttpSession httpSession = httpServletRequest.getSession(true);
			ServletContext servletContext = httpSession.getServletContext();

			HashMap<String, String> validUsers = (HashMap<String, String>) servletContext
					.getAttribute(Misc.VALID_USERS);

			if (validUsers.containsKey(userName.toLowerCase())) {
				String origUserPassword = validUsers
						.get(userName.toLowerCase());
				if (origUserPassword.equals(userPassword)) {

					UserVO userVO = new UserVO();
					userVO.setUserName(userName);
					userVO.setPassword(userPassword);
					userVO.setLogInTime(new Date(System.currentTimeMillis()));

					httpSession.setAttribute(Misc.VALID_USER, userVO);
					// Redirect to the originally requested URL or main
					actionUtils.forward(PageNames.UPDATE_DETAILS_JSP,
							httpServletRequest, httpServletResponse);
				} else {
					String loginURL = "login.jsp"
							+ "?"
							+ Misc.USER_MSG
							+ "="
							+ URLEncoder
									.encode("Invalid User Name or Password");

					httpServletResponse.sendRedirect(loginURL);
				}
			} else {
				String loginURL = "login.jsp" + "?" + Misc.USER_MSG + "="
						+ URLEncoder.encode("Invalid User Name or Password");
				httpServletResponse.sendRedirect(loginURL);
			}

		} else {
			String loginURL = "login.jsp" + "?" + Misc.USER_MSG + "="
					+ URLEncoder.encode("Invalid User Name or Password");
			httpServletResponse.sendRedirect(loginURL);
			
			

		}

	}

}
