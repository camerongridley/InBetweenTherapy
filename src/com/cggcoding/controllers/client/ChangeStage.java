package com.cggcoding.controllers.client;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;

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
        User user = (User)request.getSession().getAttribute("user");
        String forwardTo = "index.jsp";
        int treatmentPlanID = Integer.parseInt(request.getParameter("treatmentPlanID"));
        TreatmentPlan treatmentPlan = null;
        Stage activeStage  = null;
        
        try {
	        treatmentPlan = TreatmentPlan.load(treatmentPlanID);
	
	        int newViewID = Integer.parseInt(request.getParameter("stageIndex"));
	        treatmentPlan.setActiveViewStageIndex(newViewID);
	        activeStage = treatmentPlan.getActiveViewStage();
	        
			treatmentPlan.updateBasic();
			
			//TODO decide if I need to check the user role (client vs. therapist)
			treatmentPlan.setTasksDisabledStatus(user.getUserID());
			
			request.setAttribute("activeStage", activeStage);
			request.setAttribute("treatmentPlan", treatmentPlan);
			
			forwardTo = "/WEB-INF/jsp/client-tools/run-treatment-plan.jsp";
		} catch (ValidationException | DatabaseException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("treatmentPlan", treatmentPlan);
			request.setAttribute("activeStage", activeStage);
			e.printStackTrace();
		}
        
        

        request.getRequestDispatcher(forwardTo).forward(request,response);

	}

}
