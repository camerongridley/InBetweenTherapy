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
		
		try {
			if(user.hasRole(Constants.USER_CLIENT)){
				UserClient client = (UserClient)user;
				assignedPlansList = client.getAssignedTreatmentPlans();
				inProgressPlansList = client.getInProgressTreatmentPlans();
				completedPlansList = client.getCompletedTreatmentPlans();
				
				switch(requestedAction){
					case "select-plan-start":
						request.setAttribute("assignedPlansList", assignedPlansList);
						request.setAttribute("inProgressPlansList", inProgressPlansList);
						request.setAttribute("completedPlansList", completedPlansList);
						
						forwardTo = Constants.URL_CLIENT_SELECT_PLAN;
						break;
					case "select-plan-load":
						int assignedTreatmentPlanID = ParameterUtils.parseIntParameter(request, "selectedPlanID");
						TreatmentPlan selectedPlan = TreatmentPlan.load(assignedTreatmentPlanID);
						if(request.getParameter("initialize").equals("yes")){
							selectedPlan.initialize();
						}
						
						//set the active stage view to that of the current stage
						selectedPlan.setActiveViewStageIndex(selectedPlan.getCurrentStageIndex());
						
						client.setActiveTreatmentPlanId(assignedTreatmentPlanID);
						
						Stage activeStage = selectedPlan.getActiveViewStage();
						
						request.setAttribute("activeStage", activeStage);
						request.setAttribute("treatmentPlan", selectedPlan);
						forwardTo = Constants.URL_RUN_TREATMENT_PLAN;
						break;
				}
				
				request.setAttribute("client", client);
			}
		} catch (DatabaseException | ValidationException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("assignedPlansList", assignedPlansList);
			request.setAttribute("inProgressPlansList", inProgressPlansList);
			request.setAttribute("completedPlansList", completedPlansList);
			//e.printStackTrace();
			forwardTo = "/WEB-INF/jsp/client-tools/select-plan.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);

	}

}
