package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class TreatmentIssue {
	private int txIssueID;
	private String name;
	private String description;
	private List<Stage> stages;
	private int currentStageID;
	private int activeViewStageID;
	private int userID;
	
	public TreatmentIssue(String name, String description, int userID){
		this.txIssueID = 0; //temp value for id
		this.name = name;
		this.description = description;
		this.userID = userID;
		this.stages = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public void setStages(List<Stage> stages){
		this.stages = stages;
	}
	
	public List<Stage> getStages(){
		return stages;
	}
	
	public int getUserId(int userID){
		return userID;
	}
	
	public void addStage(Stage newStage, int sequenceNumber){
		stages.add(sequenceNumber, newStage);
	}
	
	public void updateStages(){
		//
	}
	
	public Stage getStage(int stageID){
		return stages.get(stageID);
	}

	public int getCurrentStageID() {
		return currentStageID;
	}

	public void setCurrentStageID(int currentStageID) {
		this.currentStageID = currentStageID;
	}

	public int activeViewStageID() {	return activeViewStageID; }

	public void activeViewStageID(int activeStageViewID) {	this.activeViewStageID = activeViewStageID;	}

	public Stage getCurrentStage(){
		return stages.get(currentStageID);
	}
	
	public int getNumberOfStages(){
		return stages.size();
	}
	
	public Stage nextStage(){
		Stage nextStage = stages.get(currentStageID);
		
		if(currentStageID < getNumberOfStages()-1){
			currentStageID++;
			nextStage = stages.get(currentStageID);
		}
		
		return nextStage;
	}

}
