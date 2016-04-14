package com.cggcoding.controllers.therapist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.SMTPEmailer;
import com.cggcoding.messaging.invitations.Invitation;
import com.cggcoding.messaging.invitations.InvitationHandler;
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.SuccessMessages;

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
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		/*-----------End Common Servlet variables---------------*/
		
		//these variables are instantiated outside the try block so they can be accessed in the catch block
		String clientUUID = "";
		UserTherapist therapistUser = null;;
		List<Invitation> invitations = null;
		try {
			if(user.hasRole(Constants.USER_THERAPIST)){
				therapistUser = (UserTherapist)user;
				Map<String, UserClient> encodedClientMap = therapistUser.getUuidToClientMap();
				
				invitations = therapistUser.getInvitationsSent();
				
				clientUUID = request.getParameter("clientUUID");
				
				User client = therapistUser.getClientFromUUID(clientUUID);
				request.setAttribute("client", client);
				
				//TODO do I still need this or can it be handled by treatmentPlanID?
				int coreTreatmentPlanID = ParameterUtils.parseIntParameter(request, "coreTreatmentPlanID");
				
				//set the default treatment plans and the custom plans for this therapist into the request
				request.setAttribute("coreTreatmentPlansList", TreatmentPlan.getCoreTreatmentPlans());
				
				//based on the client retrieved from the request, load all plans that the therapist has access to
				
				switch(requestedAction){
					case "client-management-menu":
						//get list of clients for the therapist who is logged in and put that list in the request
						request.setAttribute("encodedClientMap", encodedClientMap);
						
						
						forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_MAIN;
						break;
					case "select-client":
						forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
						break;
					case "load-client-view-treatment-plan":
						int clientTreatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
						TreatmentPlan selectedPlan = TreatmentPlan.load(clientTreatmentPlanID);
						//Stage activeStage = selectedPlan.getActiveViewStage();
						
						selectedPlan.setTasksDisabledStatus(therapistUser.getUserID());
						
						//request.setAttribute("activeStage", activeStage);
						request.setAttribute("treatmentPlan", selectedPlan);
						forwardTo = "/WEB-INF/jsp/client-tools/run-treatment-plan.jsp";
						break;
					case "select-treatment-plan-for-assignment":
						//TODO load the preview of the selected treatment plan and put into request
						
						request.setAttribute("scrollTo", "selectTreatmentPlan");
						
						forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
						break;
					case "copy-plan-to-client":
						boolean isTemplate = false;
						//therapistUser.copyTreatmentPlanForClient(clientUserID, coreTreatmentPlanID, isTemplate);
						therapistUser.createTreatmentPlanFromTemplate(client.getUserID(), coreTreatmentPlanID);
						request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_COPIED_TO_CLIENT);
						forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
						break;
						
					case "delete-plan":
						TreatmentPlan.delete(treatmentPlanID);
		    
		            	request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_DELETED);
		            	forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
						break;
					case "invite-client":
						ServletContext context = session.getServletContext();
						System.out.println("Context Path: " + context.getContextPath());
						
						String recipientEmail = request.getParameter("recipientInvitationEmail");
						String recipientFirstName = request.getParameter("recipientFirstName");
						String recipientLastName = request.getParameter("recipientLastName");
						Invitation invitation = Invitation.createInvitation(user.getUserID(), recipientEmail, recipientFirstName, recipientLastName);
						InvitationHandler.sendInvitation(invitation, user, recipientEmail);
						
						invitations.add(invitation);
						
						forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
						request.setAttribute("successMessage", SuccessMessages.INVITATION_SENT_SUCCESS);
						request.setAttribute("encodedClientMap", encodedClientMap);
						break;
					case "invitation-delete":
						String invitationCode = request.getParameter("invitationCode");
						Invitation.delete(invitationCode);
						
						//reload invitation list
						invitations = therapistUser.getInvitationsSent();
						
						forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
						request.setAttribute("successMessage", SuccessMessages.INVITATION_DELETED);
						request.setAttribute("encodedClientMap", encodedClientMap);
						break;
				}
				
				CommonServletFunctions.putClientPlansInRequest(request, therapistUser, client.getUserID());
				
				//put these back in the request so other forms can maintain selections of other forms as well as display selected items of the dropdown boxes
				request.setAttribute("client", client);
				request.setAttribute("clientUUID", clientUUID);
				request.setAttribute("coreTreatmentPlanID", coreTreatmentPlanID);
				
				
				request.setAttribute("invitationList", invitations);
			}
		
		}catch(DatabaseException | ValidationException e){

			if(requestedAction.equals("select-client")||requestedAction.equals("invite-client")){
				request.setAttribute("encodedClientMap", therapistUser.getUuidToClientMap());
				request.setAttribute("invitationList", invitations);
				forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
			} else {
				request.setAttribute("activeAssignedClientPlans", therapistUser.loadActiveAssignedClientTreatmentPlans());
				request.setAttribute("unstartedAssignedClientPlans", therapistUser.loadUnstartedAssignedClientTreatmentPlans());
				request.setAttribute("completedAssignedClientPlans", therapistUser.loadCompletedAssignedClientTreatmentPlans());
				
				forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
			}

			request.setAttribute("clientUUID", clientUUID);
			
			request.setAttribute("errorMessage", e.getMessage());
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	

}
