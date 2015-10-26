package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
import java.util.List;

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
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class CreateStage
 */
@WebServlet("/CreateStage")
public class CreateStage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateStage() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		if(user.hasRole("admin")){
			int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");

			try {
				switch(requestedAction){
				case("addStageToTreatmentPlan"):
					//set all user-independent lists into request
					request.setAttribute("defaultStages", DefaultDatabaseCalls.getDefaultStages());
					request.setAttribute("treatmentPlan", TreatmentPlan.load(treatmentPlanID));
					break;
				}
				
			} catch (DatabaseException | ValidationException e) {
				request.setAttribute("errorMessage", e.getMessage());
				e.printStackTrace();
			}
			
			
			request.getRequestDispatcher("/jsp/treatment-plans/stage-create.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		int selectedDefaultStageID = ParameterUtils.parseIntParameter(request, "defaultStageID");
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
		String stageTitle = request.getParameter("stageTitle");
    	String stageDescription = request.getParameter("stageDescription");
    	int stageOrder = ParameterUtils.parseIntParameter(request, "stageOrder");
    	String newStageGoal =request.getParameter("newStageGoal");
		TreatmentPlan treatmentPlan = null;
		List<Stage> defaultStages = null;
		
		try{
			
			defaultStages = DefaultDatabaseCalls.getDefaultStages();
			
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
					case "stage-create-start":

						forwardTo = "/jsp/treatment-plans/stage-create.jsp";
						break;
					case "stage-add-default":
		            	Stage defaultStage = Stage.load(selectedDefaultStageID);
		            	defaultStage.copy(treatmentPlanID, user.getUserID(), false);
		            	if(path.equals("editingPlanTemplate")){
		                	request.setAttribute("successMessage", SuccessMessages.STAGE_ADDED_TO_TREATMENT_PLAN);
		                	treatmentPlan = TreatmentPlan.load(treatmentPlanID);
		                	forwardTo = "/jsp/treatment-plans/treatment-plan-edit.jsp";
		                }
		            	break;
		            case "stage-create-title":

		                if(stageTitle.isEmpty() || stageDescription.isEmpty()){
		                	throw new ValidationException(ErrorMessages.STAGE_TITLE_DESCRIPTION_MISSING);
		                }
		                
		                boolean template = false;
		                if(path.equals("creatingStageTemplate")){
		                	template = true;
		                	treatmentPlanID = Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID;
		                } else {
		                	treatmentPlan = TreatmentPlan.load(treatmentPlanID);
		                }
		                
		                Stage newStage = Stage.getInstanceWithoutID(treatmentPlanID, user.getUserID(), stageTitle, stageDescription, stageOrder, template);
		                newStage.saveNew();

		                request.setAttribute("stage", newStage);
		                
		                
		                if(path.equals("editingPlanTemplate")){
		                	
		                	request.setAttribute("successMessage", SuccessMessages.STAGE_ADDED_TO_TREATMENT_PLAN);
		                }else{
		                	request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_BASIC_CREATE);
		                }
		                forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		                break;
		            default:

		                forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
		                break;
				}
				
				request.setAttribute("treatmentPlan", treatmentPlan);

			}
			
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - to maintain data and re-populate the forms
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("newStageGoal", newStageGoal);
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("defaultStages", defaultStages);
			
            forwardTo = "/jsp/treatment-plans/stage-create.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
		
	}


}
