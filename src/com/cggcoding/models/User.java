package com.cggcoding.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public abstract class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		//TODO implement
		return true;
	}
	
	public boolean isAuthorizedForStage(int stageID){
		//TODO implement
		return true;
	}
	
	public boolean isAuthorizedForTask(int taskID){
		//TODO implement
		return true;
	}
	
	//XXX change this to nest copy() - stage.copy(userID)?
	public TreatmentPlan copyTreatmentPlanForClient(int userIDTakingNewPlan, int treatmentPlanIDBeingCopied, boolean isTemplate) throws ValidationException, DatabaseException{
    	TreatmentPlan planToCopy = TreatmentPlan.load(treatmentPlanIDBeingCopied);
    	
    	if(planToCopy.getStages().size()==0){
			throw new ValidationException(ErrorMessages.STAGES_IS_EMPTY);
		}
    	
    	planToCopy.setTemplate(isTemplate);
    	
    	planToCopy.setUserID(userIDTakingNewPlan);
    	//loop through and change all the userIDs to the userID supplied by the method argument
    	for(Stage stage : planToCopy.getStages()){
    		stage.setUserID(userIDTakingNewPlan);
    		for(Task task : stage.getTasks()){
    			task.setUserID(userIDTakingNewPlan);
    		}
    	}
    	
    	//save the plan - which is responsible for updating treatmentPlanID in all the child objects
    	return planToCopy.create();
    	
    }
	
	@Override
	public String toString(){
		return "User id:" + email + ", User email: " + email;
	}

	
}
