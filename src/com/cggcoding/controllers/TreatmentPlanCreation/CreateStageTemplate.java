package com.cggcoding.controllers.TreatmentPlanCreation;

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
		
		
		try{
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (creationStep){
					case "beginning":
						//get treatment issues associated with admin role
						ArrayList<TreatmentIssue> treatmentIssues = dbActionHandler.getDefaultTreatmentIssues();
						session.setAttribute("defaultTreatmentIssues", treatmentIssues);
						forwardTo = "/jsp/treatment-plans/create-stage-template.jsp";
						break;
		            case "stageName":
		                if(stageName.isEmpty() || stageDescription.isEmpty()){
		                	throw new ValidationException("You must enter a stage name and description.");
		                }
		                
		                //TODO use factory here?
		                Stage newStageTemplate = new Stage(user.getUserID(), stageName, stageDescription);
		                
		                //validate plan name to make sure it doesn't exist for this user, if not then submit to be inserted into database
		                dbActionHandler.createStageTemplate(newStageTemplate);
		                
		                request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_CREATE);

		                forwardTo = "/jsp/admintools/adminMainMenu.jsp";

				}

			}
			
			
		} catch (ValidationException | DatabaseException e){
			request.setAttribute("stageName", stageName);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("errorMessage", e.getMessage());

            forwardTo = "/jsp/treatment-plans/create-stage-template.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
		
	}

}
