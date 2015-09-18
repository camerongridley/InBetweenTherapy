package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.helpers.DefaultDatabaseCalls;
import com.cggcoding.models.Task;
import com.cggcoding.models.User;
import com.cggcoding.models.tasktypes.GenericTask;

/**
 * Servlet implementation class CreateTaskTemplate
 */
@WebServlet("/CreateTaskTemplate")
public class CreateTaskTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int userID =  0;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateTaskTemplate() {
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
		HttpSession session = request.getSession();
		String requestedAction = request.getParameter("requestedAction");
		String forwardTo = "/index.jsp";
		Task tempTask = getParametersFromRequest(request);
		
		//get and maintain value of creatingTemplate, which indicates if this is for creating/editing templates vs data tied to specific user
		String creatingTemplate = request.getParameter("isTemplate");
		if(creatingTemplate.equals("true")){
			tempTask.setTemplate(true);
		}

		try {
			//put user-independent attributes acquired from database in the request
			request.setAttribute("taskTypeMap", DefaultDatabaseCalls.getTaskTypeMap());
			
			if(user.hasRole("admin")){
				switch(requestedAction){
				case ("create-task-start"):
					//set tempTask in request so page knows value of isTemplate
					request.setAttribute("task", tempTask);
					forwardTo = "/jsp/treatment-plans/task-create-template.jsp";
					break;
				case ("taskInfo"):
					if(creatingTemplate.equals("true")){
						tempTask = getParametersFromRequest(request);
						GenericTask.saveGenericTemplateInDatabase(user.getUserID(), tempTask.getTaskTypeID(), tempTask.getTitle(), tempTask.getInstructions(), tempTask.getResourceLink(), tempTask.isExtraTask());//TODO confirm this is the best set of parameters for this factory method
						forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
					}else{
						//code for non-template/clientTask creation and editing
					}
					
					break;
				}
			}
			
		} catch (DatabaseException | ValidationException e) {
			//put in temporary task object so values can be saved in inputs after error
			request.setAttribute("task", tempTask);
			//request.setAttribute("hasSubtasks", hasSubtasks);
			request.setAttribute("errorMessage", e.getMessage());

			forwardTo = "/jsp/treatment-plans/task-create-template.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	/**
	 * Gets all the parameter values from request, except user info which is set in doPost()
	 */
	private Task getParametersFromRequest(HttpServletRequest request){
		//get all parameters associated with Task and do null checks
				int taskID = getIntParameter(request, "taskID");
				int taskTypeID =  getIntParameter(request, "taskTypeID");
				int stageID = getIntParameter(request, "stageID");
				int parentTaskID =  getIntParameter(request, "parentTaskID");//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
				String title = request.getParameter("taskTitle");
				String instructions = request.getParameter("taskInstructions");
				String resourceLink = request.getParameter("resourceLink");
				//boolean completed = request.getParameter("requestedAction");
				//LocalDate dateCompleted = request.getParameter("requestedAction"); - set in the service layer when task is marked complete
				int taskOrder =  getIntParameter(request, "taskOrder");
				boolean extraTask = ((request.getParameter("isExtraTask")!=null) ? request.getParameter("isExtraTask").equalsIgnoreCase("true") : false); 
				boolean template = ((request.getParameter("isTemplate")!=null) ? request.getParameter("isTemplate").equalsIgnoreCase("true") : false); 
				
				String hasSubtasks = request.getParameter("hasSubtasks");
						
				Task task = GenericTask.getInstance(userID, taskTypeID, parentTaskID, title, instructions, resourceLink, extraTask, template);
				task.setTaskID(taskID);
				task.setStageID(stageID);
				task.setTaskOrder(taskOrder);
								
				return task;
	}
	
	private int getIntParameter(HttpServletRequest request, String intParameterName){
		if(request.getParameter(intParameterName)==null || request.getParameter(intParameterName).isEmpty()){
			return 0;
		} else{
			return Integer.parseInt(request.getParameter(intParameterName));
		}
	}

}
