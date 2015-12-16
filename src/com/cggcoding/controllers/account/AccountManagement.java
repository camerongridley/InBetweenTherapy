package com.cggcoding.controllers.account;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.models.User;

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
        // TODO Auto-generated constructor stub
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
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		String destination = request.getParameter("destination");
		
		
		if(user!=null){
			forwardTo = "/WEB-INF/jsp/account-management.jsp";
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
