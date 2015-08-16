package com.cggcoding.models;

import com.cggcoding.models.tasktypes.CognitiveTask;
import com.cggcoding.models.tasktypes.PsychEdTask;
import com.cggcoding.models.tasktypes.RelaxationTask;

import java.util.*;

public class Stage implements Completable {

	private int stageID;
	private int treatmentPlanID;
	private String name;
	private String description;
	private List<Task> tasks;
	private List<Task> extraTasks; //for when user chooses to do more tasks than asked of - won't count toward progress meter but can be saved for review or other analysis (e.g. themes)
	private boolean completed;
	private int numberOfTasksCompleted;
	private double percentComplete;
	private List<String> goals;
	
	public Stage (int stageID, String name, String description){
		this.stageID = stageID;
		this.name = name;
		this.description = description;
		this.tasks = new ArrayList<>();
		this.extraTasks = new ArrayList<>();
		this.completed = false;
		this.numberOfTasksCompleted = 0;
		this.percentComplete = 0;
		this.goals = new ArrayList<>();
	}
	
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> taskList) {
		this.tasks = taskList;
	}

	public List<Task> getExtraTasks() {
		return extraTasks;
	}

	public void setExtraTasks(List<Task> extraTasks) {
		this.extraTasks = extraTasks;
	}

	public int getStageID() {
		return stageID;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void addTask(Task task){//add index specifier?
		tasks.add(task);
	}
	
	public void addExtraTask(Task extraTask){//add index specifier?
		extraTasks.add(extraTask);
	}
	
	@Override
	public boolean isCompleted(){
		return completed;
	}
	
	@Override
	public void markComplete() {
		completed = true;
	}

	@Override
	public void markIncomplete() {
		completed = false;
	}

	public List<String> getGoals() {
		return goals;
	}

	public void setGoals(List<String> goals) {
		this.goals = goals;
	}

	//when a task's completion state is changed it checks if all tasks are complete and if will lead to stage being complete and any other actions desired at this time
	public Stage updateTaskList(Map<Integer, Task> updatedTasksMap, List idsOfCompletedTasks){
		//iterate through task map to update with info from updatedTasks list
		for(Task persistentTask : this.tasks){
			Task taskWithNewInfo = updatedTasksMap.get(persistentTask.getTaskID());
			updateTaskData(persistentTask, taskWithNewInfo);
		}

		updateProgress();
		return this;
	}

	private Task updateTaskData(Task persistentTask, Task taskWithNewInfo){

		//update universal properties
		persistentTask.setCompleted(taskWithNewInfo.isCompleted());
		persistentTask.setDateCompleted(taskWithNewInfo.getDateCompleted());

		//update case-specific properties
		switch (persistentTask.getTaskTypeName()) {
			case "CognitiveTask" :
				CognitiveTask cogTask = (CognitiveTask)persistentTask;
				CognitiveTask newData = (CognitiveTask)taskWithNewInfo;

				cogTask.setAlternativeThought(newData.getAlternativeThought());
				cogTask.setAutomaticThought(newData.getAutomaticThought());

				//updateCogTask DB call goes here

				break;

		}

		return persistentTask;
	}


	
	//once a task is completed this is called to update the progress meter and associated metrics
	public void updateProgress(){
		numberOfTasksCompleted = 0;
		
		for(Task task : tasks){
			if(task.isCompleted()){
				numberOfTasksCompleted++;
			}
		}
		
		percentComplete = ((double)getNumberOfTasksCompleted()/(double)getTotalNumberOfTasks());
		
		if(getPercentComplete()==100){
			this.markComplete();
		}
		
	}
	
	//returns a double digit number representing percentage of stage completion
	public int getPercentComplete(){
		return (int)(percentComplete * 100);
	}

	public int getNumberOfTasksCompleted() { return numberOfTasksCompleted; }

	public int getTotalNumberOfTasks() { return tasks.size(); }


}
