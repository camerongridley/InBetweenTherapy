package com.cggcoding.models;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class StageGoal {
	private int stageGoalID;
	private int stageID;
	private String description;
	private int associatedTaskID;
	
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	private StageGoal(int stageGoalID, String description){
		this.stageGoalID = stageGoalID;
		this.description = description;
		this.stageID = 0;
		this.associatedTaskID = 0;
	}
	
	private StageGoal(int stageGoalID, int stageID, String goal) {
		this.stageGoalID = stageGoalID;
		this.stageID = stageID;
		this.description = goal;
		this.associatedTaskID = 0;
	}
	
	private StageGoal(int stageGoalID, int stageID, String goal, int associatedTaskID) {
		this.stageGoalID = stageGoalID;
		this.stageID = stageID;
		this.description = goal;
		this.associatedTaskID = associatedTaskID;
	}
	
	public static StageGoal getInstance(int stageGoalID, int stageID, String goal){
		return new StageGoal(stageGoalID, stageID, goal);
	}
	
	/*public static StageGoal getInstanceFromDatabase(int stageGoalID){
		return null;
	}*/
	
	public static StageGoal saveNewInDatabase(int stageID, String description) throws DatabaseException{
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



}
