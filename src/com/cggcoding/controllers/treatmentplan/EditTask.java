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
import com.cggcoding.models.User;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.WarningMessages;

/**
 * Servlet implementation class EditTask
 */
@WebServlet("/secure/EditTask")
public class EditTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditTask() {
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
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		/*-----------End Common Servlet variables---------------*/
		
		//TODO delete? int userID =  user.getUserID();
		int ownerUserID = 0;
		User owner = null;
		Task task = null;
		
		
		
		/*These variables helps remember where to send the user back to when they are done editing the Task.
		If the Task being edited is a template the stageID will be TEMPLATE_HOLDER_ID, and not the Stage template being working on.
		If the Task being edited is part of a client's plan, then the stageID will be the stageID that is contained within the task.
		Need to maintain it between requests*/
		request.setAttribute("stageID", stageID);
		request.setAttribute("treatmentPlanID", treatmentPlanID);

		try {
			//put user-independent attributes acquired from database in the request
			request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
			request.setAttribute("taskTemplateList", Task.getDefaultTasks());
			
			//TODO make sure to remove ownerUserID and clientUserID from edit jsps since I have switched things to not need to maintain this value - get it from treatmentPlan/stage/task			
			//Here we check that a task has been selected (currently the only time this isn't true is with path plan-edit-selection).
    		//If so, then load it and use it's userID prop to get it's owner
    		if(taskID != 0){
    			task = Task.load(taskID);
        		ownerUserID = task.getUserID();
        		
        		//Set the User var "owner". If the owner of the plan that is being edited is different than the logged in user, then load the appropriate owner info
	    		if(ownerUserID==user.getUserID()){
	    			owner = user;
	    		} else {
	    			owner = User.loadBasic(task.getUserID());
	    		}
	    		
	    		request.setAttribute("owner", owner);
	    		
	    		//if this Task is a template, remind the user that all instances of this task will be changed
	    		if(task.isTemplate()){
					request.setAttribute("warningMessage", WarningMessages.EDITING_TASK_TEMPLATE);
				}
    		}

			
    		if(user.hasRole(Constants.USER_ADMIN) || user.hasRole(Constants.USER_THERAPIST)){
				switch(requestedAction){
					case ("edit-task-start"):

						forwardTo = Constants.URL_EDIT_TASK;
						break;
					case ("edit-task-select-task"):
						
						forwardTo = Constants.URL_EDIT_TASK;
						break;
					case ("edit-task-select-task-type"):
						int newTaskTypeID = ParameterUtils.parseIntParameter(request, "taskTypeID");
						//TODO delete? task.setTaskTypeID(newTaskTypeID);
						task =Task.convertToType(task, newTaskTypeID);
	
						forwardTo = Constants.URL_EDIT_TASK;
						break;
					case ("edit-task-update"):
						if(task.getTaskID()==0){
							throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
						}
						
					
						task = CommonServletFunctions.updateTaskParametersFromRequest(request, task);
						
						task.update();
						
						switch(path){
							case Constants.PATH_TEMPLATE_TREATMENT_PLAN:
							case Constants.PATH_TEMPLATE_STAGE:
							case Constants.PATH_CLIENT_TREATMENT_PLAN:
		            		case Constants.PATH_MANAGE_CLIENT:
								request.setAttribute("stage", Stage.load(stageID));
								request.setAttribute("defaultStageList", Stage.getDefaultStages());
								
								forwardTo = Constants.URL_EDIT_STAGE;
								
								break;
								
		            		case Constants.PATH_TEMPLATE_TASK:
		            			if(user.getRole().equals(Constants.USER_ADMIN)){
			            			forwardTo = Constants.URL_ADMIN_MAIN_MENU;
			            		} else if(user.getRole().equals(Constants.USER_THERAPIST)){
			            			forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
			            		}
						}
						
						request.setAttribute("warningMessage", null);
						
						break;
				}
				
				request.setAttribute("task", task);
				
			} else if(user.hasRole(Constants.USER_CLIENT)){
				forwardTo = "clientMainMenu.jsp";
				request.setAttribute("erorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
				//UNSURE consider creating a UnauthorizedAccessException and throwing that here
				
			}
				
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("task", task);
			request.setAttribute("stageID", stageID);
			request.setAttribute("treatmentPlanID", treatmentPlanID);
			request.setAttribute("errorMessage", e.getMessage());
			
			e.printStackTrace();

			forwardTo = Constants.URL_EDIT_TASK;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	

}
