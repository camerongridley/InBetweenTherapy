package com.cggcoding.controllers.navigation;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.Constants;

/**
 * Servlet implementation class MenuNav
 */
@WebServlet(description = "Controls user-specific navigation menu item population.", urlPatterns = { "/secure/MenuNav" })
public class MenuNav extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MenuNav() {
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
		
		
		if(user!=null){
			switch(user.getRole()){
			case "admin":
				forwardTo = Constants.URL_ADMIN_MAIN_MENU;
				break;
			case "therapist":
				UserTherapist therapistUser = (UserTherapist)user;
				try {
					Map<Integer, UserClient> clientMap = therapistUser.loadClients();
					request.setAttribute("clientMap", clientMap);
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
				break;
			case "client":
				forwardTo = Constants.URL_CLIENT_MAIN_MENU;
				break;
			default:
				
				break;
			}
		}
		
		
		request.getRequestDispatcher(forwardTo).forward(request, response);

    }
}
