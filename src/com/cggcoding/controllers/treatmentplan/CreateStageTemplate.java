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
import com.cggcoding.helpers.DefaultDatabaseCalls;
import com.cggcoding.models.Stage;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
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
		HttpSession session = request.getSession();
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String stageIDAsString = request.getParameter("stageID");
		String stageTitle = request.getParameter("stageTitle");
    	String stageDescription = request.getParameter("stageDescription");
    	String newStageGoal =request.getParameter("newStageGoal");
		
		
		try{
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
					case "beginning":
						forwardTo = "/jsp/treatment-plans/stage-create-template.jsp";
						break;
		            case "stageTitle":
		                if(stageTitle.isEmpty() || stageDescription.isEmpty()){
		                	throw new ValidationException("You must enter a stage name and description.");
		                }

		                Stage newStageTemplate = Stage.saveNewTemplateInDatabase(user.getUserID(), stageTitle, stageDescription);

		                request.setAttribute("stage", newStageTemplate);
		                request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_BASIC_CREATE);
		                forwardTo = "/jsp/treatment-plans/stage-create-template-details.jsp";
		                break;
		            case "addGoal":
		            	//Stage stageWithNewGoal = new Stage()
		            	Stage stage = (Stage)request.getAttribute("stage");//dbActionHandler.getStageTemplate(user.getUserID(), Integer.parseInt(request.getParameter("stageID")));
		            	request.setAttribute("stage", stage);
		            	
		            	
		            	
		                forwardTo = "/jsp/treatment-plans/stage-create-template-details.jsp";
		            	break;
		            case "stageGoalsTasks":
		            	
		            	break;
		            case "edit-stage-start" :
		            	session.setAttribute("defaultStageList", DefaultDatabaseCalls.getDefaultStages());
		            	forwardTo = "/jsp/treatment-plans/stage-edit-template.jsp";
		            	break;
		            case "edit-stage-select-stage" :
		            	int selectedDefaultStageID = Integer.parseInt(request.getParameter("selectedDefaultStage"));
		            	request.setAttribute("selectedDefaultStage", DefaultDatabaseCalls.getDefaultStageByID(selectedDefaultStageID));
		            	forwardTo = "/jsp/treatment-plans/stage-edit-template.jsp";
		            	break;
		            case "edit-stage-name" :
		            	
		            	break;
		            default:

		                forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
				}

			}
			
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("newStageGoal", newStageGoal);
			
            forwardTo = "/jsp/treatment-plans/stage-create-template.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
		
	}

}
