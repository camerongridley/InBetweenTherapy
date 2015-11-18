package com.cggcoding.utils;

import javax.servlet.http.HttpServletRequest;

import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.Task;

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
	
	/** Checks specified int parameter in request. If it is null or empty returns 1, otherwise parses the parameter string and return the int value
	 * @param request HttpServletRequest
	 * @param intParameterName the parameter name for a variable of type int
	 * @return int value of parameter or 0 if null or empty
	 */
	public static int parseIntParameterDefaultIsOne(HttpServletRequest request, String intParameterName){
		if(request.getParameter(intParameterName)==null || request.getParameter(intParameterName).isEmpty()){
			return 1;
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
	
	/**Checks the parameter value of the checkbox name supplied for the value of "checked".
	 * @param request
	 * @param chkbxParameterName
	 * @return TRUE if checkbox value equals "checked", false if null or equal something else.
	 */
	public static boolean singleCheckboxIsOn(HttpServletRequest request, String chkbxParameterName){
		String[] checkbox = request.getParameterValues(chkbxParameterName);
		if(checkbox == null){
			return false;
		}
		if(checkbox[0].equals("checked")){
			return true;
		}
		
		return false;
	}
	
}
