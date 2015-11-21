package com.cggcoding.models;

import java.io.Serializable;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class StageGoal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int stageGoalID;
	private int stageID;
	private String description;
	private int associatedTaskID;
	
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	private StageGoal(int stageID, String description){
		this.stageGoalID = 0;
		this.description = description;
		this.stageID = stageID;
		this.associatedTaskID = 0;
	}
	
	private StageGoal(int stageGoalID, int stageID, String description) {
		this.stageGoalID = stageGoalID;
		this.stageID = stageID;
		this.description = description;
		this.associatedTaskID = 0;
	}
	
	private StageGoal(int stageGoalID, int stageID, String goal, int associatedTaskID) {
		this.stageGoalID = stageGoalID;
		this.stageID = stageID;
		this.description = goal;
		this.associatedTaskID = associatedTaskID;
	}
	
	public static StageGoal getInstance(int stageGoalID, int stageID, String description){
		return new StageGoal(stageGoalID, stageID, description);
	}
	
	public static StageGoal getInstanceWithoutID(int stageID, String description){
		return new StageGoal(stageID, description);
	}
	
	public static StageGoal saveNewInDatabase(int stageID, String description) throws DatabaseException, ValidationException{
		return databaseActionHandler.stageGoalValidateAndCreate(new StageGoal(stageID, description));
	}
	
	public int getStageGoalID() {
		return stageGoalID;
	}

	public void setStageGoalID(int stageGoalID) {
		this.stageGoalID = stageGoalID;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String goal) {
		this.description = goal;
	}

	public int getAssociatedTaskID() {
		return associatedTaskID;
	}

	public void setAssociatedTaskID(int associatedTaskID) {
		this.associatedTaskID = associatedTaskID;
	}

	public StageGoal copy(){
		return new StageGoal(0, this.stageID, this.getDescription(), this.associatedTaskID);
	}


}
