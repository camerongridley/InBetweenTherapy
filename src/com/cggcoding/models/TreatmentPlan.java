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

	private TreatmentPlan(int treatmentPlanID, int userID, String title, String description, int txIssueID, boolean inProgress, boolean isTemplate, boolean completed){
		this.treatmentPlanID = treatmentPlanID;
		this.title = title;
		this.description = description;
		this.treatmentIssueID = txIssueID;
		this.userID = userID;
		this.stages = new ArrayList<>();
		this.currentStageIndex = 0;
		this.activeViewStageIndex = 0;
		this.inProgress = inProgress;
		this.isTemplate = isTemplate;
		this.completed = completed;
	}
	
	public static TreatmentPlan getInstanceWithoutID(String title, int userID, String description, int treatmentPlanID){
		return new TreatmentPlan(userID, title, description, treatmentPlanID);
	}
	
	public static TreatmentPlan getInstanceBasic(int treatmentPlanID, int userID, String title, String description, int txIssueID, boolean inProgress, boolean isTemplate, boolean completed){
		return new TreatmentPlan(treatmentPlanID, userID, title, description, txIssueID, inProgress, isTemplate, completed);
	}

	//TODO update with proper logic once app is connected to database
	//- set these variables based on the stage that is in progress
	//- done either dynamically by looping through all the plan's stages and checking inProgress status or by saving currentStage in database
	public void initialize(){
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

	public int getNumberOfStages(){
		return stages.size();
	}
	
	public Stage nextStage(){

		if(activeViewStageIndex == currentStageIndex){
			if(currentStageIndex < getNumberOfStages()-1){
				currentStageIndex++;
				activeViewStageIndex = currentStageIndex;
			}
		}
		
		return stages.get(activeViewStageIndex);
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
	
	@Override
	public void saveNew() throws ValidationException, DatabaseException{
		 TreatmentPlan savedPlan = databaseActionHandler.treatmentPlanValidateAndCreateBasic(this);
		 this.treatmentPlanID = savedPlan.getTreatmentPlanID();
	}

	@Override
	public void update() throws ValidationException, DatabaseException {
		// TODO implement method
		
	}

	@Override
	public void delete() throws ValidationException, DatabaseException {
		// TODO implement method
		
	}

	@Override
	public List<Object> copy(Object o, int numberOfCopies) {
		// TODO implement method
		return null;
	}

	public static TreatmentPlan load(int treatmentPlanID) throws DatabaseException, ValidationException{
		TreatmentPlan plan = databaseActionHandler.treatmentPlanLoadWithEmpyLists(treatmentPlanID);
		plan.loadStages();

		return plan;
	}
	
	public void loadStages() throws DatabaseException, ValidationException{
		List<Integer> stageIDs = databaseActionHandler.treatmentPlanGetStageIDs(this.treatmentPlanID);
		for(int stageID : stageIDs){
			addStage(Stage.load(stageID));
		}
	}
	
	//TODO delete this method?
	public static TreatmentPlan loadWithEmptyLists(int treatmentPlanID) throws DatabaseException{
		return databaseActionHandler.treatmentPlanLoadWithEmpyLists(treatmentPlanID);
	}
}
