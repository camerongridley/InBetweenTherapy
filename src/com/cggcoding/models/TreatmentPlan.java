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
	private int currentStageIndex;
	private int activeViewStageIndex;
	private boolean inProgress;
	
	public TreatmentPlan(int treatmentPlanID, String name, String description, int txIssueID){
		this.treatmentPlanID = treatmentPlanID;
		this.name = name;
		this.description = description;
		this.txIssueID = txIssueID;
		this.stages = new ArrayList<>();
		this.currentStageIndex = 0;
		this.activeViewStageIndex = 0;
	}

	public TreatmentPlan(String name, String description, int txIssueID){
		this.name = name;
		this.description = description;
		this.txIssueID = txIssueID;
		this.stages = new ArrayList<>();
		this.currentStageIndex = 0;
		this.activeViewStageIndex = 0;
	}

	//TODO update with proper logic once app is connected to database
	//- set these variables based on the stage that is in progress
	//- done either dynamically by looping through all the plan's stages and checking inProgress status or by saving currentStage in database
	public void initialize(){
		//currentStageIndex = stages.get(0).getStageID();
		//activeViewStageIndex = currentStageIndex;
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
		stages.add(newStage.getIndex(), newStage);
	}
	
	public void updateStages(){
		//
	}
	
	public Stage getStage(int stageID){
		return stages.get(stageID);
	}

	public int getCurrentStageIndex() {
		return currentStageIndex;
	}

	public void setCurrentStageIndex(int currentStageIndex) {
		this.currentStageIndex = currentStageIndex;
	}

	public int getActiveViewStageIndex() {	return activeViewStageIndex; }

	public void setActiveViewStageIndex(int activeViewStageIndex) {
		this.activeViewStageIndex = activeViewStageIndex;
	}

	public Stage getCurrentStage(){
		return stages.get(currentStageIndex);
	}

	public Stage getActiveViewStage() {
		return stages.get(activeViewStageIndex);
	}
	
	public int getNumberOfStages(){
		return stages.size();
	}
	
	public Stage nextStage(){

		if(activeViewStageIndex == currentStageIndex){
			if(currentStageIndex < getNumberOfStages()-1){
				currentStageIndex++;
				activeViewStageIndex = currentStageIndex;
			}
		}
		
		return stages.get(activeViewStageIndex);
	}

	public int getStageOrder(int stageID){
		int stageOrder = 0;
		for(Stage stage : stages){
			if(stage.getStageID() == stageID){
				stageOrder = stage.getIndex();
				return stageOrder;
			}
		}
		return stageOrder;
	}

	public int getCurrentStageOrder(){
		return getStageOrder(currentStageIndex);
	}

	public int getActiveViewStageOrder(){
		return getStageOrder(activeViewStageIndex);
	}

}
