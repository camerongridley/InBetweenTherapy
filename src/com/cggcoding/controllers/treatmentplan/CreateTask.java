package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.helpers.DefaultDatabaseCalls;
import com.cggcoding.models.Task;
import com.cggcoding.models.User;
import com.cggcoding.models.tasktypes.GenericTask;
import com.cggcoding.models.tasktypes.TwoTextBoxesTask;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		userID =  user.getUserID();
		String requestedAction = request.getParameter("requestedAction");
		String forwardTo = "/index.jsp";
		
		//performed here to get parameters for all tasks run below depending on what type of task is selected
		Task taskToCreate = CommonServletFunctions.getTaskParametersFromRequest(request, userID);

		try {
			//put user-independent (i.e. default) lists acquired from database in the request
			request.setAttribute("taskTypeMap", DefaultDatabaseCalls.getTaskTypeMap());
			
			if(user.hasRole("admin")){
				switch(requestedAction){
				case ("create-task-start"):
					taskToCreate.setTemplate(true);//TODO change this to be set based on path?
					//set tempTask in request so page knows value of isTemplate
					request.setAttribute("task", taskToCreate);
					forwardTo = "/jsp/treatment-plans/task-create.jsp";
					break;
				case "task-type-select":
					request.setAttribute("task", taskToCreate);

					forwardTo = "/jsp/treatment-plans/task-create.jsp";
					break;
				case ("task-save"):
					if(taskToCreate.isTemplate()==true){

						switch(taskToCreate.getTaskTypeID()){
							case Constants.TASK_TYPE_ID_GENERIC_TASK:
								GenericTask genericTask = (GenericTask)taskToCreate;
								genericTask.saveNew();
								break;
							case Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK:
								TwoTextBoxesTask twoTextTask = (TwoTextBoxesTask)taskToCreate;
								twoTextTask.saveNew();
								break;
						}

						forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
					}else{
						//code for non-template/clientTask creation and editing
					}
					break;
				case ("edit-task-template-start"):
					taskToCreate.setTemplate(true);
					//set tempTask in request so page knows value of isTemplate
					request.setAttribute("task", taskToCreate);
					forwardTo = "/jsp/treatment-plans/task-edit.jsp";
					break;
				}
			}
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("task", taskToCreate);
			request.setAttribute("errorMessage", e.getMessage());

			forwardTo = "/jsp/treatment-plans/task-create.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
