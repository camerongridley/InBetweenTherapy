package com.cggcoding.controllers.account;

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
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class AccountManagement
 */
@WebServlet("/secure/AccountManagement")
public class AccountManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountManagement() {
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

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		String destination = request.getParameter("destination");
		
		
		if(user!=null && requestedAction != null){

			switch(requestedAction){
				case "user-account-management":
					forwardTo = Constants.URL_ACCOUNT_MANAGEMENT;
					break;
				case "user-edit-account-info":

					forwardTo = Constants.URL_ACCOUNT_EDIT;
					break;
				case "user-update-account-info":
					String originalUserName= user.getUserName();
					String originalFirstName = user.getFirstName();
					String originalLastName = user.getLastName();
					String originalEmail = user.getEmail();
					
					String userName = request.getParameter("userName");
					String firstName = request.getParameter("firstName");
					String lastName = request.getParameter("lastName");
					String email = request.getParameter("email");
					String password = request.getParameter("password");
					String newPassword = request.getParameter("newPassword");
					String newPasswordConfirm = request.getParameter("newPasswordConfirm");
					
					
					user.setUserName(userName);
					user.setFirstName(firstName);
					user.setLastName(lastName);
					user.setEmail(email);
					
				try {
					user.update(password, newPassword, newPasswordConfirm);
					request.setAttribute("successMessage", SuccessMessages.USER_UPDATED);
					forwardTo = user.getMainMenuURL();
				} catch (DatabaseException | ValidationException e) {
					//set the user fields back to original state
					user.setUserName(originalUserName);
					user.setFirstName(originalFirstName);
					user.setLastName(originalLastName);
					user.setEmail(originalEmail);
					request.setAttribute("errorMessage", e.getMessage());
					forwardTo = Constants.URL_ACCOUNT_EDIT;
					e.printStackTrace();
				}
					
					
					
					break;
			}
			switch(user.getRole()){
			case "admin":
				
				break;
			case "therapist":

				break;
			case "client":

				break;
			default:
				
				break;
			}
		}
		
		
		request.getRequestDispatcher(forwardTo).forward(request, response);

    }
	
}
