package com.cggcoding.controllers.therapist;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;

/**
 * Servlet implementation class ManageClients
 */
@WebServlet("/secure/ManageClients")
public class ManageClients extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageClients() {
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
		
		
		
		try {
			if(user.hasRole(Constants.USER_THERAPIST)){
				UserTherapist therapistUser = (UserTherapist)user;
				Map<Integer, UserClient> clientMap = therapistUser.loadClients();
				
				int clientUserID = ParameterUtils.parseIntParameter(request, "clientUserID");
				
				User client = clientMap.get(clientUserID);
				request.setAttribute("client", client);
				
				int defaultTreatmentPlanID = ParameterUtils.parseIntParameter(request, "defaultTreatmentPlanID");
				
				//set the default treatment plans and the custom plans for this therapist into the request
				request.setAttribute("defaultTreatmentPlanList", TreatmentPlan.getDefaultTreatmentPlans());
				switch(requestedAction){
					case "client-management-menu":
						//get list of clients for the therapist who is logged in and put that list in the request
						request.setAttribute("clientMap", clientMap);
						
						forwardTo = "/WEB-INF/jsp/therapist-tools/manage-clients-main.jsp";
						break;
					case "select-client":
						List<TreatmentPlan> assignedClientPlans = therapistUser.getAssignedClientTreatmentPlans(clientUserID);
						request.setAttribute("assignedClientPlans", assignedClientPlans);
						forwardTo = "/WEB-INF/jsp/therapist-tools/manage-client-plans.jsp";
						break;
					case "load-client-view-treatment-plan":
						int clientTreatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
						TreatmentPlan selectedPlan = TreatmentPlan.load(clientTreatmentPlanID);
						Stage activeStage = selectedPlan.getActiveViewStage();
						
						selectedPlan.setTasksDisabledStatus(therapistUser.getUserID());
						
						request.setAttribute("activeStage", activeStage);
						request.setAttribute("treatmentPlan", selectedPlan);
						forwardTo = "/WEB-INF/jsp/client-tools/run-treatment-plan.jsp";
						break;
					case "copy-plan-to-client":
						boolean isTemplate = false;
						therapistUser.copyTreatmentPlanForClient(clientUserID, defaultTreatmentPlanID, isTemplate);
						
						forwardTo = "/WEB-INF/jsp/therapist-tools/manage-clients-main.jsp";
						break;
				}
				
				//put these back in the request so other forms can maintain selections of other forms as well as display selected items of the dropdown boxes
				request.setAttribute("clientUserID", clientUserID);
				request.setAttribute("defaultTreatmentPlanID", defaultTreatmentPlanID);
			}
		
		}catch(DatabaseException | ValidationException e){
			forwardTo = "/WEB-INF/jsp/therapist-tools/manage-clients-main.jsp";
			request.setAttribute("errorMessage", e.getMessage());
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	

}
