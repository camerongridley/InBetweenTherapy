package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class TreatmentPlan {
	private int txPlanID;
	private String name;
	private String description;
	private List<Stage> stages;
	private int currentStageID;
	private int activeViewStageID;
	private int userID;
	private boolean inProgress;
	
	public TreatmentPlan(String name, String description, int userID){
		this.txPlanID = 0; //temp value for id
		this.name = name;
		this.description = description;
		this.userID = userID;
		this.stages = new ArrayList<>();
		this.currentStageID = 0;
		this.activeViewStageID = 0;
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

	public int getActiveViewStageID() {	return activeViewStageID; }

	public void setActiveViewStageID(int activeViewStageID) {
		this.activeViewStageID = activeViewStageID;
	}

	public Stage getCurrentStage(){
		return stages.get(currentStageID);
	}

	public Stage getActiveViewStage() {
		return stages.get(activeViewStageID);
	}
	
	public int getNumberOfStages(){
		return stages.size();
	}
	
	public Stage nextStage(){

		if(activeViewStageID == currentStageID){
			if(currentStageID < getNumberOfStages()-1){
				currentStageID++;
				activeViewStageID = currentStageID;
			}
		}
		
		return stages.get(activeViewStageID);
	}

}
