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

public class TreatmentPlan implements Serializable, DatabaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int treatmentPlanID;
	private int userID;
	private int treatmentIssueID;
	private String title;
	private String description;
	private List<Stage> stages;
	private int currentStageIndex;
	private int activeViewStageIndex;
	private boolean inProgress;
	private boolean isTemplate;
	private boolean completed;
	
	private static DatabaseActionHandler dao= new MySQLActionHandler();
	
	private TreatmentPlan(int userID, String title, String description, int treatmentPlanID){
		this.title = title;
		this.userID = userID;
		this.description = description;
		this.treatmentIssueID = treatmentPlanID;
		this.stages = new ArrayList<>();
		this.currentStageIndex = 0;
		this.activeViewStageIndex = 0;
		this.inProgress = false;
		this.isTemplate = false;
		this.completed = false;
	}	
	
	private TreatmentPlan(int treatmentPlanID, int userID, String title, String description, int txIssueID){
		this.treatmentPlanID = treatmentPlanID;
		this.title = title;
		this.description = description;
		this.treatmentIssueID = txIssueID;
		this.userID = userID;
		this.stages = new ArrayList<>();
		this.currentStageIndex = 0;
		this.activeViewStageIndex = 0;
		this.inProgress = false;
		this.isTemplate = false;
		this.completed = false;
	}

	private TreatmentPlan(int treatmentPlanID, int userID, String title, String description, int txIssueID, boolean inProgress, 
			boolean isTemplate, boolean completed, int currentStageIndex, int activeViewStageIndex){
		this.treatmentPlanID = treatmentPlanID;
		this.title = title;
		this.description = description;
		this.treatmentIssueID = txIssueID;
		this.userID = userID;
		this.stages = new ArrayList<>();
		this.currentStageIndex = currentStageIndex;
		this.activeViewStageIndex = activeViewStageIndex;
		this.inProgress = inProgress;
		this.isTemplate = isTemplate;
		this.completed = completed;
	}
	
	public static TreatmentPlan getInstanceWithoutID(String title, int userID, String description, int treatmentPlanID){
		return new TreatmentPlan(userID, title, description, treatmentPlanID);
	}
	
	public static TreatmentPlan getInstanceBasic(int treatmentPlanID, int userID, String title, String description, int txIssueID, boolean inProgress, 
			boolean isTemplate, boolean completed, int currentStageIndex, int activeViewStageIndex){
		return new TreatmentPlan(treatmentPlanID, userID, title, description, txIssueID, inProgress, isTemplate, completed, currentStageIndex, activeViewStageIndex);
	}

	/**Run first time a client loads a plan.  Sets inProgress=true for the TreatmentPlan itself and for the first stage of the plan
	 * then updates them in the database
	 * @throws DatabaseException 
	 * @throws ValidationException 
	 */
	public void initialize() throws ValidationException, DatabaseException{
		Stage firstStage = stages.get(0);
		firstStage.setInProgress(true);
		this.setInProgress(true);
		//currentStageIndex = stages.get(0).getStageID();
		//activeViewStageIndex = currentStageIndex;
		firstStage.updateBasic();
		updateBasic();
		
	}

	public void setTreatmentPlanID(int planID){
		this.treatmentPlanID = planID;
	}
	
	public int getTreatmentPlanID(){
		return treatmentPlanID;
	}
	
	public int getUserID(){
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setTitle(String title){
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setTreatmentIssueID(int treatmentIssueID){
		this.treatmentIssueID = treatmentIssueID;
	}
	
	public int getTreatmentIssueID() {
		return treatmentIssueID;
	}

	public void setStages(List<Stage> stages){
		this.stages = stages;
	}
	
	public List<Stage> getStages(){
		return stages;
	}
	
	public void addStage(Stage newStage){
		stages.add(newStage);
	}
	
	public void updateStages(){
		//
	}
	
	public Stage getStage(int stageID){
		return stages.get(stageID);
	}

	public int getCurrentStageIndex() {
		return currentStageIndex;
	}

	public void setCurrentStageIndex(int currentStageIndex) {
		this.currentStageIndex = currentStageIndex;
	}

	public int getActiveViewStageIndex() {	return activeViewStageIndex; }

	public void setActiveViewStageIndex(int activeViewStageIndex) {
		this.activeViewStageIndex = activeViewStageIndex;
	}

	public Stage getCurrentStage(){
		return stages.get(currentStageIndex);
	}

	public Stage getActiveViewStage() {
		return stages.get(activeViewStageIndex);
	}
	
	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public boolean isTemplate() {
		return isTemplate;
	}

	public void setTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getNumberOfStages(){
		return stages.size();
	}
	
	public Stage nextStage() throws DatabaseException, ValidationException{

		if(activeViewStageIndex == currentStageIndex){
			if(currentStageIndex < getNumberOfStages()-1){
				currentStageIndex++;
				activeViewStageIndex = currentStageIndex;
				this.setCompleted(false);
			} else {
				this.setCompleted(true);
				this.setInProgress(false);
				
			}
		}
		
		Stage nextStage = stages.get(activeViewStageIndex);
		nextStage.setInProgress(true);
		
		this.updateBasic();
		nextStage.update();
		//databaseActionHandler.stageValidateAndUpdateBasic(nextStage);
		
		return nextStage;
	}

	public int getStageOrder(int stageID){
		int stageOrder = 0;
		for(Stage stage : stages){
			if(stage.getStageID() == stageID){
				stageOrder = stage.getStageOrder();
				return stageOrder;
			}
		}
		return stageOrder;
	}

	public int getCurrentStageOrder(){
		return getStageOrder(currentStageIndex);
	}

	public int getActiveViewStageOrder(){
		return getStageOrder(activeViewStageIndex);
	}
	
	private int getStageOrderDefaultValue(){
		return this.stages.size();
	}
	
	@Override
	public TreatmentPlan create() throws ValidationException, DatabaseException{
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	
        	create(cn);
        	
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

        return this;
		
	}
	
	public TreatmentPlan create(Connection cn) throws ValidationException, SQLException{
		
		if(dao.treatmentPlanValidateNewTitle(cn, this.userID, this.title)){
    		dao.treatmentPlanCreateBasic(cn, this);//createBasic(cn);
    	}
    	
    	for(Stage stage : this.stages){
			//set the new treatmentPlanID generated by the creation of the copy
			stage.setTreatmentPlanID(this.treatmentPlanID);
			stage.create(cn);
    	}
    	
    	dao.throwValidationExceptionIfNull(this);

		 return this;
	}
	
	@Override
	public void update() throws ValidationException, DatabaseException {
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	
        	updateBasic(cn);
        	
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
		
	}
	
	public void updateBasic() throws ValidationException, DatabaseException {
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();  	
        	updateBasic(cn);
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(cn);
        }
		
	}
	
	public void updateBasic(Connection cn) throws ValidationException, SQLException {
		if(dao.treatmentPlanValidateUpdatedTitle(cn, this)){
    		dao.treatmentPlanUpdateBasic(cn, this);
    	}
	}

	@Override
	public void delete() throws ValidationException, DatabaseException {
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
            
        	delete(cn);
            
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(cn);
        }
		
	}
	
	public void delete(Connection cn) throws ValidationException, SQLException {
		dao.throwValidationExceptionIfTemplateHolderID(this.treatmentPlanID);
		
		dao.treatmentPlanDelete(cn, this.treatmentPlanID);
		
	}
	
	public static void delete(int treatmentPlanID) throws DatabaseException, ValidationException{
		Connection cn = null;
        
		dao.throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
		
        try {
        	cn = dao.getConnection();
            
        	dao.treatmentPlanDelete(cn, treatmentPlanID);
            
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
        } finally {
			DbUtils.closeQuietly(cn);
        }
		
	}

	public static TreatmentPlan load(int treatmentPlanID) throws DatabaseException, ValidationException{
		Connection cn = null;
        TreatmentPlan plan = null;
        
        dao.throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
        	cn = dao.getConnection();
        	
            plan = load(cn, treatmentPlanID);
            
        } catch (SQLException e) {
			
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
        }

        dao.throwValidationExceptionIfNull(plan);
        
        return plan;

	}
	
	public static TreatmentPlan load(Connection cn, int treatmentPlanID) throws ValidationException, SQLException{
		TreatmentPlan plan = null;
		
		//Load the basic plan
		plan = dao.treatmentPlanLoadBasic(cn, treatmentPlanID);
        
		//Load the Stages
		List<Integer> stageIDs = dao.treatmentPlanGetStageIDs(cn, treatmentPlanID); 
		
		for(int stageID : stageIDs){
			plan.addStage(Stage.load(cn, stageID));
		}
        
		
		return plan;
	}
	
	public static TreatmentPlan loadBasic(int treatmentPlanID) throws DatabaseException, ValidationException{
		Connection cn = null;
        TreatmentPlan plan = null;
        
        dao.throwValidationExceptionIfTemplateHolderID(treatmentPlanID);
        
        try {
        	cn = dao.getConnection();
        	
            plan = dao.treatmentPlanLoadBasic(cn, treatmentPlanID);
            
        } catch (SQLException e) {
			
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
        }

        dao.throwValidationExceptionIfNull(plan);
        
        return plan;
		
	}
	
	
	public void deleteStage(int stageID) throws ValidationException, DatabaseException {
		
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			cn.setAutoCommit(false);
			
			//delete specified stage from database
			Stage.delete(cn, stageID);
			
			//remove the stage from the local variable
			for(int i=0; i < this.stages.size(); i++){
				if(stages.get(i).getStageID()==stageID){
					stages.remove(i);
				}
			}
			
			reorderStages();
			
			//update other stages to reflect changes in order et.al.
			for(Stage stage : stages){
				stage.updateBasic(cn);
			}
			
			cn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
		}
	}
	
	private void reorderStages(){
		for(int i=0; i < this.stages.size(); i++){
			stages.get(i).setStageOrder(i);
		}
	}
	
	
	/**Copies a pre-existing Stage into a TreatmentPlan.  This methods gets the existing Stage, updated the treatmentPlanID and the userID associated with the new TreatmentPlan that the
	 * Stage is being copies into.  It also sets the copied Stages's isTemplate to false and determines the stageOrder it will have initially.
	 * Then the copied Stage is sent the DAO to be saved in the database.  The DAO is responsible for getting the auto-generated id for the new Stage and setting it for the Stage and 
	 * all children of the Stage before returning it to this method.
	 * , and adds it the the TreatmentPlan's list of Stages.
	 * Then it 
	 * @param stageIDBeingCopied
	 * @param userIDCopiedInto
	 * @return A Stage with updated userID, treatmentPlanID and a new stageID
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public Stage copyStageIntoTreatmentPlan(int stageIDBeingCopied) throws DatabaseException, ValidationException{
		Stage stageBeingCopied = Stage.load(stageIDBeingCopied);
		stageBeingCopied.setTemplate(false);
		stageBeingCopied.setUserID(this.userID);
		stageBeingCopied.setTreatmentPlanID(this.treatmentPlanID);
		
		//set stageID in all children to -1 in case there is an error so nothings accidentally gets inserted into other users information - SQL rollback should prevent this but doing this adds another layer of data protection
		stageBeingCopied.setStageID(-1);
		for(StageGoal goal : stageBeingCopied.getGoals()){
			goal.setStageID(-1);
		}
		
		for(Task task : stageBeingCopied.getTasks()){
			task.setStageID(-1);
		}
		
		//since ArrayLists start with index of 0, setting the order of the new stage to the number of stages will give the proper order number
		stageBeingCopied.setStageOrder(this.getNumberOfStages());
		
		stageBeingCopied.create();
		
		this.addStage(stageBeingCopied);
		
		return stageBeingCopied;
	}
	
	public Stage createNewStage(int userID, String title, String description) throws ValidationException, DatabaseException{
		
		Stage newStage = Stage.getInstanceWithoutID(this.treatmentPlanID, userID, title, description, this.getStageOrderDefaultValue(), false);
		newStage.create();
		
		this.addStage(newStage);
		
		return newStage;
	}
	
	public static List<TreatmentPlan> getDefaultTreatmentPlans() throws DatabaseException, ValidationException {
		return dao.treatmentPlanGetDefaults();
	}
}
