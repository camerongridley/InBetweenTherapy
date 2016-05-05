package com.cggcoding.controllers.login;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.invitations.Invitation;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * Servlet implementation class MasterController
 */
@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogIn() {
        super();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userRole = "";
        DatabaseActionHandler dao = new MySQLActionHandler();
    	
        
	        try {
	        	//XXX remove dao here and switch to using a static method in User
				//boolean userExists = dao.userValidate(email, password);

				//use the above to get authenticate the user and get create a User object
				//if(userExists){
					user = User.login(email, password);
					request.getSession().setAttribute("user", user);
					
					if(user.hasRole(Constants.USER_ADMIN)){
						UserAdmin admin = (UserAdmin)user;
						forwardTo = Constants.URL_ADMIN_MAIN_MENU;
					} else if(user.hasRole(Constants.USER_THERAPIST)){
						UserTherapist userTherapist = (UserTherapist)user;
						Map<String, UserClient> encodedClientMap = userTherapist.getUuidToClientMap();
						
						
						
						List<Invitation> invitations = userTherapist.getInvitationsSent();
						
						request.setAttribute("invitationList", invitations);
						request.setAttribute("encodedClientMap", encodedClientMap);
						
						//TODO load HashMaps of clientIDs/hashedIDs
						
						forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
					}if(user.hasRole(Constants.USER_CLIENT)){
						UserClient client = (UserClient)user;
						forwardTo = Constants.URL_CLIENT_MAIN_MENU;
					}
					
				/*} else {
					forwardTo = Constants.URL_LOGIN;
				    throw new ValidationException(ErrorMessages.INVALID_USERNAME_OR_PASSWORD);
				}*/
			} catch (DatabaseException | ValidationException e) {
				forwardTo = Constants.URL_LOGIN;
				request.setAttribute("errorMessage", e.getMessage());
				e.printStackTrace();
			}
	        
	        request.getRequestDispatcher(forwardTo).forward(request, response);

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
