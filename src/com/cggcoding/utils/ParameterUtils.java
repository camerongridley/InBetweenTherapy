package com.cggcoding.utils;

import javax.servlet.http.HttpServletRequest;

import com.cggcoding.models.Task;
import com.cggcoding.models.tasktypes.GenericTask;

public class ParameterUtils {

	public ParameterUtils() {

	}
	/** Checks specified int parameter in request. If it is null or empty returns 0, otherwise parses the parameter string and return the int value
	 * @param request HttpServletRequest
	 * @param intParameterName the parameter name for a variable of type int
	 * @return int value of parameter or 0 if null or empty
	 */
	public static int parseIntParameter(HttpServletRequest request, String intParameterName){
		if(request.getParameter(intParameterName)==null || request.getParameter(intParameterName).isEmpty()){
			return 0;
		} else{
			return Integer.parseInt(request.getParameter(intParameterName));
		}
	}
	
	/**Gets and converts boolean parameter from the request object provided.
	 * @param request HttpServletRequest
	 * @param boolParameterName Name of the boolean parameter as a string.
	 * @return boolean value of the parameter
	 */
	public static boolean getBooleanParameter(HttpServletRequest request, String boolParameterName){
		//return ((request.getParameter(boolParameterName)!=null) ? request.getParameter(boolParameterName).equalsIgnoreCase("true") : false); 
		if(request.getParameter(boolParameterName) != null){
			return request.getParameter(boolParameterName).equals("true");
		} else {
			return false;
		}
	}
	
	
}
