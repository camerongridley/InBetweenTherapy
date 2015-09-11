package com.cggcoding.models;

import java.time.LocalDate;


public abstract class Task implements Completable, Updateable{
	private int taskID;
	private int stageID;
	private int userID;
	private int parentTaskID;//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
	private String name;
	private String instructions;
	private boolean completed;
	private LocalDate dateCompleted;
	private int index;
	private boolean isTemplate;

	//TODO if going to use TaskFactory then make all of these and subclass constructors private!
	public Task(int taskID) {
		this.taskID = taskID;
		this.parentTaskID = 0;
		this.name = "";
		this.instructions = "";
		this.completed = false;
	}

	public Task(int taskID, int userID, String name, String instructions){
		this.taskID = taskID;
		this.userID = userID;
		this.parentTaskID = 0;
		this.name = name;
		this.instructions = instructions;
		this.completed = false;
	}

	//constructor if task is going to be a subtask
	public Task(int taskID, int userID, int parentTaskID, String name, String instructions){
		this.taskID = taskID;
		this.userID = userID;
		this.parentTaskID = parentTaskID;
		this.name = name;
		this.instructions = instructions;
		this.completed = false;
	}


	public int getTaskID(){
		return taskID;
	}

	public void setTaskID(int taskID){
		this.taskID = taskID;
	}
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getParentTaskID() {
		return parentTaskID;
	}

	public void setParentTaskID(int parentTaskID) {
		this.parentTaskID = parentTaskID;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return instructions;
	}

	public void setDateCompleted(LocalDate date){
		dateCompleted = date;
	}
	
	public LocalDate getDateCompleted(){
		return dateCompleted;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isTemplate() {
		return isTemplate;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public void setCompleted(boolean status){
		this.completed = status;
	}

	public boolean isParentTask(){
		return parentTaskID == 0;
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
