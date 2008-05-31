package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leedsmet.studentresearch.util.Constants.PageNames;

public class JAMonAdminAction implements Action {

	private ActionUtils actionUtils = new ActionUtils();

	public void perform(HttpServlet httpServlet,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		actionUtils.forward(PageNames.JAMON_ADMIN,
				httpServletRequest, httpServletResponse);
		

	}

}
