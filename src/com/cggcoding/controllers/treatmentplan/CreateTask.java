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
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.ParameterUtils;

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
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/

		userID =  user.getUserID();
		int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		Stage stage = null;
		
		//performed here to get parameters for all tasks run below depending on what type of task is selected
		Task taskToCreate = CommonServletFunctions.getTaskParametersFromRequest(request, userID);

		int planToReturnTo = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
		request.setAttribute("treatmentPlanID", planToReturnTo);
		
		try {
			//put user-independent (i.e. default) lists acquired from database in the request
			request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
			request.setAttribute("defaultTasks", Task.getDefaultTasks());
	
			if(user.hasRole("admin")){
				UserAdmin admin = (UserAdmin)user;
				switch(requestedAction){
				case ("create-task-start"):
					//set tempTask in request so page knows value of isTemplate
					request.setAttribute("task", taskToCreate);
					forwardTo = "/WEB-INF/jsp/treatment-plans/task-create.jsp";
					break;
				case "task-add-default-template" :

					if(taskToCreate.getTaskID() != 0){
						stage = Stage.load(stageID);
						stage.addTaskTemplate(taskToCreate.getTaskID());
						//stage.copyTaskIntoStage(taskToCreate.getTaskID());
						
						if(path.equals("treatmentPlanTemplate") || path.equals("stageTemplate")){
							request.setAttribute("stage", stage);//XXX right now this is redundant as loadStageAndPutInRequest is called later
							request.setAttribute("defaultStageList", Stage.getDefaultStages());
							forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						}else{
							forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
						}
					}
					
					break;
				case "task-type-select":
					request.setAttribute("task", taskToCreate);
					request.setAttribute("defaultTasks", Task.getDefaultTasks());
					forwardTo = "/WEB-INF/jsp/treatment-plans/task-create.jsp";
					break;
				case ("create-new-task"):
					Task newTask = null;
				
					//TODO implement this?
					/*switch(path){
						case "taskTemplate":
							Task.createTemplate(taskToCreate);
							break;
						case "stageTemplate":
							newTask = Task.createTemplate(taskToCreate);
							stage = Stage.load(stageID);
							stage.addTaskTemplate(newTask.getTaskID());
							break;
						default:
							
							
					}*/
					if(path.equals("taskTemplate")){
						Task.createTemplate(taskToCreate);
					} else {
						newTask = Task.createTemplate(taskToCreate);
						stage = Stage.load(stageID);
						stage.addTaskTemplate(newTask.getTaskID());
						/*stage = Stage.load(stageID);
						stage.createNewTask(taskToCreate);
						
						if(ParameterUtils.singleCheckboxIsOn(request, "copyAsTemplate")){
							Task templateCopy = taskToCreate.copy();
							Task.createTemplate(templateCopy);
						}*/
						
						request.setAttribute("stage", Stage.load(stageID));
					}
										
					if(path.equals("treatmentPlanTemplate") || path.equals("stageTemplate")){
						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
					}else{
						forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
					}

					break;
				}
				
				
				if(path.equals("taskTemplate")){
					
				}else{
					stage = loadStageAndPutInRequest(request, stageID);
				}

			}
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("stage", stage);
			request.setAttribute("task", taskToCreate);
			request.setAttribute("errorMessage", e.getMessage());

			forwardTo = "/WEB-INF/jsp/treatment-plans/task-create.jsp";
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
