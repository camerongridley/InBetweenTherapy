package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class Stage implements Completable {

	private int stageID;
	private String name;
	private String description;
	private List<Task> tasks;
	private List<Task> extraTasks; //for when user chooses to do more tasks than asked of - won't count toward progress meter but can be saved for review or other analysis (e.g. themes)
	private boolean completed;
	private int numberOfTasksCompleted;
	private double percentComplete;
	
	public Stage (int stageID, String name, String description){
		this.stageID = stageID;
		this.name = name;
		this.description = description;
		this.tasks = new ArrayList<>();
		this.extraTasks = new ArrayList<>();
		this.completed = false;
		this.numberOfTasksCompleted = 0;
		this.percentComplete = 0;
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
	
	//when a task's completion state is changed it checks if all tasks are complete and if will lead to stage being complete and any other actions desired at this time
	public void updateTasks(){

		
	}
	
	//once a task is completed this is called to update the progress meter and associated metrics
	public void updateProgress(){
		numberOfTasksCompleted = 0;
		
		for(Task task : tasks){
			if(task.isCompleted()){
				numberOfTasksCompleted++;
			}
		}
		
		percentComplete = ((double)numberOfTasksCompleted/(double)tasks.size());
		
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
