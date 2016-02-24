package com.cggcoding.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.models.User;
import com.cggcoding.utils.ParameterUtils;

/**
 * Servlet Filter implementation class SessionTimeout
 */
@WebFilter("/secure/*")
public class SessionTimeout implements Filter {

    /**
     * Default constructor. 
     */
    public SessionTimeout() {
       
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//XXX The session and user checking here is code is repeated in the Authorization filter. 
        //Preferably, this would not be the case, but since it is not possible to specify the order in which filters are applied, 
        //these checks need to be in both files
		System.out.print("Session Timeout Filter - ");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        System.out.println("Request URI: " + httpRequest.getRequestURI());
        System.out.println("   Path: " + httpRequest.getParameter("path"));
        System.out.println("   RequestedAction: " + httpRequest.getParameter("requestedAction"));
    	if (session == null) {
    		System.out.println("The session has timed out.  Please log in again.");
    		
    		httpRequest.setAttribute("errorMessage", "The session has timed out.  Please log in again.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
    	} else {
    		User user = (User)session.getAttribute("user");
    		
    		if (user == null){
    			System.out.println("No logged-in user found, redirecting to login page.");
        		httpRequest.setAttribute("errorMessage", "No logged-in user found, redirecting to login page.");
	            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp"); // No logged-in user found, so redirect to login page.
    		}else{
    			chain.doFilter(request, response); // Logged-in user found, so just continue chain.
    			
    		}
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
