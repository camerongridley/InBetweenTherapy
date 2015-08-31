package com.cggcoding.models;

import java.time.LocalDate;


public abstract class Task implements Completable, Updateable{
	private int taskID;
	private int taskSetID;
	private int stageID;
	private String name;
	private String description;
	private boolean completed;
	private LocalDate dateCompleted;
	private int order;

	public Task(int taskID) {
		this.taskID = taskID;
		this.taskSetID = -1;
		this.name = "";
		this.description = "";
		this.completed = false;
	}

	public Task(String name, String description){
		this.taskID = -1;
		this.taskSetID = -1;
		this.name = name;
		this.description = description;
		this.completed = false;
	}

	public Task(int taskID, String name, String description){
		this.taskID = taskID;
		this.taskSetID = -1;
		this.name = name;
		this.description = description;
		this.completed = false;
	}

	//constructor if task is going to be in a set
	public Task(int taskID, int taskSetID, String name, String description){
		this.taskID = taskID;
		this.taskSetID = taskSetID;
		this.name = name;
		this.description = description;
		this.completed = false;
	}


	public int getTaskID(){
		return taskID;
	}

	public void setTaskID(int taskID){
		this.taskID = taskID;
	}

	public int getTaskSetID(){
		return taskSetID;
	}

	public void setTaskSetID(int taskSetID){
		this.taskSetID = taskSetID;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setDateCompleted(LocalDate date){
		dateCompleted = date;
	}
	
	public LocalDate getDateCompleted(){
		return dateCompleted;
	}

	public void setCompleted(boolean status){
		this.completed = status;
	}


	//a task is completed when all of the repetitions are performed
	@Override
	public boolean isCompleted() {
		return completed;
	}
	
	@Override
	public void markComplete() {
		completed = true;
		dateCompleted = LocalDate.now();
	}

	@Override
	public void markIncomplete() {
		completed = false;
	}

	@Override
	public int getPercentComplete() {
		if (isCompleted()) {
			return 100;
		} else {
			return 0;
		}
		//return (int)(((double)repetitionsCompleted/(double)repetitions) * 100);
	}


	public String getTaskTypeName(){
		return this.getClass().getSimpleName();
	}

	//in place so can be overridden by concrete classes
	@Override
	public boolean updateData(Task taskWithNewData) {
		return false;
	}
}
