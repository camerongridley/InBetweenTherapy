package com.cggcoding.models;

//TODO rename class to StageTaskTemplateDetail or StageTaskTemplateMapping
public class StageTaskDetail {

	private int stageID;
	private int taskID;
	private int taskOrder;
	private int templateRepetitions;
	
	
	public StageTaskDetail(int stageID, int taskID, int taskOrder, int templateRepetitions) {
		this.stageID = stageID;
		this.taskID = taskID;
		this.taskOrder = taskOrder;
		this.templateRepetitions = templateRepetitions;
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


	public int getTemplateRepetitions() {
		return templateRepetitions;
	}


	public void setTemplateRepetitions(int templateRepetitions) {
		this.templateRepetitions = templateRepetitions;
	}
	
	
	

}
