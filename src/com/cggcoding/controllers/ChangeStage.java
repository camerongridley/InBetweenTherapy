package com.cggcoding.controllers;

import com.cggcoding.models.TreatmentIssue;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ChangeStage
 */
@WebServlet("/ChangeStage")
public class ChangeStage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeStage() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

		TreatmentIssue txIssue =  (TreatmentIssue)session.getAttribute("txIssue");
        int newViewID = Integer.parseInt(request.getParameter("stageID"));
        txIssue.setActiveViewStageID(newViewID);

        session.setAttribute("txIssue", txIssue);

        request.getRequestDispatcher("taskReview.jsp").forward(request,response);

	}

}
