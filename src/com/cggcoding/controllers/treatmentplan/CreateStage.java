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
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
		String stageTitle = request.getParameter("stageTitle");
    	String stageDescription = request.getParameter("stageDescription");
    	int stageOrder = ParameterUtils.parseIntParameter(request, "stageOrder");
    	boolean template = ParameterUtils.getBooleanParameter(request, "template");
    	String newStageGoal =request.getParameter("newStageGoal");
		
		
		try{
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
					case "stage-create-start":
						//get the TreatmentPlan for this stage and put in request
		                loadTreatmentPlanFromDatabaseIntoRequest(request, treatmentPlanID);
		                
						forwardTo = "/jsp/treatment-plans/stage-create.jsp";
						break;
		            case "stage-create-title":
		            	//get the TreatmentPlan for this stage and put in request
		                loadTreatmentPlanFromDatabaseIntoRequest(request, treatmentPlanID);
		                
		                if(stageTitle.isEmpty() || stageDescription.isEmpty()){
		                	throw new ValidationException("You must enter a stage name and description.");
		                }

		                Stage newStageTemplate = Stage.getInstanceWithoutID(treatmentPlanID, user.getUserID(), stageTitle, stageDescription, stageOrder, template);
		                newStageTemplate.setTreatmentPlanID(ParameterUtils.parseIntParameter(request, "treatmentPlanID"));
		                newStageTemplate.saveNew();

		                request.setAttribute("stage", newStageTemplate);
		                request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_BASIC_CREATE);
		                forwardTo = "/jsp/treatment-plans/task-create.jsp";
		                break;
		            case "stage-add-goal":
		            	//Stage stageWithNewGoal = new Stage()
		            	Stage stage = (Stage)request.getAttribute("stage");//dbActionHandler.getStageTemplate(user.getUserID(), Integer.parseInt(request.getParameter("stageID")));
		            	request.setAttribute("stage", stage);

		                forwardTo = "/jsp/treatment-plans/stage-create-details.jsp";
		            	break;
		            case "stage-add-tasks":
		            	
		            	break;
		            /*case "stage-edit-start" :
		            	session.setAttribute("defaultStageList", DefaultDatabaseCalls.getDefaultStages());
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            case "stage-edit-select-stage" :
		            	int selectedDefaultStageID = Integer.parseInt(request.getParameter("selectedDefaultStageID"));
		            	request.setAttribute("selectedDefaultStage", DefaultDatabaseCalls.getDefaultStageByID(selectedDefaultStageID));
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            case "stage-edit-name" :
		            	if(stageIDAsString.isEmpty()){
		            		throw new ValidationException(ErrorMessages.STAGE_UPDATE_NO_SELECTION);
		            	}else{
			            	int stageID = Integer.parseInt(stageIDAsString);
			            	Stage stageToUpdate = DefaultDatabaseCalls.getDefaultStageByID(stageID);
			            	stageToUpdate.setTitle(stageTitle);
			            	stageToUpdate.setDescription(stageDescription);
			            	stageToUpdate.updateInDatabase();
			            	
			            	request.setAttribute("selectedDefaultStage", stageToUpdate);
			            	
			            	forwardTo = "/jsp/treatment-plans/stage-edit-goals.jsp";
		            	}

		            	break;*/
		            default:

		                forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
				}

			}
			
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - to maintain data and re-populate the forms
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("newStageGoal", newStageGoal);
			
            forwardTo = "/jsp/treatment-plans/stage-create.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
		
	}
	
	private void loadTreatmentPlanFromDatabaseIntoRequest(HttpServletRequest request, int treatmentPlanID) throws DatabaseException{
		TreatmentPlan treatmentPlan = TreatmentPlan.loadBasic(treatmentPlanID);
        request.setAttribute("treatmentPlan", treatmentPlan);
	}

}
