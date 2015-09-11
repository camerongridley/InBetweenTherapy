package com.cggcoding.models;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class StageGoal implements DatabaseModel{
	private int stageGoalID;
	private int stageID;
	private String goal;
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	public StageGoal(int stageGoalID, int stageID, String goal) {
		this.stageGoalID = stageGoalID;
		this.stageID = stageID;
		this.goal = goal;
	}
	
	public static StageGoal getInstanceFromDatabase(int stageGoalID){
		return null;
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

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	@Override
	public boolean validateForDatabase() throws ValidationException, DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveNewInDatabase() throws ValidationException, DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadDataFromDatabase() throws ValidationException, DatabaseException {
		StageGoal goal = getInstanceFromDatabase(this.stageGoalID);
		if(goal != null){
			this.stageGoalID = goal.stageGoalID;
			this.stageID = goal.stageID;
			this.goal = goal.goal;
			return true;
		}
		return false;
	}

	@Override
	public boolean updateInDatabase() throws ValidationException, DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteFromDatabase() throws ValidationException, DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

}
