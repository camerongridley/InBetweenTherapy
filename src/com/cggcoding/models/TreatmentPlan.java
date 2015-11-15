package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class TreatmentPlan implements DatabaseModel{
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
	
	private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();
	
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

	public void initialize(){
		stages.get(0).setInProgress(true);
		setInProgress(true);
		//currentStageIndex = stages.get(0).getStageID();
		//activeViewStageIndex = currentStageIndex;
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
		
		databaseActionHandler.treatmentPlanValidateAndUpdateBasic(this);
		databaseActionHandler.stageValidateAndUpdateBasic(nextStage);
		
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
	public Object saveNew() throws ValidationException, DatabaseException{
		 TreatmentPlan savedPlan = databaseActionHandler.treatmentPlanValidateAndCreate(this);
		 this.treatmentPlanID = savedPlan.getTreatmentPlanID();
		 return savedPlan;
	}

	@Override
	public void update() throws ValidationException, DatabaseException {
		databaseActionHandler.treatmentPlanValidateAndUpdateBasic(this);
		
	}

	@Override
	public void delete() throws ValidationException, DatabaseException {
		databaseActionHandler.treatmentPlanDelete(this.treatmentPlanID);
		
	}
	
	public static void delete(int treatmentPlanID) throws DatabaseException, ValidationException{
		databaseActionHandler.treatmentPlanDelete(treatmentPlanID);
	}

	public static TreatmentPlan load(int treatmentPlanID) throws DatabaseException, ValidationException{
		TreatmentPlan plan = databaseActionHandler.treatmentPlanLoad(treatmentPlanID);
		
		return plan;
	}
	
	/*public void loadStages() throws DatabaseException, ValidationException{
		List<Integer> stageIDs = databaseActionHandler.treatmentPlanGetStageIDs(this.treatmentPlanID);
		for(int stageID : stageIDs){
			addStage(Stage.load(stageID));
		}
	}*/
	
	public static TreatmentPlan loadWithEmptyLists(int treatmentPlanID) throws DatabaseException, ValidationException{
		return databaseActionHandler.treatmentPlanLoadWithEmpyLists(treatmentPlanID);
	}
	
	public void deleteStage(int stageID) throws ValidationException, DatabaseException {
		for(int i=0; i < this.stages.size(); i++){
			if(stages.get(i).getStageID()==stageID){
				stages.remove(i);
			}
		}
		
		for(int i=0; i < this.stages.size(); i++){
			stages.get(i).setStageOrder(i);
		}
		
		databaseActionHandler.treatmentPlanDeleteStage(stageID, stages);
	}
	
	/*public TreatmentPlan copy(int userID){
		TreatmentPlan copiedPlan = null;
		
		return copiedPlan;
	}*/
	
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
		
		stageBeingCopied.saveNew();
		
		this.addStage(stageBeingCopied);
		
		return stageBeingCopied;
	}
	
	public Stage createNewStage(int userID, String title, String description) throws ValidationException, DatabaseException{
		
		Stage newStage = Stage.getInstanceWithoutID(this.treatmentPlanID, userID, title, description, this.getStageOrderDefaultValue(), false);
		newStage.saveNew();
		
		this.addStage(newStage);
		
		return newStage;
	}
}
