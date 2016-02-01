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
import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
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
	int userID =  0;

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

		userID =  user.getUserID();
		int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		Stage stage = null;
		int taskReps = ParameterUtils.parseIntParameter(request, "taskReps");
		
		//performed here to get parameters for all tasks run below depending on what type of task is selected
		Task taskToCreate = CommonServletFunctions.getTaskParametersFromRequest(request, userID);//TODO change this to use updateTaskParametersFromRequest

		int planToReturnTo = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
		request.setAttribute("treatmentPlanID", planToReturnTo);
		
		try {
			//put user-independent (i.e. default) lists acquired from database in the request
			request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
			request.setAttribute("defaultTasks", Task.getDefaultTasks());
			
			if(!path.equals(Constants.PATH_TEMPLATE_TASK)){
				stage = Stage.load(stageID);
			}
	
			if(user.hasRole(Constants.USER_ADMIN) || user.hasRole(Constants.USER_THERAPIST)){

				switch(requestedAction){
				case ("create-task-start"):
					//set tempTask in request so page knows value of isTemplate
					request.setAttribute("task", taskToCreate);
					forwardTo = Constants.URL_CREATE_TASK;
					break;
				case "task-add-default-template" :

					if(taskToCreate.getTaskID() != 0){				
						
						forwardTo = Constants.URL_EDIT_STAGE;
						if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN) || path.equals(Constants.PATH_TEMPLATE_STAGE)){
							stage.addTaskTemplate(taskToCreate.getTaskID(), taskReps);
							request.setAttribute("defaultStageList", Stage.getDefaultStages());
							
						} else if (path.equals(Constants.PATH_MANAGE_CLIENT)){
							int clientRepetition = ParameterUtils.parseIntParameter(request, "clientRepetitions");
							
							stage.copyTaskIntoClientStage(taskToCreate.getTaskID());
						}
						request.setAttribute("successMessage", SuccessMessages.TASK_ADDED_TO_STAGE);
					}
					
					break;
				case "task-type-select":
					request.setAttribute("task", taskToCreate);
					request.setAttribute("defaultTasks", Task.getDefaultTasks());
					forwardTo = Constants.URL_CREATE_TASK;
					break;
				case ("create-new-task"):
					Task newTask = null;
				
					//TODO implement this?
					/*switch(path){
						case Constants.PATH_TEMPLATE_TASK:
							Task.createTemplate(taskToCreate);
							break;
						case Constants.PATH_TEMPLATE_STAGE:
							newTask = Task.createTemplate(taskToCreate);
							stage = Stage.load(stageID);
							stage.addTaskTemplate(newTask.getTaskID());
							break;
						default:
							
							
					}*/
					if(path.equals(Constants.PATH_TEMPLATE_TASK)){
						Task.createTemplate(taskToCreate);
					} else {
						newTask = Task.createTemplate(taskToCreate);
						//TODO delete? stage = Stage.load(stageID);
						stage.addTaskTemplate(newTask.getTaskID(), taskReps);//TODO make sure this is working right
						/*stage = Stage.load(stageID);
						stage.createNewTask(taskToCreate);
						
						if(ParameterUtils.singleCheckboxIsOn(request, "copyAsTemplate")){
							Task templateCopy = taskToCreate.copy();
							Task.createTemplate(templateCopy);
						}*/
						
						//request.setAttribute("stage", Stage.load(stageID));
					}
										
					if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN) || path.equals(Constants.PATH_TEMPLATE_STAGE)){
						forwardTo = Constants.URL_EDIT_STAGE;
					}else{
						forwardTo = Constants.URL_ADMIN_MAIN_MENU;
					}

					break;
				}
				
				
				/*//TODO delete after confirm removal didn't break things
				 * if(path.equals(Constants.PATH_TEMPLATE_TASK)){
					
				}else{
					stage = loadStageAndPutInRequest(request, stageID);//OPTIMIZE delete this and just make sure all previous methods return the Stage object with the proper modifications
				}*/
				
				request.setAttribute("stage", stage);

			}
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("stage", stage);
			request.setAttribute("task", taskToCreate);
			request.setAttribute("treatmentPlanID", planToReturnTo);
			request.setAttribute("errorMessage", e.getMessage());

			forwardTo = Constants.URL_CREATE_TASK;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
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
