package com.cggcoding.utils;

import javax.servlet.http.HttpServletRequest;

import com.cggcoding.models.Task;
import com.cggcoding.models.tasktypes.GenericTask;

public class ParameterUtils {

	public ParameterUtils() {

	}
	/** Checks specified int parameter in request. If it is null or empty returns 0, otherwise parses the paran string and return the int value
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
	
	public static boolean getBooleanParameter(HttpServletRequest request, String boolParameterName){
		//return ((request.getParameter(boolParameterName)!=null) ? request.getParameter(boolParameterName).equalsIgnoreCase("true") : false); 
		if(request.getParameter(boolParameterName) != null){
			return request.getParameter(boolParameterName).equals("true");
		} else {
			return false;
		}
	}
	

	/**Gets all the parameter values from request, except user info which isn't in the request and is set in doPost(), and creates a Task object with them
	 * @param request  HttpServletRequest
	 * @param userID id for the current user
	 * @return Task object populated with parameter values obtained from request.
	 */
	public static Task getTaskParametersFromRequest(HttpServletRequest request, int userID){
		//get all parameters associated with Task and do null checks
				int taskID = parseIntParameter(request, "taskID");
				int taskTypeID =  parseIntParameter(request, "taskTypeID");
				int stageID = parseIntParameter(request, "stageID");
				int parentTaskID =  parseIntParameter(request, "parentTaskID");//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
				String title = request.getParameter("taskTitle");
				String instructions = request.getParameter("taskInstructions");
				String resourceLink = request.getParameter("resourceLink");
				//boolean completed = request.getParameter("requestedAction");
				//LocalDateTime dateCompleted = request.getParameter("requestedAction"); - set in the service layer when task is marked complete
				int taskOrder =  parseIntParameter(request, "taskOrder");
				boolean extraTask = getBooleanParameter(request, "isExtraTask"); 
				boolean template = getBooleanParameter(request, "isTemplate"); 
				
				//String hasSubtasks = request.getParameter("hasSubtasks");
						
				Task task = GenericTask.getInstanceWithoutTaskID(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template);
				task.setTaskID(taskID);
				task.setStageID(stageID);
				task.setTaskOrder(taskOrder);
								
				return task;
	}
	
}
