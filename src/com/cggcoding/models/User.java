package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private int userID;
	private String email;
	private List<String> roles;
	private List<TreatmentPlan> txPlans;
	
	public User (int userID, String email){
		this.userID = userID;
		this.email = email;
		roles = new ArrayList<>();
		this.txPlans = new ArrayList<>();
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

	public void setTreatmentPlanList(List<TreatmentPlan> txPlans){
		this.txPlans = txPlans;
	}

	public List<TreatmentPlan> getTreatmentPlanList(){
		return txPlans;
	}

	public void addTreatmentPlan(TreatmentPlan txPlan){
		this.txPlans.add(txPlan);
	}

	public TreatmentPlan getTreatmentPlan(int txPlanID){
		return txPlans.get(txPlanID);
	}
}
