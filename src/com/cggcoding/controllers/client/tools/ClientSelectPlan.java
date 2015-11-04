package com.cggcoding.controllers.client.tools;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;

/**
 * Servlet implementation class ClientSelectPlan
 */
@WebServlet("/ClientSelectPlan")
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
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		try {
			if(user.hasRole(Constants.USER_CLIENT)){
				UserClient client = (UserClient)user;
				
				switch(requestedAction){
				case "select-plan-start":
					
					request.setAttribute("assignedPlansList", client.getAssignedTreatmentPlanIDs());
					forwardTo= "/jsp/client-tools/start-new-plan.jsp";
					break;
				case "select-plan-load":
					int assignedTreatmentPlanID = ParameterUtils.parseIntParameter(request, "assignedTreatmentPlanID");
					TreatmentPlan selectedPlan = TreatmentPlan.load(assignedTreatmentPlanID);
					selectedPlan.initialize();
					
					client.addTreatmentPlan(selectedPlan);
					client.setActiveTreatmentPlanId(assignedTreatmentPlanID);

					request.setAttribute("treatmentPlan", selectedPlan);
					forwardTo = "/jsp/client-tools/run-treatment-plan.jsp";
					break;
				}
			}
		} catch (DatabaseException | ValidationException e) {
			request.setAttribute("errorMessage", e.getMessage());
			e.printStackTrace();
			forwardTo = "/jsp/client-tools/client-main-menu.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);

	}

}
