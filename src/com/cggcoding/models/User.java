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
	private int roleID;
	private List<String> roles;
	String role;
	private List<TreatmentPlan> treatmentPlanList;
	private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();
	
	public User (int userID, String email){
		this.userID = userID;
		this.email = email;
		this.roleID = 0;
		roles = new ArrayList<>();
		this.treatmentPlanList = new ArrayList<>();
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getUserID(){
		return userID;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
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
		TreatmentPlan planFound = null;
		for(TreatmentPlan plan : treatmentPlanList){
			if(plan.getTreatmentPlanID()==treatmentPlanID){
				planFound = plan;
			}
		}
		
		if(planFound == null){
			try {
				throw new Exception("There was an error loading your treatment plan. (User.getTreamentPlan())");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return planFound;
 
	}
	
	public boolean isAuthorizedForTreatmentPlan(int treatmentPlanID){
		return true;
	}
	
	public boolean isAuthorizedForStage(int stageID){
		return true;
	}
	
	public boolean isAuthorizedForTask(int taskID){
		return true;
	}
	
	@Override
	public String toString(){
		return "User id:" + email + ", User email: " + email;
	}

	
}
