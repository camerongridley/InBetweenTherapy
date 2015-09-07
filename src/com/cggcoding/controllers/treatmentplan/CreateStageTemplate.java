package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class CreateStageTemplate
 */
@WebServlet("/CreateStageTemplate")
public class CreateStageTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateStageTemplate() {
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
		DataSource datasource = (DataSource)request.getServletContext().getAttribute("datasource");
		MySQLActionHandler dbActionHandler = new MySQLActionHandler(datasource);
		HttpSession session = request.getSession();
		String forwardTo = "index.jsp";
		String creationStep = request.getParameter("chosenAction");
		String stageName = request.getParameter("stageName");
    	String stageDescription = request.getParameter("stageDescription");
    	String newStageGoal =request.getParameter("stageGoal");
		
		
		try{
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (creationStep){
					case "beginning":
						forwardTo = "/jsp/treatment-plans/stage-create-template.jsp";
						break;
		            case "stageName":
		                if(stageName.isEmpty() || stageDescription.isEmpty()){
		                	throw new ValidationException("You must enter a stage name and description.");
		                }
		                
		                //TODO use factory here?
		                Stage newStageTemplate = new Stage(user.getUserID(), stageName, stageDescription);
		                
		                //validate plan name to make sure it doesn't exist for this user, if not then submit to be inserted into database
		                newStageTemplate = dbActionHandler.createStageTemplate(newStageTemplate);

		                request.setAttribute("stage", newStageTemplate);
		                forwardTo = "/jsp/treatment-plans/stage-create-template-details.jsp";
		                break;
		            case "addGoal":
		            	Stage stage = (Stage)request.getAttribute("stage");//dbActionHandler.getStageTemplate(user.getUserID(), Integer.parseInt(request.getParameter("stageID")));
		            	request.setAttribute("stage", stage);
		            	
		            	
		            	
		                forwardTo = "/jsp/treatment-plans/stage-create-template-details.jsp";
		            	break;
		            case "stageGoalsTasks":
		            	
		            	
		            	request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_CREATE);
		                forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
				}

			}
			
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("stageName", stageName);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("newStageGoal", newStageGoal);
			
            forwardTo = "/jsp/treatment-plans/stage-create-template.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
		
	}

}
