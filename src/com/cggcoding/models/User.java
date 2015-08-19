package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private int userID;
	private String email;
	private List<String> roles;
	private List<TreatmentPlan> treatmentPlanList;
	
	public User (int userID, String email){
		this.userID = userID;
		this.email = email;
		roles = new ArrayList<>();
		this.treatmentPlanList = new ArrayList<>();
	}

	public List<String> getRoles(){
		return roles;
	}

	public boolean hasRole(String roleName){

		for(String name : roles){
			if (name.equals(roleName)){
				return true;
			}
		}
		return false;
	}

	public void addRole(String roleName){
		roles.add(roleName);
	}

	public String getEmail() {
		return email;
	}

	public void setTreatmentPlanList(List<TreatmentPlan> treatmentPlanList){
		this.treatmentPlanList = treatmentPlanList;
	}

	public List<TreatmentPlan> getTreatmentPlanList(){
		return treatmentPlanList;
	}

	public void addTreatmentPlan(TreatmentPlan treatmentPlan){
		this.treatmentPlanList.add(treatmentPlan);
	}

	public TreatmentPlan getTreatmentPlan(int treatmentPlanID){
		return treatmentPlanList.get(treatmentPlanID);
	}
}
