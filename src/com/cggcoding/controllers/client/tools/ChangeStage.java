package com.cggcoding.controllers.client.tools;

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
@WebServlet("/ChangeStage")
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
        
        try {
	        //FIXME - dont get txPlan from user, load from db to prevent concurrency probs
	        //TreatmentPlan treatmentPlan = user.getTreatmentPlan(treatmentPlanID);
	        TreatmentPlan treatmentPlan = TreatmentPlan.load(treatmentPlanID);
	
	        int newViewID = Integer.parseInt(request.getParameter("stageIndex"));
	        treatmentPlan.setActiveViewStageIndex(newViewID);
	        Stage activeStage = treatmentPlan.getActiveViewStage();
	        
	        //FIXME change to only update basic info - though I think that is all update() does right now, should change method name to reflect this.
        
			treatmentPlan.update();
			
			request.setAttribute("activeStage", activeStage);
			request.setAttribute("treatmentPlan", treatmentPlan);
			
			forwardTo = "/jsp/client-tools/run-treatment-plan.jsp";
		} catch (ValidationException | DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        

        request.getRequestDispatcher(forwardTo).forward(request,response);

	}

}
