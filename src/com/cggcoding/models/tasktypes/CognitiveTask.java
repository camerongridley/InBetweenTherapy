package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;

public class CognitiveTask extends Task {
	private String automaticThought;
	private String alternativeThought;
	private int preSUDS;
	private int postSUDS;

	public CognitiveTask (int taskID){
		super(taskID);
	}
	public CognitiveTask (int taskID, String name, String description){
		super(taskID, name, description);
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


	
}
