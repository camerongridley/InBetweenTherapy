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
@WebServlet("/CreateTask")
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
		/*--Common Servlet variables that should be in every controller--*/
		/*HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);*/
		/*-----------End Common Servlet variables---------------*/
		
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
					forwardTo = "/jsp/treatment-plans/task-create.jsp";
					break;
				case "task-add-default" :

					if(taskToCreate.getTaskID() != 0){
						stage = Stage.load(stageID);
						stage.copyTaskIntoStage(taskToCreate.getTaskID());
						
						if(path.equals("editingPlanTemplate") || path.equals("creatingPlanTemplate") || path.equals("creatingStageTemplate")|| path.equals("editingStageTemplate")){
							request.setAttribute("stage", stage);
							forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
						}else{
							forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
						}
					}
					
					break;
				case "task-type-select":
					request.setAttribute("task", taskToCreate);
					request.setAttribute("defaultTasks", Task.getDefaultTasks());
					forwardTo = "/jsp/treatment-plans/task-create.jsp";
					break;
				case ("task-save"):
					if(path.equals("creatingTaskTemplate")){
						Task.createTemplate(taskToCreate);
					} else{
						stage = Stage.load(stageID);
						stage.createNewTask(taskToCreate);
						
						if(ParameterUtils.singleCheckboxIsOn(request, "copyAsTemplate")){
							Task templateCopy = taskToCreate.copy();
							Task.createTemplate(templateCopy);
						}
						
						request.setAttribute("stage", Stage.load(stageID));
					}
										
					if(path.equals("editingPlanTemplate") || path.equals("creatingPlanTemplate") || path.equals("creatingStageTemplate")|| path.equals("editingStageTemplate")){
						forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
					}else{
						forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
					}

					break;
				}
				
				
				if(path.equals("creatingTaskTemplate")){
					
				}else{
					stage = getStageAndPutInRequest(request, stageID);
				}

			}
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("stage", stage);
			request.setAttribute("task", taskToCreate);
			request.setAttribute("errorMessage", e.getMessage());

			forwardTo = "/jsp/treatment-plans/task-create.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	private Stage getStageAndPutInRequest(HttpServletRequest request, int stageID) throws DatabaseException, ValidationException{
		Stage stage = null;
		if(stageID != 0){
			stage = Stage.load(stageID);
			request.setAttribute("stage", stage);
		}
		
		return stage;
	}

}
