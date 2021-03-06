package com.cggcoding.controllers.client;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ChangeStage
 */
@WebServlet("/secure/ChangeStage")
public class ChangeStage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeStage() {
        super();
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

		int clientUserID = ParameterUtils.parseIntParameter(request, "clientUserID");
		User client = null;
        int treatmentPlanID = Integer.parseInt(request.getParameter("treatmentPlanID"));
        TreatmentPlan treatmentPlan = null;
        Stage activeStage  = null;
        
        try {
        	if(user.getRole().equals(Constants.USER_CLIENT)){
        		client = user;
        	} else if(user.getRole().equals(Constants.USER_THERAPIST)){
        		client = User.loadBasic(clientUserID);
        	}
        	
	        treatmentPlan = TreatmentPlan.load(treatmentPlanID);
	
	        int newViewID = Integer.parseInt(request.getParameter("stageIndex"));
	        treatmentPlan.setActiveViewStageIndex(newViewID);
	        activeStage = treatmentPlan.getActiveViewStage();
	        
			treatmentPlan.updateBasic();
			
			//TODO decide if I need to check the user role (client vs. therapist)
			treatmentPlan.setTasksDisabledStatus(user.getUserID());
			
			request.setAttribute("activeStage", activeStage);
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("client", client);
	
			forwardTo = Constants.URL_RUN_TREATMENT_PLAN;
		} catch (ValidationException | DatabaseException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("activeStage", activeStage);
			e.printStackTrace();
		}
        
        

        request.getRequestDispatcher(forwardTo).forward(request,response);

	}

}
