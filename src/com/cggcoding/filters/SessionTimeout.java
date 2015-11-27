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

/**
 * Servlet Filter implementation class SessionTimeout
 */
@WebFilter("/*")
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
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		System.out.print("Session Timeout Filter - ");
		
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        
        System.out.println("Request URI: " + request.getRequestURI());
        //System.out.println("Context Path: " + request.getContextPath());

        //XXX This should probably work by having all my restricted servlets use a different path, like /secure/ServletName and have the @WebFilter only apply to that path
        if(!request.getRequestURI().equals("/index.jsp")){
        	if (session == null || session.getAttribute("user") == null) {
	            response.sendRedirect(request.getContextPath() + "/"); // No logged-in user found, so redirect to login page.
	        } else {
	            chain.doFilter(req, res); // Logged-in user found, so just continue request.
	        }
        }else {
        	// pass the request along the filter chain
        	chain.doFilter(req, res);
        }
        
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
