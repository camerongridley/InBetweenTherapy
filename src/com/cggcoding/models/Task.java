package com.cggcoding.models;

import java.time.LocalDate;


public abstract class Task implements Completable, Updateable{
	private int taskID;
	private String name;
	private String description;
	private boolean completed;
	private LocalDate dateCompleted;
	private int repetitions;
	private int repetitionsCompleted;

	public Task(int taskID) {
		this.taskID = taskID;
		this.name = "";
		this.description = "";
		this.completed = false;
		this.repetitions = 1;
		this.repetitionsCompleted = 0;
	}

	public Task(int taskID, String name, String description){
		this.taskID = taskID;
		this.name = name;
		this.description = description;
		this.completed = false;
		this.repetitions = 1;
		this.repetitionsCompleted = 0;
	}

	public Task(int taskID, String name, String description, int repetitions){
		this.taskID = taskID;
		this.name = name;
		this.description = description;
		this.completed = false;
		this.repetitions = repetitions;
		this.repetitionsCompleted = 0;
	}
	
	public int getTaskID(){
		return taskID;
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

	public int getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}

	public int getRepetitionsCompleted() {
		return repetitionsCompleted;
	}

	public void completedRepetition(){
		repetitionsCompleted++;
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
		return (int)(((double)repetitionsCompleted/(double)repetitions) * 100);
	}

	//This is called each time one or more repetitions of a task is performed.  A task is not complete until all the repetitions are performed
	public void performed(int timesPerformed){
		repetitions += timesPerformed;
	}

	//This is for tasks with multiple repetitions. Returns true if one but not all of the repetitions have been performed.
	public boolean inProgress(){
		if(repetitionsCompleted > 0 && repetitionsCompleted < repetitions){
			return true;
		} else {
			return false;
		}
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
