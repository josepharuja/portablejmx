package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author arujajo
 * 
 */
public interface Action {

	/**
	 * @param servlet
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void perform(HttpServlet httpServlet,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException,
			ServletException;

}
