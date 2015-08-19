package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class UserClient extends User{
	private int activeTreatmentPlanId;
	
	public UserClient(int userID, String email){
		super(userID, email);
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
