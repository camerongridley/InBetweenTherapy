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

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.models.User;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Servlet Filter implementation class Authorization
 */
@WebFilter("/secure/treatment-components/*")
public class Authorization implements Filter {

    /**
     * Default constructor. 
     */
    public Authorization() {
        
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
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        

        boolean authorized = true;  //XXX Authentication should be moved to the model.  this method missed authenticating a create request and seems weaker overall
        
        System.out.print("\nAuth Filter: ");
        
        //XXX The session and user checking here is code that also exists in SessionTimeout filter and is repeated here. 
        //Preferably, this would not be the case, but since it is not possible to specify the order in which filters are applied, 
        //these checks need to be in both files
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
    			//do authorization checks
    			int treatmentPlanID = ParameterUtils.parseIntParameter(httpRequest, "treatmentPlanID");
    			int stageID = ParameterUtils.parseIntParameter(httpRequest, "stageID");
    			int taskID = ParameterUtils.parseIntParameter(httpRequest, "taskID");
    			
    			System.out.println("txPlanID: " + treatmentPlanID + "; stageID: " + stageID + "; taskID: " + taskID);
    			
    			//do an auth check on the most senior ID present - if all end up being 0, then authorize (this misses ability to authorize create requests!) 
    			try {
	    			if(treatmentPlanID!=0){
	    				System.out.println("check for TxPlan auth.");
	    				
					if(!user.isAuthorizedForTreatmentPlan(treatmentPlanID)){
						authorized = false;
					}
						
	    				
	    			} else if (stageID!=0){
	    				System.out.println("check for Stage auth.");
	    				if(!user.isAuthorizedForStage(stageID)){
	    					authorized = false;
	    				}
	    				
	    			} else if (taskID!=0){
	    				System.out.println("check for Task auth.");
	    				if(!user.isAuthorizedForTask(taskID)){
	    					authorized = false;
	    				}
	    			}/* else {
	    				//separate check for inserting to database since won't have any ids available at that time
	    				String reqAction = httpRequest.getParameter("requestedAction");
	    				
	    				if(reqAction!=null){
	    					switch(reqAction){
	        				case "plan-create-start":
	        				case "plan-edit-start":
	        					authorized = true;
	        					break;
	        					
	        				default:
	        					//none of the cases have been met so consider as unauthorized access attempt and forward to unauthorizied access error page
	        					System.out.println("Unauthorized Access.");
	        					httpRequest.setAttribute("errorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
	        					//httpResponse.sendRedirect(httpRequest.getContextPath() + "/unauthorized-access.jsp");
	        					//httpRequest.getRequestDispatcher("/secure/unauthorized-access.jsp").forward(request, response);
	        				}
	    				} else {
	    					//none of the ID fields are present and there is no path, so forward to unauthorized access
	    					System.out.println("Unauthorized Access.");
	    					httpRequest.setAttribute("errorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
	    					//httpResponse.sendRedirect(httpRequest.getContextPath() + "/secure/unauthorized-access.jsp");
	    				}
	    				
	    				
	    			}*/
    			
    			} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			System.out.println("authorized=" + authorized);
    			
    			if(authorized){
    				chain.doFilter(request, response); // Logged-in user found and request authorized, so just continue chain.
    			} else {
    				httpRequest.getRequestDispatcher(Constants.URL_ERROR_UNAUTHORIZED_ACCESS).forward(request, response);
    			}
    			
    		}
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
