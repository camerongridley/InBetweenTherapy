package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
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
	private static DatabaseActionHandler dao= new MySQLActionHandler();
	
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
	/*public TreatmentPlan copyTreatmentPlanForClient(int userIDTakingNewPlan, int treatmentPlanIDBeingCopied, boolean isTemplate) throws ValidationException, DatabaseException{
    	TreatmentPlan planToCopy = TreatmentPlan.load(treatmentPlanIDBeingCopied);
    	List<Stage> stages = planToCopy.getStages();
    	
    	if(planToCopy.getStages().size()==0){
			throw new ValidationException(ErrorMessages.STAGES_IS_EMPTY);
		}
    	
    	planToCopy.setTemplate(isTemplate);
    	
    	planToCopy.setUserID(userIDTakingNewPlan);

    	Connection cn = null;
        
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	
        	planToCopy = planToCopy.createBasic(cn);//XXX Check that this updates the treatmentPlanID correctly
        	
        	//loop through and copy into the new plan
        	for(Stage stage : stages){
        		//OPTIMIZE the following lines of code are repeated in TreatmentPlan.copyStageIntoTreatmentPlan.  I couldn't figure out a way to use that method since I need to know the state of a TreatmentPlan's isTemplate to determine if repetitions are made, so can't do that from Stage.java
        		stage.setUserID(userIDTakingNewPlan);
        		stage.setTemplate(false);
        		stage.setTreatmentPlanID(planToCopy.getTreatmentPlanID());//set with new treatmentPlanID that was created when .createBasic was called earlier
        		for(Task task : stage.getTasks()){
        			task.setUserID(userIDTakingNewPlan);
        			task.setTemplate(false);
        			task.setStageID(stageID);
        		}
        	}
        	
        	cn.commit();
        } catch (SQLException e) {
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
        }
    	
    	//save the plan - which is responsible for updating treatmentPlanID in all the child objects
    	return planToCopy.create();
    	
    }*/
	
	
	 public TreatmentPlan copyTreatmentPlanForClient(int userIDTakingNewPlan, int treatmentPlanIDBeingCopied, boolean isTemplate) throws ValidationException, DatabaseException{
    	TreatmentPlan planToCopy = TreatmentPlan.load(treatmentPlanIDBeingCopied);
    	planToCopy.setTemplate(isTemplate);
    	
    	planToCopy.setUserID(userIDTakingNewPlan);
    	//loop through and change all the userIDs to the userID supplied by the method argument
    	//OPTIMIZE O(N3) complexity here with 3 nested for loops.  Is there a better way to do this?
    	for(Stage stage : planToCopy.getStages()){
    		stage.setUserID(userIDTakingNewPlan);
    		List<Task> taskRepetitionsAdded = new ArrayList<>();
    		for(Task task : stage.getTasks()){
    			task.setUserID(userIDTakingNewPlan);
    			task.setTemplate(false);
    			task.setTemplateID(task.getTaskID());
    			for(int i = 0; i < task.getRepetitions(); i++){
    				Task taskRep = task.copy();
    				
    				if(task.getRepetitions() > 1){
    					taskRep.setTitle(task.getTitle() + " (" + (i+1) + ")");
    				}
    				
    				taskRep.setTaskOrder(taskRepetitionsAdded.size());
    				taskRepetitionsAdded.add(taskRep);
    			}
    			
    		}//end Task loop
    		
    		//reset the Stage's Task list with new list that has been populated with repetitions
    		stage.setTasks(taskRepetitionsAdded);
    		
    		//TODO before moving on to next stage in loop, update the task orders to account for any repetitions added?
    	}//end Stage loop
    	
    	//save the plan - which is responsible for updating treatmentPlanID in all the child objects
    	return planToCopy.create();
    	
    }
	
	@Override
	public String toString(){
		return "User id:" + email + ", User email: " + email;
	}

	
}
