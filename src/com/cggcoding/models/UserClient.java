package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class UserClient extends User{
	private int userID;
	private String email;
	private int activeTreatmentPlanId;
	private List<TreatmentPlan> txPlans;
	
	public UserClient(int userID, String email){
		super(userID, email);
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
	
	public void setActiveTreatmentPlanId(int txPlanID){
		this.activeTreatmentPlanId = txPlanID;
	}
	
	public int getActiveTreatmentPlanId(){
		return activeTreatmentPlanId;
	}
	
	public TreatmentPlan getActiveTreatmentPlan(){
		return txPlans.get(activeTreatmentPlanId);
	}
}
