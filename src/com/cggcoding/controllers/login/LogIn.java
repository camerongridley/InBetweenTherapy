package com.cggcoding.controllers.login;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
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
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userRole = "";
        DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
    	
        
	        try {
				boolean userExists = databaseActionHandler.userValidate(email, password);

				//use the above to get authenticate the user and get create a User object
				if(userExists){
					user = databaseActionHandler.userLoadInfo(email, password);
					request.getSession().setAttribute("user", user);
					
					if(user.hasRole(Constants.USER_ADMIN)){
						forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
					} else if(user.hasRole(Constants.USER_THERAPIST)){
						forwardTo = "/WEB-INF/jsp/therapist-tools/therapist-main-menu.jsp";
					}if(user.hasRole(Constants.USER_CLIENT)){
						forwardTo = "/WEB-INF/jsp/client-tools/client-main-menu.jsp";
					}
					
				} else {
				    throw new DatabaseException(ErrorMessages.INVALID_USERNAME_OR_PASSWORD);
				}
			} catch (DatabaseException e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", e.getMessage());
				//response.sendRedirect("index.jsp");
			    request.getRequestDispatcher("/index.jsp").forward(request, response);
			}
	        
	        request.getRequestDispatcher(forwardTo).forward(request, response);

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
