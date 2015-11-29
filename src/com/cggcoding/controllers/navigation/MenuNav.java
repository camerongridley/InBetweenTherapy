package com.cggcoding.controllers.navigation;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.models.User;

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
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		if(user!=null){
			switch(user.getRole()){
			case "admin":
				forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
				break;
			case "therapist":
				forwardTo = "/WEB-INF/jsp/therapist-tools/therapist-main-menu.jsp";
				break;
			case "client":
				forwardTo = "/WEB-INF/jsp/client-tools/client-main-menu.jsp";
				break;
			default:
				
				break;
			}
		}
		
		
		request.getRequestDispatcher(forwardTo).forward(request, response);

    }
}
