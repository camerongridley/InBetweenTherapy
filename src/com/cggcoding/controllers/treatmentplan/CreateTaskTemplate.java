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
    //all parameters used in servlet
	int taskID = 0;
	int stageID = 0;
	int userID =  0;
	int taskTypeID =  0;
	int parentTaskID =  0;//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
	String title = "";
	String instructions = "";
	String resourceLink = "";
	//boolean completed = "";
	//LocalDate dateCompleted = ""; - set in the service layer when task is marked complete
	int taskOrder =  0;
	boolean extraTask = false;
	
	
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
		
		//get and maintain value of isTemplate, which indicates if this is for creating/editing templates vs data tied to specific user
		String isTemplate = request.getParameter("isTemplate");
		request.setAttribute("isTemplate", isTemplate);
		
		/*TODO Delete if revised approach works out
		//get all parameters associated with Task and do null checks
		int taskID = ((request.getParameter("taskID")!=null) ? Integer.parseInt(request.getParameter("taskID")) : 0);
		int stageID = ((request.getParameter("stageID")!=null) ? Integer.parseInt(request.getParameter("stageID")) : 0);
		int userID =  user.getUserID();
		int taskTypeID =  ((request.getParameter("taskTypeID")!=null) ? Integer.parseInt(request.getParameter("taskTypeID")) : 0);
		int parentTaskID =  ((request.getParameter("parentTaskID")!=null) ? Integer.parseInt(request.getParameter("parentTaskID")) : 0);//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
		String title = request.getParameter("taskTitle");
		String instructions = request.getParameter("taskInstructions");
		String resourceLink = request.getParameter("resourceLink");
		//boolean completed = request.getParameter("requestedAction");
		//LocalDate dateCompleted = request.getParameter("requestedAction"); - set in the service layer when task is marked complete
		int taskOrder =  ((request.getParameter("taskOrder")!=null) ? Integer.parseInt(request.getParameter("taskOrder")) : 0);
		boolean extraTask = ((request.getParameter("isExtraTask")!=null) ? request.getParameter("isExtraTask").equalsIgnoreCase("true") : false);*/
		
		
		
		
		try {
			//put user-independent attributes acquired from database in the request
			request.setAttribute("taskTypeMap", DefaultDatabaseCalls.getTaskTypeMap());
			
			if(user.hasRole("admin")){
				switch(requestedAction){
				case ("create-task-start"):
					forwardTo = "/jsp/treatment-plans/task-create-template.jsp";
					break;
				case ("taskInfo"):
					if(isTemplate.equals("true")){
						getParametersFromRequest(request);
						GenericTask.saveGenericTemplateInDatabase(user.getUserID(), taskTypeID, title, instructions, resourceLink, extraTask);
						forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
					}else{
						
					}
					
					break;
				}
				
				
			}
			
		} catch (DatabaseException | ValidationException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("title", title);
			request.setAttribute("instructions", instructions);
			forwardTo = "/jsp/treatment-plans/task-create-template.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	/**
	 * Gets all the parameter values from request, except user info which is set in doPost()
	 */
	private void getParametersFromRequest(HttpServletRequest request){
		//get all parameters associated with Task and do null checks
				taskID = getIntParameter(request, "taskID");
				stageID = getIntParameter(request, "stageID");
				taskTypeID =  getIntParameter(request, "taskTypeID");
				parentTaskID =  getIntParameter(request, "parentTaskID");//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
				title = request.getParameter("taskTitle");
				instructions = request.getParameter("taskInstructions");
				resourceLink = request.getParameter("resourceLink");
				//completed = request.getParameter("requestedAction");
				//dateCompleted = request.getParameter("requestedAction"); - set in the service layer when task is marked complete
				taskOrder =  getIntParameter(request, "taskOrder");
				extraTask = ((request.getParameter("isExtraTask")!=null) ? request.getParameter("isExtraTask").equalsIgnoreCase("true") : false);   ;
	}
	
	private int getIntParameter(HttpServletRequest request, String intParameterName){
		if(request.getParameter(intParameterName)==null || request.getParameter(intParameterName).isEmpty()){
			return 0;
		} else{
			return Integer.parseInt(request.getParameter("taskID"));
		}
	}

}
