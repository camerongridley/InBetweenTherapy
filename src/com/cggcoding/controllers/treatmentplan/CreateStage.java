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
import com.cggcoding.models.MapTreatmentPlanStageTemplate;
import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class CreateStage
 */
@WebServlet("/secure/treatment-components/CreateStage")
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
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
		
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		/*-----------Common Treatment Plan object variables------------*/
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		TreatmentPlan treatmentPlan = null;
		Stage stage = null;
		Task task = null;
		int ownerUserID = 0;
		User owner = null;
		/*-----------End Treatment Plan object variables---------------*/
		
		//maintain clientUUID value for therapist
    	String clientUUID = request.getParameter("clientUUID");
		request.setAttribute("clientUUID", clientUUID);
		
		int selectedCoreStageID = ParameterUtils.parseIntParameter(request, "coreStageID");
		String stageTitle = request.getParameter("stageTitle");
    	String stageDescription = request.getParameter("stageDescription");
    	int stageOrder = ParameterUtils.parseIntParameter(request, "stageOrder");
    	String newStageGoal =request.getParameter("newStageGoal");
		List<Stage> coreStages = null;
		
		try{
			//check if this a therapist is accessing a client's data and authorize
			if(clientUUID != null && !clientUUID.isEmpty()){
				user.isAuthorizedForClientData(clientUUID);				
			}
			
			if(!path.equals(Constants.PATH_TEMPLATE_STAGE)){
				treatmentPlan = TreatmentPlan.load(treatmentPlanID);
				ownerUserID = treatmentPlan.getUserID();
			}
			
			//Set the User var "owner". If the owner of the plan that is being edited is different than the logged in user, then load the appropriate owner info
    		if(ownerUserID==user.getUserID() || ownerUserID==0){
    			owner = user;
    		} else {
    			owner = User.loadBasic(ownerUserID);
    		}
			
			coreStages = Stage.getCoreStages();
			
			if(user.hasRole(Constants.USER_ADMIN) || user.hasRole(Constants.USER_THERAPIST)){				
				switch (requestedAction){
					case "stage-create-start":

						forwardTo = Constants.URL_CREATE_STAGE;
						break;
					case("add-stage-to-treatment-plan"):
						//set all user-independent lists into request
						request.setAttribute("coreStages", coreStages);
		            	//TODO delete? treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						forwardTo = Constants.URL_CREATE_STAGE;
						break;
					case "stage-add-template":
						if(request.getParameter("submitButton").equals("save")){
							if(selectedCoreStageID != 0){
				            	if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN)){
				            		treatmentPlan.addStageTemplate(selectedCoreStageID);
				                	
				                	//freshly load the treatment plan so it has the newly created stage included when returning to the edit plan page
				                	CommonServletFunctions.setCoreTreatmentIssuesInRequest(request);
				                	CommonServletFunctions.setCoreTreatmentPlansInRequest(request);
				                	
				                	forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
				                } else if (path.equals(Constants.PATH_MANAGE_CLIENT)){
				                	MapTreatmentPlanStageTemplate platStageInfo = null;//should there be any attributes to be passed, like if stage repetitions was added, they would go in this. for now, none of it's properties are relevant in this particular situation
				                	treatmentPlan.createStageFromTemplate(selectedCoreStageID, platStageInfo);
				                	CommonServletFunctions.setCoreTreatmentIssuesInRequest(request);
				                	forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
				                }
				            	
				            	request.setAttribute("successMessage", SuccessMessages.STAGE_ADDED_TO_TREATMENT_PLAN);
							}
						} else {
							//Cancel button pressed to send back to appropriate page
							forwardTo = setForwardToForCancel(request, user, path);
						}
						
		            	break;
		            case "stage-create-new": //TODO rename to stage-create-new
		            	
		            	if(request.getParameter("submitButton").equals("save")){
		            		if(stageTitle.isEmpty() || stageDescription.isEmpty()){
			                	throw new ValidationException(ErrorMessages.STAGE_TITLE_DESCRIPTION_MISSING);
			                }
			                
			                Stage newStage = null;
			                if(path.equals(Constants.PATH_TEMPLATE_STAGE)){
			                	newStage = Stage.createTemplate(user.getUserID(), stageTitle, stageDescription);
			                	request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_BASIC_CREATE);
			                	forwardTo = Constants.URL_EDIT_STAGE;
			                } else {
			                	if(path.equals(Constants.PATH_MANAGE_CLIENT)){
			                		newStage = treatmentPlan.createClientStage(stageTitle, stageDescription);
			                		forwardTo = Constants.URL_EDIT_STAGE;
			                	} else if (path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN)){
			                		//TODO write new method in TreatmentPlan that combines these two lines?
			                		newStage = Stage.createTemplate(user.getUserID(), stageTitle, stageDescription);
			                		treatmentPlan.addStageTemplate(newStage.getStageID());
			                		request.setAttribute("successMessage", SuccessMessages.STAGE_ADDED_TO_TREATMENT_PLAN);
			                		forwardTo = Constants.URL_EDIT_STAGE;
			                		CommonServletFunctions.setCoreTreatmentIssuesInRequest(request);
			                	}

			                }

			                stage = newStage;
		            	} else {
		            		//Cancel button pressed to send back to appropriate page
		            		forwardTo = setForwardToForCancel(request, user, path);
		            	}
		                
		                
		                break;
		            
		            default:

		                forwardTo = Constants.URL_ADMIN_MAIN_MENU;
		                break;
				}
				
				request.setAttribute("treatmentPlan", treatmentPlan);
				request.setAttribute("stage", stage);
				request.setAttribute("task", task);
				request.setAttribute("owner", owner);

			}
			
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - to maintain data and re-populate the forms
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("newStageGoal", newStageGoal);
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("coreStages", coreStages);
			request.setAttribute("owner", owner);
			
            forwardTo = Constants.URL_CREATE_STAGE;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

	private String setForwardToForCancel(HttpServletRequest request, User user, String path) throws DatabaseException, ValidationException{
		String forwardTo = "";
		switch(path){
		case Constants.PATH_TEMPLATE_STAGE:
			forwardTo = user.getMainMenuURL();
			break;
		case Constants.PATH_TEMPLATE_TREATMENT_PLAN:
		case Constants.PATH_MANAGE_CLIENT:	
			request.setAttribute("coreTreatmentPlansList", TreatmentPlan.getCoreTreatmentPlans());
			forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
			break;
	}
		
		return forwardTo;
	}

}
