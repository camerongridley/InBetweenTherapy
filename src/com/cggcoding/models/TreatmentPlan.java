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
import com.cggcoding.utils.Constants;
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
	private List<MapTreatmentPlanStageTemplate> treatmentPlanStageTemplateMapList;
	private int currentStageIndex;
	private int activeViewStageIndex;
	private boolean inProgress;
	private boolean template;
	private boolean completed;
	private int templateID;
	private int assignedByUserID;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	
	private TreatmentPlan(int userID, String title, String description, int treatmentIssueID){
		this.title = title;
		this.userID = userID;
		this.description = description;
		this.treatmentIssueID = treatmentIssueID;
		this.stages = new ArrayList<>();
		this.treatmentPlanStageTemplateMapList = new ArrayList<>();
		this.currentStageIndex = 0;
		this.activeViewStageIndex = 0;
		this.inProgress = false;
		this.template = false;
		this.completed = false;
		this.templateID = 0;
		this.assignedByUserID = Constants.ADMIN_ROLE_ID;
	}	

	private TreatmentPlan(int treatmentPlanID, int userID, String title, String description, int txIssueID, boolean inProgress, 
			boolean template, boolean completed, int currentStageIndex, int activeViewStageIndex, int templateID, int assignedByUserID){
		this.treatmentPlanID = treatmentPlanID;
		this.title = title;
		this.description = description;
		this.treatmentIssueID = txIssueID;
		this.userID = userID;
		this.stages = new ArrayList<>();
		this.treatmentPlanStageTemplateMapList = new ArrayList<>();
		this.currentStageIndex = currentStageIndex;
		this.activeViewStageIndex = activeViewStageIndex;
		this.inProgress = inProgress;
		this.template = template;
		this.completed = completed;
		this.templateID = templateID;
		this.assignedByUserID = assignedByUserID;
	}
	
	public static TreatmentPlan getInstanceWithoutID(String title, int userID, String description, int treatmentIssueID){
		return new TreatmentPlan(userID, title, description, treatmentIssueID);
	}
	
	public static TreatmentPlan getInstanceBasic(int treatmentPlanID, int userID, String title, String description, int txIssueID, boolean inProgress, 
			boolean template, boolean completed, int currentStageIndex, int activeViewStageIndex, int templateID, int assignedByUserID){
		return new TreatmentPlan(treatmentPlanID, userID, title, description, txIssueID, inProgress, template, completed, currentStageIndex, activeViewStageIndex, templateID, assignedByUserID);
	}

	/**Run first time a client loads a plan.  Sets inProgress=true for the TreatmentPlan itself and for the first stage of the plan
	 * then updates them in the database
	 * @throws DatabaseException 
	 * @throws ValidationException 
	 */
	public void initialize() throws ValidationException, DatabaseException{
		if(stages.size() != 0){
			Stage firstStage = stages.get(0);
			firstStage.setInProgress(true);
			firstStage.updateBasic();
		}
		
		this.setInProgress(true);
		
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
	
	public List<MapTreatmentPlanStageTemplate> getTreatmentPlanStageTemplateMapList() {
		return treatmentPlanStageTemplateMapList;
	}

	public void setTreatmentPlanStageTemplateMapList(List<MapTreatmentPlanStageTemplate> treatmentPlanStageTemplateMapList) {
		this.treatmentPlanStageTemplateMapList = treatmentPlanStageTemplateMapList;
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
		return template;
	}

	public void setTemplate(boolean template) {
		this.template = template;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	
	public int getAssignedByUserID() {
		return assignedByUserID;
	}

	public void setAssignedByUserID(int assignedByUserID) {
		this.assignedByUserID = assignedByUserID;
	}
	
	public int getNumberOfStages(){
		return stages.size();
	}
	
	//TODO update so this gives a value that incorporates each stages percent complete
	public int percentComplete(){
		double stagesComplete = 0;
		for(Stage stage : stages){
			if(stage.isCompleted()){
				stagesComplete = stagesComplete + 1;
			}
		}
		
		if(stagesComplete!=0){
			return (int) (stagesComplete/getNumberOfStages()*100);
		} else {
			return 0;
		}
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
				stageOrder = stage.getClientStageOrder();
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
	
	public MapTreatmentPlanStageTemplate getMappedStageTemplateByStageID(int stageID){
		MapTreatmentPlanStageTemplate found = null;
		for(MapTreatmentPlanStageTemplate planStageTemplate : treatmentPlanStageTemplateMapList){
			if(stageID == planStageTemplate.getStageID()){
				found = planStageTemplate;
				break;
			}
		}
		return found;
	}
	
	//OPTIMIZE move this logic to when the TreatmentPlan's Stages are loaded
	public void setTasksDisabledStatus(int loggedInUserID){
		/*if the userID of the TreatmentPlan doesn't equal the userID of the user logged in 
		 * (e.g. plan belongs to a client and the logged in user is the therapist of the client)
		 * then all tasks should be disabled.  If the logged in user also owns the plan, then all
		 * tasks that are in Stages that haven't been started yet should be disabled (i.e. stages where completed==false and inProgress==false)
		*/
		if(this.userID != loggedInUserID){
			for(Stage stage : this.stages){
				for(Task task : stage.getTasks()){
					task.setDisabled(true);
				}
			}
		}else{
			for(Stage stage : this.stages){
				if(!stage.isCompleted() && !stage.isInProgress()){
					for(Task task : stage.getTasks()){
						task.setDisabled(true);
					}
				}
			}
		}
	}
	
	@Override
	public TreatmentPlan create() throws ValidationException, DatabaseException{
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	
        	create(cn);
        	
        	cn.commit();
        } catch (SQLException | ValidationException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
			
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
	
	protected TreatmentPlan create(Connection cn) throws ValidationException, SQLException{
		
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
	
	public TreatmentPlan createBasic() throws ValidationException, DatabaseException {
		
		Connection cn = null;
        TreatmentPlan plan = null;
        try {
        	cn = dao.getConnection();  	
        	plan = createBasic(cn);
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(cn);
        }
        
        return plan;
	}
	
	protected TreatmentPlan createBasic(Connection cn) throws ValidationException, SQLException {
		TreatmentPlan plan = null;
		if(dao.treatmentPlanValidateNewTitle(cn, this.userID, this.title)){
			dao.treatmentPlanCreateBasic(cn, this);
		}
		return plan;
	}
	
	@Override
	public void update() throws ValidationException, DatabaseException {
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	
        	updateBasic(cn);
        	
        	cn.commit();
        } catch (SQLException | ValidationException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
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
	
	protected void updateBasic(Connection cn) throws ValidationException, SQLException {
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
	
	protected void delete(Connection cn) throws ValidationException, SQLException {
		dao.throwValidationExceptionIfTemplateHolderID(this.treatmentPlanID);
		
		dao.treatmentPlanDelete(cn, this.treatmentPlanID);
		
	}
	
	public static void delete(int treatmentPlanID) throws DatabaseException, ValidationException{
		Connection cn = null;
        
		dao.throwValidationExceptionIfZero(treatmentPlanID);
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
		if(plan.isTemplate()){
			List<MapTreatmentPlanStageTemplate> stageMap = dao.mapTreatmentPlanStageTemplateLoad(cn, treatmentPlanID);
			plan.setTreatmentPlanStageTemplateMapList(stageMap);
			
			//OPTIMIZE modify so dao.treatmentPlanLoadClientStages can be used here too - maybe rename treatmentPlanLoadClientStages then
			for(MapTreatmentPlanStageTemplate planStageDetail : plan.treatmentPlanStageTemplateMapList){
				plan.addStage(Stage.load(cn, planStageDetail.getStageID()));
			}
			
		}else{
			plan.setStages(dao.treatmentPlanLoadClientStages(cn, treatmentPlanID));
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
	
	
	//TODO rename to deleteStageTemplate and refactor to use MapTreatmentPlanStageTemplate class
	public TreatmentPlan deleteStage(int stageID) throws ValidationException, DatabaseException {
		
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			cn.setAutoCommit(false);
			Stage stage = null;
			//remove the stage from the local variable
			for(int i=0; i < this.stages.size(); i++){
				stage = stages.get(i);
				if(stage.getStageID()==stageID){
					
					if(stage.isTemplate()){
						MapTreatmentPlanStageTemplate.delete(cn, stageID, this.treatmentPlanID);
						treatmentPlanStageTemplateMapList.remove(i);
					}else{
						Stage.delete(cn, stageID);
					}	
					
					stages.remove(i);
					break;
				}
			}
			
				
			reorderStages();
			
			//update other stages to reflect changes in order et.al.
			if(stage.isTemplate()){
				updateStageTemplateList(cn, stages);
			}else{
				//OPTIMIZE Could replace this with method in dao that takes List<Stage> and loops through updating
				for(Stage updateStage : stages){
					updateStage.updateBasic(cn);
				}
			}
			

			cn.commit();
		} catch (SQLException | ValidationException e) {
            e.printStackTrace();
            try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DatabaseException(ErrorMessages.ROLLBACK_DB_ERROR);
			}
            if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
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
	
	protected List<Stage> updateStageTemplateList(Connection cn, List<Stage> stageTemplates) throws SQLException{
		return dao.treatmentPlanUpdateTemplateStages(cn, this.treatmentPlanID, stageTemplates);
	}
	
	private void reorderStages(){
		//if this Stage is a template then it's task order info is going to be in the mapping table so reorder those.  Otherwise, the task order is a prop of the task
		if(this.template){
			for(int i=0; i < this.treatmentPlanStageTemplateMapList.size(); i++){
				treatmentPlanStageTemplateMapList.get(i).setTemplateStageOrder(i);
			}
		} else {
			for(int i=0; i < this.stages.size(); i++){
				stages.get(i).setClientStageOrder(i);
			}
		}
		
	}
	
	/**Adds a stage template to a treatment plan template.  Inserts into stageTemplate-Constants.PATH_TEMPLATE_TREATMENT_PLAN mapping table.
	 * Then loads the specified stage template into the local stages List variable. 
	 * Both the Stage and TreatmentPlan must be templates to be valid.
	 * @param stageTemplateID
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public void addStageTemplate(int stageTemplateID) throws DatabaseException, ValidationException{
		Connection cn = null;
	
		if(this.isTemplate()){
			try {
				
	        	cn = dao.getConnection();
	        	
        		//since ArrayLists start with index of 0, setting the order of the new stage to the number of stages will give the proper order number
        		MapTreatmentPlanStageTemplate mapPlanStageTemplate = new MapTreatmentPlanStageTemplate(treatmentPlanID, stageTemplateID, this.getNumberOfStages());
        		if(mapPlanStageTemplate.validate(cn)){
        			mapPlanStageTemplate.create(cn);
        			treatmentPlanStageTemplateMapList.add(mapPlanStageTemplate);
        		}
	        	
	        	this.stages.add(Stage.load(stageTemplateID));

			} catch (SQLException e) {
				e.printStackTrace();
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			} finally {
				DbUtils.closeQuietly(cn);
		    }		
		}else{
			throw new ValidationException(ErrorMessages.PLAN_IS_NOT_TEMPLATE);
		}
		
	}
	
	public Stage createClientStage(String stageTitle, String stageDescription) throws ValidationException, DatabaseException{
		Stage clientStage = null;
		if(!template){
			clientStage = Stage.getInstanceWithoutID(this.treatmentPlanID, this.userID, stageTitle, stageDescription, this.getStageOrderDefaultValue(), false);

			clientStage.create();
			
			this.addStage(clientStage);
		} else {
			throw new ValidationException(ErrorMessages.STAGE_CLIENT_ONLY_ALLOWED_IN_PLAN_TEMPLATE);
		}
		
		
		return clientStage;
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
		//since ArrayLists start with index of 0, setting the order of the new stage to the number of stages will give the proper order number
		stageBeingCopied.setClientStageOrder(this.getNumberOfStages());
		
		//set stageID in all children to -1 in case there is an error so nothings accidentally gets inserted into other users information - SQL rollback should prevent this but doing this adds another layer of data protection
		stageBeingCopied.setStageID(-1);
		for(StageGoal goal : stageBeingCopied.getGoals()){
			goal.setStageID(-1);
		}
		
		for(Task task : stageBeingCopied.getTasks()){
			task.setStageID(-1);
		}
		
		
		
		stageBeingCopied.create();
		
		this.addStage(stageBeingCopied);
		
		return stageBeingCopied;
	}
	
	public static List<TreatmentPlan> getDefaultTreatmentPlans() throws DatabaseException, ValidationException {
		return dao.treatmentPlanGetDefaults();
	}
}
