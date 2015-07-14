package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class TreatmentIssue {
	private int id;
	private String name;
	private String description;
	private List<Stage> stages;
	
	public TreatmentIssue(String name, String description){
		this.id = 0; //temp value for id
		this.name = name;
		this.description = description;
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
	
	public void addStage(Stage newStage, int sequenceNumber){
		stages.add(sequenceNumber, newStage);
	}
	
	public void updateStages(){
		//
	}

}
