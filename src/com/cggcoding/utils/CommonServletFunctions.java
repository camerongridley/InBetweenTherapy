package com.cggcoding.utils;

import javax.servlet.http.HttpServletRequest;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.helpers.DefaultDatabaseCalls;
import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TaskTwoTextBoxes;

public class CommonServletFunctions {

	public CommonServletFunctions() {

	}

	public static void createDefaultTreatmentIssue(HttpServletRequest request, int userID) throws ValidationException, DatabaseException{
		String newIssueName = request.getParameter("newDefaultTreatmentIssue");
		TreatmentIssue issue = new TreatmentIssue(newIssueName, userID);
		issue.create();
		
		request.setAttribute("defaultTreatmentIssues", DefaultDatabaseCalls.getDefaultTreatmentIssues());
	}
	
	/**Gets all the parameter values from request, except user info which isn't in the request and is set in doPost(), and creates a Task object of the correct taskType with them
	 * @param request  HttpServletRequest
	 * @param userID id for the current user
	 * @return Task object populated with parameter values obtained from request.
	 */
	public static Task getTaskParametersFromRequest(HttpServletRequest request, int userID){
		Task task = null;
		//get all parameters associated with Task and do null checks
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		int taskTypeID =  ParameterUtils.parseIntParameter(request, "taskTypeID");
		int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int parentTaskID =  ParameterUtils.parseIntParameter(request, "parentTaskID");//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
		String title = request.getParameter("taskTitle");
		String instructions = request.getParameter("taskInstructions");
		String resourceLink = request.getParameter("resourceLink");
		//boolean completed = request.getParameter("requestedAction");
		//LocalDateTime dateCompleted = request.getParameter("requestedAction"); - set in the service layer when task is marked complete
		int taskOrder =  ParameterUtils.parseIntParameter(request, "taskOrder");
		boolean extraTask = ParameterUtils.getBooleanParameter(request, "isExtraTask"); 
		boolean template = ParameterUtils.getBooleanParameter(request, "isTemplate"); 
		int templateID = ParameterUtils.parseIntParameter(request, "templateID");
		int repetitions = ParameterUtils.parseIntParameterDefaultIsOne(request, "repetitions");
		
		TaskGeneric genericTask = TaskGeneric.getInstanceWithoutTaskID(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template, templateID, repetitions);
		genericTask.setTaskID(taskID);
		
		switch(taskTypeID){
			case Constants.TASK_TYPE_ID_GENERIC_TASK:
				task = genericTask;
				break;
			case Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK:
				String extraTextLabel1 = request.getParameter("extraTextLabel1");
				String extraTextValue1 = request.getParameter("extraTextValue1");
				String extraTextLabel2 = request.getParameter("extraTextLabel2");
				String extraTextValue2 = request.getParameter("extraTextValue2");
				
				TaskTwoTextBoxes twoTextTask = TaskTwoTextBoxes.addDataToGenericTask(genericTask, extraTextLabel1, extraTextValue1, extraTextLabel2, extraTextValue2);
				
				task = twoTextTask;
				break;
			default:
				task = genericTask;
				break;
		}

		return task;
	}
	
	public static void setDefaultTreatmentIssuesInRequest(HttpServletRequest request) throws DatabaseException, ValidationException{
		request.setAttribute("defaultTreatmentIssues", DefaultDatabaseCalls.getDefaultTreatmentIssues());
	}
	
	public static void setDefaultTreatmentPlansInRequest(HttpServletRequest request) throws DatabaseException, ValidationException{
		request.setAttribute("defaultTreatmentPlanList", DefaultDatabaseCalls.getDefaultTreatmentPlans());
	}
}
