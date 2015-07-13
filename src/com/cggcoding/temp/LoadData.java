package com.cggcoding.temp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.tasktypes.CognitiveTask;
import com.cggcoding.models.tasktypes.PsychEdTask;

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
		
		TreatmentIssue issue = new TreatmentIssue("ED", "Erectile dysfunction");
		
		//create stages
		Stage psychEd = new Stage("PsychoEducation", "Important concepts to learn about the problem you are experiencing.  Understanding some of these core concept can help you feel confident about the treatment strategies implemented here.");
		Stage relax = new Stage("Relaxation", "Learning to relax your body on command is a fundamental building block of overcoming any sexual difficulty");
		Stage cognitive = new Stage("Cognitive", "Here we help you monitor and respond differently to unhelpful thinking.");
		
		//create and load tasks for each stage
		psychEd.addTask(new PsychEdTask("Coping with ED", "Chapter 3 - Developing Realistic Expectations"));
		
		
		//load stages into issue
		issue.addStage(psychEd, 0);
		issue.addStage(relax, 1);
		issue.addStage(cognitive, 2);
		
		//create tasks for 
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
