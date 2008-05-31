package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.leedsmet.studentresearch.util.Constants;
import org.leedsmet.studentresearch.util.Constants.PageNames;

public class LogoutAction implements Action {
	
	private ActionUtils actionUtils = new ActionUtils();

	public void perform(HttpServlet servlet, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpSession httpSession = httpServletRequest.getSession();
		if (httpSession != null) {
			httpSession.removeAttribute(Constants.Misc.VALID_USER);
			httpSession.invalidate();
			httpServletRequest.setAttribute("USR_MSG", "You have logged out");
		}
		else
		{
			httpServletRequest.setAttribute("USR_MSG", "Your session is timed out");
		}
		actionUtils.forward(PageNames.LOGOUT_JSP,
				httpServletRequest, httpServletResponse);
	}

}
