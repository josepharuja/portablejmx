package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.leedsmet.studentresearch.util.Constants.Misc;

/**
 * @author arujajo
 * 
 */
public class LoginAction implements Action {

	private ActionUtils utils = new ActionUtils();

	@SuppressWarnings("unchecked")
	public void perform(HttpServlet httpServlet,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException,
			ServletException {

		String origURL = HttpUtils.getRequestURL(httpServletRequest).toString();
		String queryString = httpServletRequest.getQueryString();
		if (queryString != null) {
			origURL += "?" + queryString;
		}
		String loginURL = "login.jsp" + "?" + Misc.USER_MSG + "="
				+ URLEncoder.encode("Please Login First");
		utils.forward(loginURL, httpServletRequest, httpServletResponse);
	}

}
