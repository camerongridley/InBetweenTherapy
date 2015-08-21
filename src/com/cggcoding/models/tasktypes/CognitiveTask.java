package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;
import com.cggcoding.models.Updateable;

public class CognitiveTask extends Task implements Updateable{
	private String automaticThought;
	private String alternativeThought;
	private int preSUDS;
	private int postSUDS;

	public CognitiveTask (int taskID){
		super(taskID);
	}
	public CognitiveTask (int taskID, String name, String description, int repetitions){
		super(taskID, name, description, repetitions);
	}
	
	public String getAutomaticThought() {
		return automaticThought;
	}
	public void setAutomaticThought(String automaticThought) {
		this.automaticThought = automaticThought;
	}
	public String getAlternativeThought() {
		return alternativeThought;
	}
	public void setAlternativeThought(String alternativeThought) {
		this.alternativeThought = alternativeThought;
	}
	public int getPreSUDS() {
		return preSUDS;
	}
	public void setPreSUDS(int preSUDS) {
		this.preSUDS = preSUDS;
	}
	public int getPostSUDS() {
		return postSUDS;
	}
	public void setPostSUDS(int postSUDS) {
		this.postSUDS = postSUDS;
	}


	@Override
	public boolean updateData(Task taskWithNewData) {
		//update universal properties
		this.setCompleted(taskWithNewData.isCompleted());
		this.setDateCompleted(taskWithNewData.getDateCompleted());


		//CognitiveTask cogTask = (CognitiveTask)persistentTask;
		CognitiveTask newData = (CognitiveTask)taskWithNewData;

		this.setAlternativeThought(newData.getAlternativeThought());
		this.setAutomaticThought(newData.getAutomaticThought());

		//updateCogTask DB call goes here

		return true;//TODO returns true if DB update was success
	}

}
