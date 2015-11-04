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

	public List<TreatmentPlan> getAssignedTreatmentPlanIDs() throws DatabaseException, ValidationException{
		return databaseActionHandler.userGetAssignedClientTreatmentPlans(getUserID());
	}

}
