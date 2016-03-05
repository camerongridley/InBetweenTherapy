package com.cggcoding.models;

import java.io.Serializable;
import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class UserClient extends User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int activeTreatmentPlanId;
	
	DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	public UserClient(int userID, String userName, String firstName, String lastName, String email){
		super(userID, userName, firstName, lastName, email);
		this.setRoleID(Constants.CLIENT_ROLE_ID);
		this.addRole(Constants.USER_CLIENT);
		this.setRole(Constants.USER_CLIENT);
		activeTreatmentPlanId = -1;
		setMainMenuURL(Constants.URL_CLIENT_MAIN_MENU);
	}

	
	public void setActiveTreatmentPlanId(int treatmentPlanID){
		this.activeTreatmentPlanId = treatmentPlanID;
	}
	
	public int getActiveTreatmentPlanId(){
		return activeTreatmentPlanId;
	}
	
	public TreatmentPlan getActiveTreatmentPlan() throws ValidationException{
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


	@Override
	public boolean isAuthorizedForTreatmentPlan(int treatmentPlanID) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAuthorizedForStage(int stageID) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAuthorizedForTask(int taskID) {
		// TODO Auto-generated method stub
		return true;
	}
}
