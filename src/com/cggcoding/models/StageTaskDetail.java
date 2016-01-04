package com.cggcoding.models;

public class StageTaskDetail {

	private int stageID;
	private int taskID;
	private int taskOrder;
	private int repetitions;
	
	
	public StageTaskDetail(int stageID, int taskID, int taskOrder, int repetitions) {
		this.stageID = stageID;
		this.taskID = taskID;
		this.taskOrder = taskOrder;
		this.repetitions = repetitions;
	}


	public int getStageID() {
		return stageID;
	}


	public void setStageID(int stageID) {
		this.stageID = stageID;
	}


	public int getTaskID() {
		return taskID;
	}


	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}


	public int getTaskOrder() {
		return taskOrder;
	}


	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}


	public int getRepetitions() {
		return repetitions;
	}


	public void setRepetitions(int repetitions) {
		this.repetitions = repetitions;
	}
	
	
	

}
