package com.cggcoding.models;

import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class UserClient extends User{
	private int activeTreatmentPlanId;
	
	DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	public UserClient(int userID, String email){
		super(userID, email);
		activeTreatmentPlanId = -1;
	}

	
	public void setActiveTreatmentPlanId(int treatmentPlanID){
		this.activeTreatmentPlanId = treatmentPlanID;
	}
	
	public int getActiveTreatmentPlanId(){
		return activeTreatmentPlanId;
	}
	
	public TreatmentPlan getActiveTreatmentPlan(){
		return super.getTreatmentPlan(activeTreatmentPlanId);
	}

	public List<TreatmentPlan> getAssignedTreatmentPlans() throws DatabaseException, ValidationException{
		boolean inProgress = false;
		boolean isCompleted = false;
		
		return databaseActionHandler.userGetClientTreatmentPlans(getUserID(), inProgress, isCompleted);
	}
	
	public List<TreatmentPlan> getInProgressTreatmentPlans() throws DatabaseException, ValidationException{
		boolean inProgress = true;
		boolean isCompleted = false;
		
		return databaseActionHandler.userGetClientTreatmentPlans(getUserID(), inProgress, isCompleted);
	}
	
	public List<TreatmentPlan> getCompletedTreatmentPlans() throws DatabaseException, ValidationException{
		boolean inProgress = false;
		boolean isCompleted = true;
		
		return databaseActionHandler.userGetClientTreatmentPlans(getUserID(), inProgress, isCompleted);
	}
}
