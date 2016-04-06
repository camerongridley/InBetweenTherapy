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
import com.cggcoding.models.Keyword;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.User;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Servlet implementation class ListManagement
 */
@WebServlet("/secure/ListManagement")
public class ListManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListManagement() {
        super();
        // TODO Auto-generated constructor stub
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
		
		forwardTo = Constants.URL_LIST_MANAGEMENT;
		
		try {
			request.setAttribute("coreTaskKeyords", Keyword.loadCoreList());
			request.setAttribute("coreTreatmentIssues", TreatmentIssue.getCoreTreatmentIssues());
		} catch (ValidationException | DatabaseException e) {
			request.setAttribute("errorMessage", ErrorMessages.GENERAL_DB_ERROR);//TODO revisit what message is set in request. 
			e.printStackTrace();
		}
				
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
