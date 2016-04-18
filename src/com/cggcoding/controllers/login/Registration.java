package com.cggcoding.controllers.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.invitations.InvitationHandler;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.security.PasswordEncryptionService;
import com.cggcoding.utils.Constants;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//processRequest(request, response);
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
		/*-----------End Common Servlet variables---------------*/
		
		String userName = request.getParameter("userName");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String password = request.getParameter("password");
		String passwordConfirm = request.getParameter("passwordConfirm");
		String email = request.getParameter("email");
		String userRole = request.getParameter("userRole");
		String invitationCode = request.getParameter("invitationCode");
		
		try{
			
			user = User.registerNewUser(userName, firstName, lastName, password, passwordConfirm, email, userRole, invitationCode);

			request.getSession().setAttribute("user", user);
			
			forwardTo = user.getMainMenuURL();
			
		}catch (DatabaseException | ValidationException e){
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("userName", userName);
			request.setAttribute("email", email);
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			request.setAttribute("invitationCode", invitationCode);
			request.setAttribute("userRole", userRole);
			forwardTo = Constants.URL_REGISTRATION;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
