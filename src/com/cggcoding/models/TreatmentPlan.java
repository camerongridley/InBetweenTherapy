package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class TreatmentPlan {
	private int treatmentPlanID;
	private int userClientID; //TODO -  collapse these into one userID since there are other ways to track relationship between client and therapist?
	private int userTherapistID;
	private int txIssueID;
	private String name;
	private String description;
	private List<Stage> stages;
	private int currentStageID;
	private int activeViewStageID;
	private boolean inProgress;
	
	public TreatmentPlan(int treatmentPlanID, String name, String description, int txIssueID){
		this.treatmentPlanID = treatmentPlanID;
		this.name = name;
		this.description = description;
		this.txIssueID = txIssueID;
		this.stages = new ArrayList<>();
		this.currentStageID = 0;
		this.activeViewStageID = 0;
	}

	public TreatmentPlan(String name, String description, int txIssueID){
		this.name = name;
		this.description = description;
		this.txIssueID = txIssueID;
		this.stages = new ArrayList<>();
		this.currentStageID = 0;
		this.activeViewStageID = 0;
	}

	//TODO update with proper logic once app is connected to database - set these variables based on the stage that is in progress
	public void initialize(){
		currentStageID = stages.get(0).getStageID();
		activeViewStageID = currentStageID;
	}

	public int getTreatmentPlanID(){
		return treatmentPlanID;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getTxIssueID() {
		return txIssueID;
	}

	public void setStages(List<Stage> stages){
		this.stages = stages;
	}
	
	public List<Stage> getStages(){
		return stages;
	}
	
	public void addStage(Stage newStage){
		stages.add(newStage.getOrder(), newStage);
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

	public int getStageOrder(int stageID){
		int stageOrder = 0;
		for(Stage stage : stages){
			if(stage.getStageID() == stageID){
				stageOrder = stage.getOrder();
				return stageOrder;
			}
		}
		return stageOrder;
	}

	public int getCurrentStageOrder(){
		return getStageOrder(currentStageID);
	}

	public int getActiveViewStageOrder(){
		return getStageOrder(activeViewStageID);
	}

}
