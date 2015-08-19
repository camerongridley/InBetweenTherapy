package com.cggcoding.controllers.client.tools;

import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;

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
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User)request.getSession().getAttribute("user");

        int treatmentPlanID = Integer.parseInt(request.getParameter("treatmentPlanID"));

        TreatmentPlan treatmentPlan = user.getTreatmentPlan(treatmentPlanID);

        int newViewID = Integer.parseInt(request.getParameter("stageID"));
        treatmentPlan.setActiveViewStageID(newViewID);

        request.setAttribute("treatmentPlan", treatmentPlan);

        request.getRequestDispatcher("taskReview.jsp").forward(request,response);

	}

}
