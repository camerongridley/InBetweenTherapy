package com.cggcoding.controllers.therapist;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.User;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.Constants;

/**
 * Servlet implementation class AssignTreatmentPlan
 */
@WebServlet("/AssignTreatmentPlan")
public class AssignTreatmentPlan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AssignTreatmentPlan() {

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
		if(user.hasRole(Constants.USER_THERAPIST)){
			UserTherapist therapistUser = (UserTherapist)user;
			switch(requestedAction){
				case "assign-treatment-plan-start":
					//get list of clients for the therapist who is logged in and put that list in the request
					request.setAttribute("clientMap", therapistUser.loadClients());
					
					//set the default treatment plans and the custom plans for this therapist into the request
					//TODO get and set lists
					
					forwardTo = "/jsp/therapist-tools/assign-treatment-plan.jsp";
				break;
			}
		}
		
		}catch(DatabaseException e){
			request.setAttribute("errorMessage", e.getMessage());
			System.out.println(e.getMessage());
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
