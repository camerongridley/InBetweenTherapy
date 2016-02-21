package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.MapStageTaskTemplate;
import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class CreateTask
 */
@WebServlet("/secure/CreateTask")
public class CreateTask extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTask() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		/*-----------Common Treatment Plan object variables------------*/
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		TreatmentPlan treatmentPlan = null;
		Stage stage = null;
		Task task = null;
		int ownerUserID = 0;
		User owner = null;
		/*-----------End Treatment Plan object variables---------------*/
		
		int taskReps = ParameterUtils.parseIntParameter(request, "taskReps");
		
		//performed here to get parameters for all tasks run below depending on what type of task is selected
		task = CommonServletFunctions.getTaskParametersFromRequest(request, user.getUserID());//TODO change this to use updateTaskParametersFromRequest

		try {
			if(!path.equals(Constants.PATH_TEMPLATE_TASK)){
				stage = Stage.load(stageID);//load the entire stage since we need everything loaded to determine certain properties, such as the taskOrder
				treatmentPlan = TreatmentPlan.loadBasic(treatmentPlanID);//only need basic info such as title so use loadBasic()
				ownerUserID = stage.getUserID();
			}

			//Set the User var "owner". If the owner of the plan that is being edited is different than the logged in user, then load the appropriate owner info
    		if(ownerUserID==user.getUserID() || ownerUserID==0){
    			owner = user;
    		} else {
    			owner = User.loadBasic(ownerUserID);
    		}
			
	
			if(user.hasRole(Constants.USER_ADMIN) || user.hasRole(Constants.USER_THERAPIST)){

				switch(requestedAction){
					case ("create-task-start"):
						//set tempTask in request so page knows value of isTemplate
						request.setAttribute("task", task);
						forwardTo = Constants.URL_CREATE_TASK;
						break;
					case "task-add-template" :
	
						forwardTo = Constants.URL_EDIT_STAGE;
						//only do the following if the save button was clicked.
						if(request.getParameter("submitButton").equals("save")){
							if(task.getTaskID() != 0){				

								if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN) || path.equals(Constants.PATH_TEMPLATE_STAGE)){
									stage.addTaskTemplate(task.getTaskID(), taskReps);
									request.setAttribute("coreStagesList", Stage.getCoreStages());
									
								} else if (path.equals(Constants.PATH_MANAGE_CLIENT)){
									int clientRepetition = ParameterUtils.parseIntParameter(request, "clientRepetitions");
									MapStageTaskTemplate stageTaskInfo = new MapStageTaskTemplate(stage.getStageID(), task.getTaskID(), 0, clientRepetition);
									stage.createTaskFromTemplate(task.getTaskID(), stageTaskInfo);
								}
								request.setAttribute("successMessage", SuccessMessages.TASK_ADDED_TO_STAGE);
							}
							
						}else{
							//Cancel button pressed.  Just send back to appropriate page
							forwardTo = setForwardToForCancel(request, user, path);
						}
						
						break;
					case "task-type-select":
						request.setAttribute("task", task);
						request.setAttribute("coreTasks", Task.getCoreTasks());
	
						request.setAttribute("scrollTo", "taskTypeSelection");
						
						forwardTo = Constants.URL_CREATE_TASK;
						break;
					case ("task-create-new"):
						Task newTask = null;

						if(request.getParameter("submitButton").equals("save")){
							switch(path){
								case Constants.PATH_TEMPLATE_TASK:
									Task.createTemplate(task);
									forwardTo = Constants.URL_ADMIN_MAIN_MENU;
									break;
								case Constants.PATH_TEMPLATE_TREATMENT_PLAN:
								case Constants.PATH_TEMPLATE_STAGE:
									newTask = Task.createTemplate(task);
									stage.addTaskTemplate(newTask.getTaskID(), taskReps);
									request.setAttribute("coreStagesList", Stage.getCoreStages());
									
									forwardTo = Constants.URL_EDIT_STAGE;
									break;
								case Constants.PATH_MANAGE_CLIENT:
									//TODO implement creating a new Task here!  newTask = Stage.createClientTask(taskTitle, taskInstructions); 
									
									forwardTo = Constants.URL_EDIT_STAGE;
									break;
							}
						}else{
							//Cancel button pressed.  Just send back to appropriate page
							forwardTo = setForwardToForCancel(request, user, path);
						}

					break;
				}
				
				
				/*//TODO delete after confirm removal didn't break things
				 * if(path.equals(Constants.PATH_TEMPLATE_TASK)){
					
				}else{
					stage = loadStageAndPutInRequest(request, stageID);//OPTIMIZE delete this and just make sure all previous methods return the Stage object with the proper modifications
				}*/
				
				//put user-independent (i.e. default) lists acquired from database in the request
				request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
				request.setAttribute("coreTasks", Task.getCoreTasks());
				request.setAttribute("treatmentPlan", treatmentPlan);
				request.setAttribute("stage", stage);
				request.setAttribute("owner", owner);

			}
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("stage", stage);
			request.setAttribute("task", task);
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("owner", owner);

			forwardTo = Constants.URL_CREATE_TASK;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	private String setForwardToForCancel(HttpServletRequest request, User user, String path) throws DatabaseException, ValidationException{
		String forwardTo = "";
		switch(path){
			case Constants.PATH_TEMPLATE_TASK:
				forwardTo = user.getMainMenuURL();
				break;
			case Constants.PATH_TEMPLATE_TREATMENT_PLAN:
			case Constants.PATH_TEMPLATE_STAGE:
				request.setAttribute("coreStagesList", Stage.getCoreStages());
				forwardTo = Constants.URL_EDIT_STAGE;
				break;
			case Constants.PATH_MANAGE_CLIENT:
				forwardTo = Constants.URL_EDIT_STAGE;
				break;
		}
		
		return forwardTo;
	}
	
	private Stage loadStageAndPutInRequest(HttpServletRequest request, int stageID) throws DatabaseException, ValidationException{
		Stage stage = null;
		if(stageID != 0){
			stage = Stage.load(stageID);
			request.setAttribute("stage", stage);
		}
		
		return stage;
	}

}
