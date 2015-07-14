package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class Stage implements Completable {

	private int id;
	private String name;
	private String description;
	private List<Task> tasks;
	private List<Task> extraTasks; //for when user chooses to do more tasks than asked of - won't count toward progress meter but can be saved for review or other analysis (e.g. themes)
	private boolean completed;
	
	public Stage (String name, String description){
		this.id = 0; //temp value for id
		this.name = name;
		this.description = description;
		this.tasks = new ArrayList<>();
		this.extraTasks = new ArrayList<>();
		this.completed = false;
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
		return id;
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
	
	public void updateTasks(){
		//when a task's completion state is changed it checks if all tasks are complete and if will lead to stage being complete and any other actions desired at this time
	}
	
	public void updateProgress(){
		//once a task is completed this is called to update the progress meter and associated metrics
	}
	
	//returns a double digit number representing percentage of stage completion
	public int getProgress(){
		return 0;
	}
	
}
