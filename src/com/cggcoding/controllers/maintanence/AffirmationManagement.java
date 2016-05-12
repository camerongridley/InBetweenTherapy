package com.cggcoding.controllers.maintanence;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Affirmation;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.Constants;

/**
 * Servlet implementation class AffirmationManagement
 */
@WebServlet("/secure/AffirmationManagement")
public class AffirmationManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AffirmationManagement() {
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
		
		//maintain clientUUID value for therapist
    	String clientUUID = request.getParameter("clientUUID");
		request.setAttribute("clientUUID", clientUUID);
		
		Affirmation affirmation = new Affirmation();
		UserClient client = null;
		UserTherapist therapist = null;
		try {
			if(user.hasRole(Constants.USER_THERAPIST)){
				//check if this a therapist is accessing a client's data and authorize
				if(clientUUID != null && !clientUUID.isEmpty()){
					therapist = (UserTherapist)user;
					user.isAuthorizedForClientData(clientUUID);
					client = therapist.getClientFromUUID(clientUUID);
				}
			}
		
			
			switch(requestedAction){
			case "create-new-affirmation":
				affirmation.setAffirmation(request.getParameter("newAffirmation"));
				
				if(user.hasRole(Constants.USER_CLIENT)){
					client = (UserClient)user;
					affirmation.setUserID(user.getUserID());
					forwardTo = client.getMainMenuURL();
				}
				
				if(user.hasRole(Constants.USER_THERAPIST)){
					affirmation.setUserID(client.getUserID());
					forwardTo = therapist.getMainMenuURL();
				}
				
				affirmation.create();
				
			}
		} catch (ValidationException | DatabaseException e) {
			request.setAttribute("errorMessage", e.getMessage());
			e.printStackTrace();
		}	
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
