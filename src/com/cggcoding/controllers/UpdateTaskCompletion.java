package com.cggcoding.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.tasktypes.CognitiveTask;

/**
 * Servlet implementation class UpdateTaskCompletion
 */
@WebServlet("/UpdateTaskCompletion")
public class UpdateTaskCompletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTaskCompletion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		TreatmentIssue txIssue = (TreatmentIssue)request.getSession().getAttribute("txIssue");
		
		Stage currentStage = updateTaskCompletionState(request);
		
		if(currentStage.isCompleted()){
			currentStage = txIssue.nextStage();
		}
		
		session.setAttribute("currentStage", currentStage);

		request.getRequestDispatcher("taskReview.jsp").forward(request, response);
		
	}
	
	private Stage updateTaskCompletionState(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		//get all of the checkbox values
		Stage currentStage = (Stage)session.getAttribute("currentStage");
		String[] completedTasksString = request.getParameterValues("taskChkBx[]");

		List<Task> allStageTasks = (ArrayList)currentStage.getTasks();

		List<Integer> completedTasksInt = new ArrayList<>();

		//since I can't get values for unchecked checkboxes I convert the checkbox params array to a List so I can use the contains method to be able to update checks and unchecks
		if(completedTasksString != null){
			for(int i = 0; i < completedTasksString.length; i++){
				try{
					completedTasksInt.add(Integer.parseInt(completedTasksString[i]));
				} catch (NumberFormatException ex){
					System.out.println("Illegal value for a task checkbox.  Detected a non-integer value.");
					ex.printStackTrace();
				}
			}
		}

		//THIS IS BUSINESS LOGIC!!!!  Move it
		//now iterate through all stage tasks and if has matching id to completedTasksInt List then mark complete, else mark incomplete
		for(Task currentTask : allStageTasks){
			int currentTaskID = currentTask.getTaskID();

			if(completedTasksInt.contains(currentTaskID)){
				updateTaskEntry(request, currentTask);
				currentTask.markComplete();
			} else {
				currentTask.markIncomplete();
			}

		}

		//now update progress for the current stage to determine if it is completed
		currentStage.updateProgress();
		
		return currentStage;
	}

	//Wire up the objects and then pass them to the service layer
	private void updateTaskEntry(HttpServletRequest request, Task task){
		switch (task.getTaskTypeName()) {
			case "CognitiveTask":
				System.out.println("Updating Cognitive Task");
				CognitiveTask cogTask = (CognitiveTask)task;
				String autoThought = (String)request.getParameter("automaticThought" + cogTask.getTaskID());
				cogTask.setAutomaticThought(autoThought);
				String altThought = (String) request.getParameter("alternativeThought" + cogTask.getTaskID());
				cogTask.setAlternativeThought(altThought);

				//call to database to update
				//success = bdHelper.update();
				break;
			case "RelaxationTask":
				System.out.println("Updating Relaxation Task.");
				break;
			case "PsychEdTask":
				System.out.println("Updating PsychEdTask");
				break;
		}
	}

}
