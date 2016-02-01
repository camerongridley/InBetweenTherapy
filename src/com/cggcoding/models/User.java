package com.cggcoding.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
	private String role;
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

	public TreatmentPlan getTreatmentPlan(int treatmentPlanID) throws ValidationException{
		TreatmentPlan planFound = null;
		for(TreatmentPlan plan : treatmentPlanList){
			if(plan.getTreatmentPlanID()==treatmentPlanID){
				planFound = plan;
			}
		}
		
		if(planFound == null){
			throw new ValidationException(ErrorMessages.NO_PLAN_FOUND_FOR_USER);
		}
		return planFound;
 
	}

	//UNSURE make these methods abstract
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
	
	//TODO redo this to loop MapStageTaskTemplate and MapTreatmentPlanStageTemplate objects instead of using TreatmentPlan.load()?
	 public TreatmentPlan copyTreatmentPlanForClient(int userIDTakingNewPlan, int treatmentPlanIDBeingCopied, boolean isTemplate) throws ValidationException, DatabaseException{
    	TreatmentPlan planToCopy = TreatmentPlan.load(treatmentPlanIDBeingCopied);
    	planToCopy.setTemplate(isTemplate);//XXX won't this always be false since plans owned by clients can never be templates?
    	planToCopy.setTemplateID(planToCopy.getTreatmentPlanID());
    	planToCopy.setUserID(userIDTakingNewPlan);
    	planToCopy.setAssignedByUserID(this.userID);
    	//loop through and change all the userIDs to the userID supplied by the method argument
    	//OPTIMIZE O(N3) complexity here with 3 nested for loops.  Is there a better way to do this?
    	for(Stage stage : planToCopy.getStages()){
    		stage.setUserID(userIDTakingNewPlan);
    		stage.setTemplate(false);
    		stage.setTemplateID(stage.getStageID());
    		//OPTIMIZE getMappedStageTemplateByID uses another loop to get the return value, so even more Big O complexity...
    		stage.setClientStageOrder(planToCopy.getMappedStageTemplateByStageID(stage.getStageID()).getTemplateStageOrder());
    		
    		List<Task> taskRepetitionsAdded = new ArrayList<>();
    		for(Task task : stage.getTasks()){
    			task.setUserID(userIDTakingNewPlan);
    			task.setTemplate(false);
    			task.setTemplateID(task.getTaskID());
    			
    			MapStageTaskTemplate taskDetail = stage.getMappedTaskTemplateByTaskID(task.getTaskID());
    			int taskReps = taskDetail.getTemplateRepetitions();
    			int taskOrder = taskDetail.getTemplateTaskOrder();//for now this isn't used since the task orders are going to change as repetitions are created
    			for(int i = 0; i<taskReps; i++){
    				Task taskRep = task.copy();
    				taskRep.setClientRepetition(i+1);
    				
    				//if more than 1 repetition, change the Task title to reflect repetition number
    				if(taskReps > 1){
    					taskRep.setTitle(task.getTitle() + " (" + (i+1) + ")");
    				}
    				
    				taskRep.setClientTaskOrder(taskRepetitionsAdded.size());
    				
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
	
	public static User loadBasic(int userID) throws DatabaseException, ValidationException{
		return dao.userLoadByID(userID);
	}
	
	@Override
	public String toString(){
		return "User id:" + email + ", User email: " + email;
	}

	
}
