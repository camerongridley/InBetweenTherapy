package com.cggcoding.utils;

import javax.servlet.http.HttpServletRequest;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.models.TaskTwoTextBoxes;

public class CommonServletFunctions {

	public CommonServletFunctions() {

	}

	public static void createCoreTreatmentIssue(HttpServletRequest request, int userID) throws ValidationException, DatabaseException{
		String newIssueName = request.getParameter("newCoreTreatmentIssue");
		TreatmentIssue issue = new TreatmentIssue(newIssueName, userID);
		issue.create();
		
		request.setAttribute("coreTreatmentIssues", TreatmentIssue.getCoreTreatmentIssues());
	}
	
	//TODO delete this after fully transitioned to updateTaskParametersFromRequest
	/**Gets all the parameter values from request, except user info which is in the session, and creates a Task object of the correct taskType with them
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
		int clientTaskOrder =  ParameterUtils.parseIntParameter(request, "clientTaskOrder");
		boolean extraTask = ParameterUtils.getBooleanParameter(request, "isExtraTask"); 
		boolean template = ParameterUtils.getBooleanParameter(request, "isTemplate"); 
		int templateID = ParameterUtils.parseIntParameter(request, "templateID");
		int clientRepetition = ParameterUtils.parseIntParameterDefaultIsOne(request, "clientRepetition");
		
		TaskGeneric genericTask = TaskGeneric.getInstanceWithoutTaskID(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, clientTaskOrder, extraTask, template, templateID, clientRepetition);
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
	
	public static Task updateTaskParametersFromRequest(HttpServletRequest request, Task task){
		//update appropriate parameters associated with Task and do null checks
		task.setTaskTypeID(ParameterUtils.parseIntParameter(request, "taskTypeID"));
		String title = request.getParameter("taskTitle");
		task.setTitle(request.getParameter("taskTitle"));
		task.setInstructions(request.getParameter("taskInstructions"));
		task.setResourceLink(request.getParameter("resourceLink"));
		task.setClientTaskOrder(ParameterUtils.parseIntParameter(request, "clientTaskOrder"));
		task.setExtraTask(ParameterUtils.getBooleanParameter(request, "isExtraTask"));
		task.setClientRepetition(ParameterUtils.parseIntParameterDefaultIsOne(request, "clientRepetition"));
		
		//The following are other properties of Task that shouldn't really ever be altered in the jsp
		//task.setTemplate(ParameterUtils.getBooleanParameter(request, "isTemplate")); 
		//int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		//int parentTaskID =  ParameterUtils.parseIntParameter(request, "parentTaskID");//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
		//boolean completed = request.getParameter("requestedAction");
		//LocalDateTime dateCompleted = request.getParameter("requestedAction"); - set in the service layer when task is marked complete
		//int templateID = ParameterUtils.parseIntParameter(request, "templateID");
		
		
		
		//XXX replace use of downcasting?  re: http://stackoverflow.com/questions/2856122/how-to-find-out-the-subclass-from-the-base-class-instance
		//If is instanceof TaskGeneric then nothing else needs updating
		if(task instanceof TaskTwoTextBoxes){
			((TaskTwoTextBoxes)task).setExtraTextLabel1(request.getParameter("extraTextLabel1"));
			((TaskTwoTextBoxes)task).setExtraTextValue1(request.getParameter("extraTextValue1"));
			((TaskTwoTextBoxes)task).setExtraTextLabel2(request.getParameter("extraTextLabel2"));
			((TaskTwoTextBoxes)task).setExtraTextValue2(request.getParameter("extraTextValue2"));

		}
		
		return task;

	}
	
	
	public static void setCoreTreatmentIssuesInRequest(HttpServletRequest request) throws DatabaseException, ValidationException{
		request.setAttribute("coreTreatmentIssues", TreatmentIssue.getCoreTreatmentIssues());
	}
	
	public static void setCoreTreatmentPlansInRequest(HttpServletRequest request) throws DatabaseException, ValidationException{
		request.setAttribute("coreTreatmentPlansList", TreatmentPlan.getCoreTreatmentPlans());
	}
	
}
