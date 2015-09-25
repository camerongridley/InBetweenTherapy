package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public abstract class User {
	private int userID;
	private String email;
	private List<String> roles;
	String role;
	private List<TreatmentPlan> treatmentPlanList;
	private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();
	
	public User (int userID, String email){
		this.userID = userID;
		this.email = email;
		roles = new ArrayList<>();
		this.treatmentPlanList = new ArrayList<>();
	}

	public int getUserID(){
		return userID;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getRoles(){
		return roles;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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
	

	@Override
	public String toString(){
		return "User id:" + email + ", User email: " + email;
	}

	
}
