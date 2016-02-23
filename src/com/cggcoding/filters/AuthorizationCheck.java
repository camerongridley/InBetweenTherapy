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

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.User;
import com.cggcoding.utils.ParameterUtils;

/**
 * Servlet Filter implementation class AuthorizationCheck
 */
@WebFilter("/secure/*")
public class AuthorizationCheck implements Filter {

    /**
     * Default constructor. 
     */
    public AuthorizationCheck() {

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
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		int treatmentPlanID = ParameterUtils.parseIntParameter(httpRequest, "treatmentPlanID");
		int stageID = ParameterUtils.parseIntParameter(httpRequest, "stageID");
		int taskID = ParameterUtils.parseIntParameter(httpRequest, "taskID");
		
		System.out.println("Athorization Filter - txPlanID: " + treatmentPlanID + "; stageID: " + stageID + "; taskID: " + taskID);

		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
