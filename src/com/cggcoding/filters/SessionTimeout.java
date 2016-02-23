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
		
		System.out.print("Session Timeout Filter - ");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        boolean userPresent = false;
        boolean authorized = false;
        
        System.out.println("Request URI: " + httpRequest.getRequestURI() + " || Path: " + httpRequest.getParameter("path") + " || RequestedAction: " + httpRequest.getParameter("requestedAction"));
        //System.out.println("Context Path: " + request.getContextPath());

    	if (session == null) {
    		System.out.println("The session has timed out.  Please log in again.");
    		
    		httpRequest.setAttribute("errorMessage", "The session has timed out.  Please log in again.");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
    	} else {
    		User user = (User)session.getAttribute("user");
    		
    		if (user == null){
    			System.out.println("No logged-in user found, redirecting to login page.");
        		userPresent = false;
        		httpRequest.setAttribute("errorMessage", "No logged-in user found, redirecting to login page.");
	            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp"); // No logged-in user found, so redirect to login page.
    		}else{
    			userPresent = true;
    			//do authorization checks
    			int treatmentPlanID = ParameterUtils.parseIntParameter(httpRequest, "treatmentPlanID");
    			int stageID = ParameterUtils.parseIntParameter(httpRequest, "stageID");
    			int taskID = ParameterUtils.parseIntParameter(httpRequest, "taskID");
    			
    			System.out.println("Athorization Check - txPlanID: " + treatmentPlanID + "; stageID: " + stageID + "; taskID: " + taskID);
    			
    			//do an auth check on the most senior ID present
    			if(treatmentPlanID!=0){
    				System.out.println("check for TxPlan auth.");
    				if(user.isAuthorizedForTreatmentPlan(treatmentPlanID)){
    					authorized = true;
    				}
    				
    			} else if (stageID!=0){
    				System.out.println("check for Stage auth.");
    				if(user.isAuthorizedForStage(stageID)){
    					authorized = true;
    				}
    				
    			} else if (taskID!=0){
    				System.out.println("check for Task auth.");
    				if(user.isAuthorizedForTask(taskID)){
    					authorized = true;
    				}
    			} else {
    				//separate check for inserting to database since won't have any ids available at that time
    				String path = httpRequest.getParameter("path");
    				
    				if(path!=null){
    					switch(path){
        				case "test":
        					
        					break;
        					
        				default:
        					//none of the cases have been met so consider as unauthorized access attempt and forward to unauthorizied access error page
        					System.out.println("Unauthorized Access.");
        					//httpResponse.sendRedirect(httpRequest.getContextPath() + "/unauthorized-access.jsp");
        					//httpRequest.getRequestDispatcher("/secure/unauthorized-access.jsp").forward(request, response);
        				}
    				} else {
    					//none of the ID fields are present and there is no path, so forward to unauthorized access
    					System.out.println("Unauthorized Access.");
    					//httpResponse.sendRedirect(httpRequest.getContextPath() + "/secure/unauthorized-access.jsp");
    				}
    				
    				
    			}
    			
    			//if(userPresent && authorized){
    				chain.doFilter(request, response); // Logged-in user found and request authorized, so just continue chain.
    			//}
    			
    		}
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
