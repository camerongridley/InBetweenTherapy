package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
import java.util.List;

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
import com.cggcoding.models.Keyword;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;
import com.cggcoding.utils.messaging.WarningMessages;

/**
 * Servlet implementation class EditTask
 */
@WebServlet("/secure/treatment-components/EditTask")
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
		
		int newTaskTypeID = 0;
		boolean updateDataBase = false;
		List<Integer> updatedKeywordIDs = ParameterUtils.parseIntArrayParameter(request, "keywords[]");
		
		try {
			
			
			//Here we check that a task has been selected (currently the only time this isn't true is with path plan-edit-start).
    		//If so, then load it and use it's userID prop to get it's owner
    		if(taskID != 0){
    			task = Task.load(taskID);
    			stage = Stage.load(stageID);//load the entire stage since we need everything loaded to determine certain properties, such as the taskOrder
    			treatmentPlan = TreatmentPlan.loadBasic(treatmentPlanID);//only need basic info such as title so use loadBasic()
        		ownerUserID = task.getUserID();
        		
        		//Set the User var "owner". If the owner of the plan that is being edited is different than the logged in user, then load the appropriate owner info
	    		if(ownerUserID==user.getUserID()){
	    			owner = user;
	    		} else {
	    			owner = User.loadBasic(task.getUserID());
	    		}
	    		
	    		request.setAttribute("owner", owner);
	    		request.setAttribute("coreTaskKeyords", Keyword.loadCoreList());
	    		
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
						if(path.equals(Constants.PATH_TEMPLATE_TASK)){
							request.setAttribute("scrollTo", "taskSelection");
						}
						forwardTo = Constants.URL_EDIT_TASK;
						break;
					case ("edit-task-select-task-type"):
						if(task==null || task.getTaskID()==0){
							throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
						}
					
						newTaskTypeID = ParameterUtils.parseIntParameter(request, "taskTypeID");
						//TODO delete? task.setTaskTypeID(newTaskTypeID);
					
						//do not update database here.  that should only happen once user has submitted the overall update request
						updateDataBase = false;
						task =Task.convertToType(task, newTaskTypeID, updateDataBase);
						
	
						forwardTo = Constants.URL_EDIT_TASK;
						break;
					case ("edit-task-update"):
						//if Save button pressed, run the following.  If Cancel button was pressed then skip and just forward to appropriate page
						if(request.getParameter("submitButton").equals("save")){
							if(task==null || task.getTaskID()==0){
								throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
							}
							
							newTaskTypeID = ParameterUtils.parseIntParameter(request, "taskTypeID");
							updateDataBase = true;
							task =Task.convertToType(task, newTaskTypeID, updateDataBase);
						
							task = CommonServletFunctions.updateTaskParametersFromRequest(request, task);
							
							task.setUpdatedKeywordIDsList(updatedKeywordIDs);
							
							task.update();
							
							String[] kwords = request.getParameterValues("testingData[]");
							for(int c=0; c<kwords.length; c++){
								System.out.println("Jquery Test - keyword: " + kwords[c]);
							}
							//System.out.println("Testining jqery:" + request.getParameterValues("testingData"));
							
							request.setAttribute("successMessage", SuccessMessages.TASK_UPDATED);
						}
							
						switch(path){
							case Constants.PATH_TEMPLATE_TREATMENT_PLAN:
							case Constants.PATH_TEMPLATE_STAGE:
							case Constants.PATH_CLIENT_TREATMENT_PLAN:
		            		case Constants.PATH_MANAGE_CLIENT:
								stage = Stage.load(stageID);
								request.setAttribute("coreStagesList", Stage.getCoreStages());
								task = null;
								forwardTo = Constants.URL_EDIT_STAGE;
								
								break;
								
		            		case Constants.PATH_TEMPLATE_TASK:
		            			if(user.getRole().equals(Constants.USER_ADMIN)){
			            			forwardTo = Constants.URL_ADMIN_MAIN_MENU;
			            		} else if(user.getRole().equals(Constants.USER_THERAPIST)){
			            			forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
			            		}
		            			break;
						}

						request.setAttribute("warningMessage", null);
						
						break;
				}
				
				//put user-independent attributes acquired from database in the request
				request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
				request.setAttribute("coreTasks", Task.getCoreTasks());
				request.setAttribute("treatmentPlan", treatmentPlan);
				request.setAttribute("stage", stage);
				request.setAttribute("task", task);
				request.setAttribute("owner", owner);
				
			} else if(user.hasRole(Constants.USER_CLIENT)){
				forwardTo = "clientMainMenu.jsp";
				request.setAttribute("erorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
				//UNSURE consider creating a UnauthorizedAccessException and throwing that here
				
			}
				
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("task", task);
			request.setAttribute("stage", stage);
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("owner", owner);
			
			try {
				request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
				request.setAttribute("coreTasks", Task.getCoreTasks());
			} catch (DatabaseException e1) {
				request.setAttribute("erorMessage", ErrorMessages.GENERAL_DB_ERROR);
				e1.printStackTrace();
			}
			
			
			e.printStackTrace();

			forwardTo = Constants.URL_EDIT_TASK;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	

}
