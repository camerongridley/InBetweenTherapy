package com.cggcoding.models;


public class UserClient extends User{
	private int activeTreatmentPlanId;
	
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


}
