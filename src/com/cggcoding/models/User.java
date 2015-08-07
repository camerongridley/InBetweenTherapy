package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int userID;
	private String email;
	private String encryptedPassword;
	private int activeTreatmentPlanId;
	private List<TreatmentPlan> txPlans;
	
	public User (int userID, String email, String password){
		this.userID = userID;
		this.email = email;
		this.encryptedPassword = password;
		this.txPlans = new ArrayList<>();
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
	
	public void setActiveTreatmentPlanId(int stageId){
		this.activeTreatmentPlanId = stageId;
	}
	
	public int getActiveTreatmentPlanId(){
		return activeTreatmentPlanId;
	}
	
	public TreatmentPlan getActiveTreatmentPlan(){
		return txPlans.get(activeTreatmentPlanId);
	}
}
