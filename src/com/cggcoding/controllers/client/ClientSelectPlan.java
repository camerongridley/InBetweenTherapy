package com.cggcoding.controllers.client;

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
import com.cggcoding.models.UserClient;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.SuccessMessages;
import com.cggcoding.utils.messaging.WarningMessages;

/**
 * Servlet implementation class ClientSelectPlan
 */
@WebServlet("/secure/ClientSelectPlan")
public class ClientSelectPlan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientSelectPlan() {
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
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		List<TreatmentPlan> assignedPlansList = null;
		List<TreatmentPlan> inProgressPlansList = null;
		List<TreatmentPlan> completedPlansList = null;
		
		int treatmentPlanID = 0;
		TreatmentPlan treatmentPlan = null;
		UserClient client = null;
		
		try {
			if(user.hasRole(Constants.USER_CLIENT)){
				client = (UserClient)user;
				
				treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
				
				switch(requestedAction){
					case "select-plan-start":
						prepareSelectPlanPage(request, client);
						/*client.loadAllClientTreatmentPlans();
						assignedPlansList = client.getAssignedTreatmentPlans();
						inProgressPlansList = client.getInProgressTreatmentPlans();
						completedPlansList = client.getCompletedTreatmentPlans();
						
						request.setAttribute("assignedPlansList", assignedPlansList);
						request.setAttribute("inProgressPlansList", inProgressPlansList);
						request.setAttribute("completedPlansList", completedPlansList);*/
						
						forwardTo = Constants.URL_CLIENT_SELECT_PLAN;
						break;
					case "make-active-plan":
						treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						
						client.setActiveTreatmentPlanID(treatmentPlanID);
						client.updateActiveTreatmentPlanID();
						
						if(request.getParameter("initialize").equals("yes")){
							treatmentPlan.initialize();
						}
						
						request.setAttribute("treatmentPlan", treatmentPlan);
						forwardTo = Constants.URL_CLIENT_MAIN_MENU;
						
						break;
					case "select-plan-load":
						treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						
						//set the active stage view to that of the current stage
						treatmentPlan.setActiveViewStageIndex(treatmentPlan.getCurrentStageIndex());
						
						client.setActiveTreatmentPlanID(treatmentPlanID);
						
						if(request.getParameter("initialize").equals("yes")){
							treatmentPlan.initialize();
						}
						
						
						
						//TODO delete me if ok - Stage activeStage = selectedPlan.getActiveViewStage();
						
						//TODO delete me if ok - request.setAttribute("activeStage", activeStage);
						request.setAttribute("treatmentPlan", treatmentPlan);
						forwardTo = Constants.URL_RUN_TREATMENT_PLAN;
						break;
						
					case "select-plan-preview":
						treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						
						//set the active stage view to that of the current stage
						treatmentPlan.setActiveViewStageIndex(treatmentPlan.getCurrentStageIndex());
						
						treatmentPlan.setTasksDisabledStatus(client.getUserID(), true);
						request.setAttribute("warningMessage", WarningMessages.CLIENT_TREATMENT_PLAN_DISABLED);
						
						request.setAttribute("treatmentPlan", treatmentPlan);
						forwardTo = Constants.URL_RUN_TREATMENT_PLAN;
						
						break;
						
					case "delete-plan":
						TreatmentPlan.delete(treatmentPlanID);
		    
		            	request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_DELETED);
		            	
		            	prepareSelectPlanPage(request, client);
		            	
		            	forwardTo = Constants.URL_CLIENT_SELECT_PLAN;
						break;
				}
				
				request.setAttribute("client", client);
			}
		} catch (DatabaseException | ValidationException e) {
			request.setAttribute("errorMessage", e.getMessage());
			try {
				request.setAttribute("assignedPlansList", client.getAssignedTreatmentPlans());
				request.setAttribute("inProgressPlansList", client.getInProgressTreatmentPlans());
				request.setAttribute("completedPlansList", client.getCompletedTreatmentPlans());
			} catch (DatabaseException | ValidationException e1) {
				e1.printStackTrace();
			}
			
			
			e.printStackTrace();
			forwardTo = "/WEB-INF/jsp/client-tools/select-plan.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);

	}
	
	private void prepareSelectPlanPage(HttpServletRequest request, UserClient client) throws DatabaseException, ValidationException{
		client.loadAllClientTreatmentPlans();
		List<TreatmentPlan> assignedPlansList = client.getAssignedTreatmentPlans();
		List<TreatmentPlan> inProgressPlansList = client.getInProgressTreatmentPlans();
		List<TreatmentPlan> completedPlansList = client.getCompletedTreatmentPlans();
		
		request.setAttribute("assignedPlansList", assignedPlansList);
		request.setAttribute("inProgressPlansList", inProgressPlansList);
		request.setAttribute("completedPlansList", completedPlansList);
	}

}
