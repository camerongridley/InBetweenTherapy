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
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Servlet implementation class EditStageTemplate
 */
@WebServlet("/EditStageTemplate")
public class EditStageTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditStageTemplate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		HttpSession session = request.getSession();
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("chosenAction");
		String stageIDAsString = request.getParameter("stageID");
		String stageName = request.getParameter("stageName");
		String stageDescription = request.getParameter("stageDescription");
		
		
		try{
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
		            case "edit-stage-start" :
		            	session.setAttribute("defaultStageList", DefaultDatabaseCalls.getDefaultStages());
		            	forwardTo = "/jsp/treatment-plans/stage-edit-template.jsp";
		            	break;
		            case "edit-stage-select-stage" :
		            	int selectedDefaultStageID = Integer.parseInt(request.getParameter("selectedDefaultStageID"));
		            	request.setAttribute("selectedDefaultStage", DefaultDatabaseCalls.getDefaultStageByID(selectedDefaultStageID));
		            	forwardTo = "/jsp/treatment-plans/stage-edit-template.jsp";
		            	break;
		            case "edit-stage-name" :
		            	if(stageIDAsString.isEmpty()){
		            		throw new ValidationException(ErrorMessages.STAGE_UPDATE_NO_SELECTION);
		            	}else{
			            	int stageID = Integer.parseInt(stageIDAsString);
			            	Stage stage =DefaultDatabaseCalls.getDefaultStageByID(stageID);
			            	stage.setName(stageName);
			            	stage.setDescription(stageDescription);
			            	stage.updateInDatabase();
			            	
			            	request.setAttribute("selectedDefaultStage", stage);
			            	
			            	forwardTo = "/jsp/treatment-plans/stage-edit-template-goals.jsp";
		            	}

		            	break;
		            case "stage-edit-add-goal" :
		            	
		            	break;
		            default:

		                forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
				}

			}
			
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("stageName", stageName);
			request.setAttribute("stageDescription", stageDescription);
            forwardTo = "/jsp/treatment-plans/stage-edit-template.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}