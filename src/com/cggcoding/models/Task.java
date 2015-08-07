package com.cggcoding.models;

import java.time.LocalDate;


public abstract class Task implements Completable{
	private int taskID;
	private String name;
	private String description;
	private boolean completed;
	private LocalDate dateCompleted;

	public Task(int taskID) {
		this.taskID = taskID;
	}

	public Task(int taskID, String name, String description){
		this.taskID = taskID;
		this.name = name;
		this.description = description;
		this.completed = false;
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
	
	public String getTaskTypeName(){
		return this.getClass().getSimpleName();
	}

	
	
}
