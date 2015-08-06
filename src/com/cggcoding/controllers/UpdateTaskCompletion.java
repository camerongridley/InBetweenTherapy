package com.cggcoding.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cggcoding.models.tasktypes.PsychEdTask;
import com.cggcoding.models.tasktypes.RelaxationTask;

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

		List<Integer> completedTaskIDsConvertedToInt = new ArrayList<>();

		/* since I can't get values for unchecked checkboxes from the request I have to do a workaround to properly update if the user unchecks a task
		First, get checked values from the request and convert to a List<Integer>.*/
		if(completedTasksString != null){
			for(int i = 0; i < completedTasksString.length; i++){
				try{
					completedTaskIDsConvertedToInt.add(Integer.parseInt(completedTasksString[i]));
				} catch (NumberFormatException ex){
					System.out.println("Illegal value for a task checkbox.  Detected a non-integer value.");
					ex.printStackTrace();
				}
			}
		}


		/*Now iterate through all the tasks, building a HashMap to pass back to the service layer for updating there.
		 If same id is in the list of updated tasks, update accordingly with new data.  All other tasks get marked as incomplete.*/
		Map<Integer, Task> newInfoTaskMap = new HashMap<>();
		for(Task currentTask : allStageTasks){
			Task updatedTask = updateTaskEntry(request, currentTask, completedTaskIDsConvertedToInt);



			newInfoTaskMap.put(updatedTask.getTaskID(), updatedTask);
		}

		Stage updatedStage = currentStage.updateTaskList(newInfoTaskMap, completedTaskIDsConvertedToInt);
		
		return updatedStage;
	}

	//Wire up the objects and then pass them to the service layer
	private Task updateTaskEntry(HttpServletRequest request, Task task, List idsOfCompletedTasks){
		Task updatedTask = null;
		switch (task.getTaskTypeName()) {
			case "CognitiveTask":
				System.out.println("Updating Cognitive Task");
				CognitiveTask cogTask = new CognitiveTask(task.getTaskID());
				String autoThought = (String)request.getParameter("automaticThought" + cogTask.getTaskID());
				cogTask.setAutomaticThought(autoThought);
				String altThought = (String) request.getParameter("alternativeThought" + cogTask.getTaskID());
				cogTask.setAlternativeThought(altThought);

				determineIfTaskCompleted(cogTask, idsOfCompletedTasks);

				updatedTask = cogTask;
				break;
			case "RelaxationTask":
				System.out.println("Updating Relaxation Task.");
				RelaxationTask relaxTask = new RelaxationTask(task.getTaskID());
				determineIfTaskCompleted(relaxTask, idsOfCompletedTasks);

				updatedTask =  relaxTask;
				break;
			case "PsychEdTask":
				System.out.println("Updating PsychEdTask");
				PsychEdTask psychEdTask = new PsychEdTask(task.getTaskID());
				determineIfTaskCompleted(psychEdTask, idsOfCompletedTasks);

				updatedTask = psychEdTask;
				break;
		}

		return updatedTask;
	}

	private Task determineIfTaskCompleted(Task task, List idsOfCompletedTasks){
		if(idsOfCompletedTasks.contains(task.getTaskID())){
			task.markComplete();
		} else {
			task.markIncomplete();
		}

		return task;
	}


}
