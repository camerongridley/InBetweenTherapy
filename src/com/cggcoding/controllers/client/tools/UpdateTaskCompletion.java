package com.cggcoding.controllers.client.tools;

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

import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.tasktypes.CognitiveTask;
import com.cggcoding.models.tasktypes.GenericTask;
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
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User)request.getSession().getAttribute("user");

		int treatmentPlanID = Integer.parseInt(request.getParameter("treatmentPlanID"));

		TreatmentPlan treatmentPlan = user.getTreatmentPlan(treatmentPlanID);
		Stage activeStage = treatmentPlan.getActiveViewStage();

		//get checked values from the request and convert to List<Integer>
		List<Integer> checkedTaskIDs = convertStringArrayToInt(request.getParameterValues("taskChkBx[]"));

		//get all Task ids from hidden field so we can get at unchecked values
		List<Integer> allTaskIDs = convertStringArrayToInt(request.getParameterValues("allTaskIDs"));

		//build maps containing new data to pass back to service layer for updating
		Map<Integer, Task> tasksToBeUpdated = buildNewInfoOnlyTaskMap(user, checkedTaskIDs, allTaskIDs, request);

		//call to service layer to save and process the new task data and return an updated Stage
		Stage updatedStage = activeStage.updateTaskList(tasksToBeUpdated);

		//Check to see if the stage is now completed based on what was updated. If so,prompt user as desired and load next stage
		if(updatedStage.isCompleted()){
			updatedStage = treatmentPlan.nextStage();
		}
		
		request.setAttribute("treatmentPlan", treatmentPlan);
		request.setAttribute("currentStage", updatedStage);

		request.getRequestDispatcher("/jsp/task-review.jsp").forward(request, response);
		
	}

	/*Iterates through all tasks in the stage and updates modifiable fields with data retreived from the request
	* Marks these data holder tasks as completed if checked and incomplete if not in the list of checked tasks.
	* Then the service layer can use this temporary task's isCompleted to determine determine logic for updating completion and progress states in the persistant task.
	* */
	private Map<Integer, Task> buildNewInfoOnlyTaskMap(User user, List<Integer> checkedTaskIDs, List<Integer> allTasksIDs, HttpServletRequest request){

		Map<Integer, Task> newInfoTaskMap = new HashMap<>();

		//loop through all tasks in stage
		//first have to cast to correct task type to know what fields can be updated (some fields like description, id, name, etc.
		//will never change so don't need to bother with them here)
		//then wire up all the data with the corresponding fields
		for(int currentTaskID : allTasksIDs){
			Task updatedTask = null;
			String taskTypeName = request.getParameter("taskTypeName"+currentTaskID);

			//TODO Do I use a static factory method here or just stick with contructors?
			switch (taskTypeName) {
				case "GenericTask":
					System.out.println("Updating Generic Task.");
					GenericTask genTask = GenericTask.getInstanceByID(currentTaskID, user.getUserID());

					updatedTask =  genTask;
					break;
				case "CognitiveTask":
					System.out.println("Updating Cognitive Task");
					CognitiveTask cogTask = new CognitiveTask(currentTaskID, user.getUserID());
					String autoThought = (String)request.getParameter("automaticThought" + cogTask.getTaskID());
					cogTask.setAutomaticThought(autoThought);
					String altThought = (String) request.getParameter("alternativeThought" + cogTask.getTaskID());
					cogTask.setAlternativeThought(altThought);

					updatedTask = cogTask;
					break;
				case "RelaxationTask":
					System.out.println("Updating Relaxation Task.");
					RelaxationTask relaxTask = new RelaxationTask(currentTaskID, user.getUserID());

					updatedTask =  relaxTask;
					break;
				case "PsychEdTask":
					System.out.println("Updating PsychEdTask");
					PsychEdTask psychEdTask = new PsychEdTask(currentTaskID, user.getUserID());

					updatedTask = psychEdTask;
					break;
			}

			//if a taskID is in the checkedTaskIDs list, then mark completed in the data holder task - all others are therefore unchecked and are marked incomplete
			if(checkedTaskIDs.contains(updatedTask.getTaskID())){
				updatedTask.markComplete();
			} else {
				updatedTask.markIncomplete();
			}

			newInfoTaskMap.put(updatedTask.getTaskID(), updatedTask);
		}

		return newInfoTaskMap;
	}

	private List<Integer> convertStringArrayToInt(String[] taskIDsStrings){
		List<Integer> taskIDsConvertedToInts = new ArrayList<>();
		if(taskIDsStrings != null){
			for(int i = 0; i < taskIDsStrings.length; i++){
				try{
					taskIDsConvertedToInts.add(Integer.parseInt(taskIDsStrings[i]));
				} catch (NumberFormatException ex){
					//TODO handle this exception differently?
					System.out.println("Illegal value for a task checkbox.  Detected a non-integer value.");
					ex.printStackTrace();
				}
			}
		}

		return taskIDsConvertedToInts;
	}

}
