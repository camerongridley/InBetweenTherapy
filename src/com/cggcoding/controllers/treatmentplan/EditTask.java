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
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Servlet implementation class EditTask
 */
@WebServlet("/EditTask")
public class EditTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int userID = 0;
       
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
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		userID =  user.getUserID();
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		String forwardTo = "/index.jsp";
		
		//performed here to get parameters for all tasks run below
		Task tempTask = CommonServletFunctions.getTaskParametersFromRequest(request, userID);
		
		//get and maintain value of creatingTemplate, which indicates if this is for creating/editing templates vs data tied to specific user
		/*String creatingTemplate = request.getParameter("isTemplate");
		if(creatingTemplate.equals("true")){
			tempTask.setTemplate(true);
		}*/

		try {
			//put user-independent attributes acquired from database in the request
			request.setAttribute("taskTypeMap", DefaultDatabaseCalls.getTaskTypeMap());
			request.setAttribute("taskTemplateList", DefaultDatabaseCalls.getDefaultTasks());
			
			if(user.hasRole("admin")){
				switch(requestedAction){
				case ("edit-task-start"):
					tempTask.setTemplate(true);
					//set tempTask in request so page knows value of isTemplate
					request.setAttribute("task", tempTask);
					forwardTo = "/jsp/treatment-plans/task-edit.jsp";
					break;
				case ("edit-task-select-task"):
					int selectedTaskID = ParameterUtils.parseIntParameter(request, "defaultTaskListID");
					if(selectedTaskID != 0){
						request.setAttribute("task", Task.load(selectedTaskID));
					}

					forwardTo = "/jsp/treatment-plans/task-edit.jsp";
					break;
				case ("edit-task-select-task-type"):
					/*selectedTaskID = ParameterUtils.parseIntParameter(request, "taskID");
					int newTaskTypeID = ParameterUtils.parseIntParameter(request, "taskTypeID");
					Task taskWithNewType = Task.load(selectedTaskID);
					taskWithNewType.setTaskTypeID(newTaskTypeID);
					if(selectedTaskID != 0){
						switch(newTaskTypeID){
							case (Constants.TASK_TYPE_ID_GENERIC_TASK):
								GenericTask genericTask = (GenericTask)taskWithNewType;
								request.setAttribute("task", genericTask);
								break;
							case(Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK):
								TwoTextBoxesTask twoTextBoxesTask = (TwoTextBoxesTask)taskWithNewType;
								request.setAttribute("task", twoTextBoxesTask);
								break;
						}
						
					}*/
					
					forwardTo = "/jsp/treatment-plans/task-edit.jsp";
					break;
				case ("edit-task-update"):
					switch(tempTask.getTaskTypeID()){
						case (Constants.TASK_TYPE_ID_GENERIC_TASK):
							GenericTask genericTask = (GenericTask)tempTask;
							genericTask.update();
							break;
						case(Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK):
							TwoTextBoxesTask twoTextBoxesTask = (TwoTextBoxesTask)tempTask;
							twoTextBoxesTask.update();
							break;
					}
					
					break;
				}
			}
				
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("task", tempTask);
			request.setAttribute("errorMessage", e.getMessage());

			forwardTo = "/jsp/treatment-plans/task-edit.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
