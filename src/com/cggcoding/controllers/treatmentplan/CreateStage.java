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
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class CreateStage
 */
@WebServlet("/secure/CreateStage")
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
		processRequest(request, response);
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
		
		//maintain treatmentPlanID for when wanting to return user to main edit plan page
		request.setAttribute("treatmentPlanID", treatmentPlanID);
		
		try{
			if(!path.equals("stageTemplate")){
				treatmentPlan = TreatmentPlan.load(treatmentPlanID);
			}
			
			defaultStages = Stage.getDefaultStages();
			
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
					case "stage-create-start":

						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-create.jsp";
						break;
					case "stage-add-default-template":
						
						if(selectedDefaultStageID != 0){
							//TODO delete? treatmentPlan = TreatmentPlan.load(treatmentPlanID);
							treatmentPlan.addStageTemplate(selectedDefaultStageID);
							//treatmentPlan.copyStageIntoTreatmentPlan(selectedDefaultStageID);
	
			            	if(path.equals("treatmentPlanTemplate")){
			                	request.setAttribute("successMessage", SuccessMessages.STAGE_ADDED_TO_TREATMENT_PLAN);
			                	
			                	//freshly load the treatment plan so it has the newly created stage included when returning to the edit plan page
			                	CommonServletFunctions.setDefaultTreatmentIssuesInRequest(request);
			                	CommonServletFunctions.setDefaultTreatmentPlansInRequest(request);
			                	
			                	//OPTIMIZE loading the plan twice here - possible improvement would be to have plan.addStageTemplate add the stage to the local stages List so would need to have the dao method return a stage 
			                	//need to reload the plan with the newly added stage
			                	treatmentPlan = TreatmentPlan.load(treatmentPlanID);
			                	forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
			                }
						}
		            	break;
		            case "stage-create-title":
		            	
		                if(stageTitle.isEmpty() || stageDescription.isEmpty()){
		                	throw new ValidationException(ErrorMessages.STAGE_TITLE_DESCRIPTION_MISSING);
		                }
		                
		                Stage newStage = null;
		                if(path.equals("stageTemplate")){
		                	newStage = Stage.createTemplate(userAdmin.getUserID(), stageTitle, stageDescription);
		                } else {
		                	newStage = Stage.createTemplate(userAdmin.getUserID(), stageTitle, stageDescription);
		                	//TODO delete? treatmentPlan = TreatmentPlan.load(treatmentPlanID);
		                	treatmentPlan.addStageTemplate(newStage.getStageID());
		                	
		                	/*treatmentPlan = TreatmentPlan.load(treatmentPlanID);
		                	newStage = treatmentPlan.createNewStage(userAdmin.getUserID(), stageTitle, stageDescription);*/
		                }

		                request.setAttribute("stage", newStage);
		                
		                
		                if(path.equals("treatmentPlanTemplate")){
		                	
		                	request.setAttribute("successMessage", SuccessMessages.STAGE_ADDED_TO_TREATMENT_PLAN);
		                }else{
		                	request.setAttribute("successMessage", SuccessMessages.STAGE_TEMPLATE_BASIC_CREATE);
		                }
		                forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
		                break;
		            case("add-stage-to-treatment-plan"):
						//set all user-independent lists into request
						request.setAttribute("defaultStages", defaultStages);
		            	//TODO delete? treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-create.jsp";
						break;
		            default:

		                forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
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
			
            forwardTo = "/WEB-INF/jsp/treatment-plans/stage-create.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}


}
