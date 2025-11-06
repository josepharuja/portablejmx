package org.leedsmet.studentresearch.jmx.web.impl;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.jamonapi.*;


public class MonitorFilter extends HttpServlet implements Filter {
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private FilterConfig filterConfig = null;

     //Handle the passed-in FilterConfig
     public void init(FilterConfig filterConfig) {
       this.filterConfig = filterConfig;
     }

     //Process the request/response pair
     public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
       // monitor page hits and processing time
       Monitor monitor = MonitorFactory.start(getURI(request));

       try {
       //call next filter in chain
        filterChain.doFilter(request, response);
       }
       finally {
        monitor.stop();
        
       }
     }

     protected String getURI(ServletRequest request){
       if (request instanceof HttpServletRequest) {
        return ((HttpServletRequest)request).getRequestURI();
       }
       else
        return "Not an HttpServletRequest";
     }

     //Clean up resources
     public void destroy() {
       filterConfig=null;
     }

}

