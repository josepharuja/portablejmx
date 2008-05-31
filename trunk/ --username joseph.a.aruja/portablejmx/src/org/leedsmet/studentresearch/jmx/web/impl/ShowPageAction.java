package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * @author arujajo
 *
 */
public class ShowPageAction implements Action {
    private ActionUtils utils = new ActionUtils();
    
    /**
     * Forwards to the specified JSP page. The reason for using this
     * action instead of requesting a JSP page directly is to let
     * the PBDispatcher handle authentication and access control even 
     * for JSP pages. Note that nothing prevents a user from
     * requesting a JSP page directly, so this is not a secure way
     * to perform access control. What it does give is automatic
     * redirection to the login page for users that are not properly
     * logged in. 
     * <p>
     * In this application, as in most applications with  a servlet as 
     * a controller, accessing a JSP page directly doesn't reveal any 
     * restricted information however; the JSP page only displays 
     * information available in beans, created by the controller.
     */
    public void perform(HttpServlet servlet, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {
        String url = request.getParameter("page");
        if (url == null) {
            throw new ServletException("Missing page info");
        }
        utils.forward(url, request, response);
    }
}