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
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class EditStage
 */
@WebServlet("/EditStage")
public class EditStage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditStage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		
		if(user.hasRole("admin")){
			int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
			int stageID = ParameterUtils.parseIntParameter(request, "stageID");
			int taskID = ParameterUtils.parseIntParameter(request, "taskID");
			
			Stage stage = null;
			try {
				switch(requestedAction){
					case "editStage":
						break;
					case "deleteStage":
						break;
					
				}
				stage = Stage.load(stageID);
				request.setAttribute("stage", stage);
			} catch (DatabaseException | ValidationException e) {
				request.setAttribute("errorMessage", e.getMessage());
				e.printStackTrace();
			}
			
			
			request.getRequestDispatcher("/jsp/treatment-plans/stage-edit.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		String stageIDAsString = request.getParameter("stageID");
		String stageTitle = request.getParameter("stageTitle");
		String stageDescription = request.getParameter("stageDescription");
		Stage editedStage = null;
		
		
		try{
			request.setAttribute("defaultStageList", DefaultDatabaseCalls.getDefaultStages());
			
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
		            case "stage-edit-start" :
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            case "stage-edit-select-stage" :
		            	int selectedDefaultStageID = Integer.parseInt(request.getParameter("selectedDefaultStageID"));
		            	request.setAttribute("stage", Stage.load(selectedDefaultStageID));
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            case "stage-edit-name" :
		            	if(stageIDAsString.isEmpty()){
		            		throw new ValidationException(ErrorMessages.STAGE_UPDATE_NO_SELECTION);
		            	}else{
			            	int stageID = Integer.parseInt(stageIDAsString);
			            	editedStage = Stage.load(stageID);
			            	editedStage.setTitle(stageTitle);
			            	editedStage.setDescription(stageDescription);
			            	editedStage.update();
			            	
			            	request.setAttribute("stage", editedStage);
			            	if(path.equals("editingPlanTemplate") || path.equals("creatingPlanTemplate")){
			            		request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
			            		request.setAttribute("treatmentPlan", TreatmentPlan.load(editedStage.getTreatmentPlanID()));
			            		forwardTo = "/jsp/treatment-plans/treatment-plan-edit.jsp";
			            	}else{
			            		request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
			            		forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
			            	}
			            	
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
			
			request.setAttribute("stage", editedStage);
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
            forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
