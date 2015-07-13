package com.cggcoding.temp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cggcoding.models.CognitiveTask;
import com.cggcoding.models.Task;

/**
 * Servlet implementation class LoadData
 */
@WebServlet("/LoadData")
public class LoadData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//load a list of of tasks - in place of a db call since the db is not yet implemented
		List<Task> taskList = new ArrayList<Task>();
		
		CognitiveTask thoughtMonitoring = new CognitiveTask("Monitor Negative Thoughts", "Track all the negative thoughts you have.");
		CognitiveTask affirmation = new CognitiveTask("Affirmation", "Remind yourself of an authentic thought or belief.");
		CognitiveTask thoughtDistancing = new CognitiveTask("Thought Distancing", "Take a step back from the thought to just observe it without judgment.");
		
		taskList.add(thoughtMonitoring);
		taskList.add(affirmation);
		taskList.add(thoughtDistancing);
		
		request.setAttribute("taskList", taskList);
		
		request.getRequestDispatcher("taskReview.jsp").forward(request, response);
	}

}
