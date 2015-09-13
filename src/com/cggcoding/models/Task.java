package com.cggcoding.models;

import java.time.LocalDate;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;


public abstract class Task implements Completable, Updateable{
	private int taskID;
	private int stageID;
	private int userID;
	private int taskTypeID;
	private int parentTaskID;//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
	private String title;
	private String instructions;
	private String resourceLink;
	private boolean completed;
	private LocalDate dateCompleted;
	private int taskOrder;
	private boolean extraTask;
	private boolean template;
	private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();
	
	//TODO see if I can eliminate some of these constructors!
	//empty constructor necessary to allow static factory methods in subclasses
	public Task(){
		
	}
	
	//basic parent Task that sets properties to defaults
	public Task(int taskID, int userID) {
		this.taskID = taskID;
		this.stageID = 0;
		this.userID = userID;
		this.taskTypeID = 0;
		this.parentTaskID = 0;
		this.title = "";
		this.instructions = "";
		this.resourceLink = null;
		this.completed = false;
		this.dateCompleted = null;
		this.taskOrder = 0;
		this.extraTask = false;
		this.template = false;
	}
	
	//basic parent Task that sets properties to defaults
	public Task(int taskID, int userID, String title, String instructions){
		this.taskID = taskID;
		this.stageID = 0;
		this.userID = userID;
		this.taskTypeID = 0;
		this.parentTaskID = 0;
		this.title = title;
		this.instructions = instructions;
		this.resourceLink = null;
		this.completed = false;
		this.dateCompleted = null;
		this.taskOrder = 0;
		this.extraTask = false;
		this.template = false;
	}

	//basic parent Task before database insert that has no id and sets properties to defaults
	public Task(int userID, String title, String instructions){
		this.taskID = 0;
		this.stageID = 0;
		this.userID = userID;
		this.taskTypeID = 0;
		this.parentTaskID = 0;
		this.title = title;
		this.instructions = instructions;
		this.resourceLink = null;
		this.completed = false;
		this.dateCompleted = null;
		this.taskOrder = 0;
		this.extraTask = false;
		this.template = false;
	}
	
	//basic constructor if task is going to be a subtask
	public Task(int taskID, int userID, int parentTaskID, String title, String instructions){
		this.taskID = taskID;
		this.stageID = 0;
		this.userID = userID;
		this.taskTypeID = 0;
		this.parentTaskID = parentTaskID;
		this.title = title;
		this.instructions = instructions;
		this.resourceLink = null;
		this.completed = false;
		this.dateCompleted = null;
		this.taskOrder = 0;
		this.extraTask = false;
		this.template = false;
	}
	
	public Task (int userID, int taskTypeID, String title, String instructions, String resourceLink, boolean extraTask, boolean template) throws DatabaseException, ValidationException{
		this.taskID = 0;
		this.stageID = 0;
		this.userID = userID;
		this.taskTypeID = taskTypeID;
		this.parentTaskID = 0;
		this.title = title;
		this.instructions = instructions;
		this.resourceLink = resourceLink;
		this.completed = false;
		this.dateCompleted = null;
		this.taskOrder = 0;
		this.extraTask = extraTask;
		this.template = true;
	}
	
	
	/**Saves all of the fields in Task into the database table that holds the common fields for all tasks
	 * @param taskID
	 * @param stageID
	 * @param userID
	 * @param taskTypeID
	 * @param parentTaskID
	 * @param title
	 * @param instructions
	 * @param resourceLink
	 * @param completed
	 * @param dateCompleted
	 * @param taskOrder
	 * @param extraTask
	 * @param template
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	protected Task saveGeneralDataForTemplateInDatabase(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title, 
			String instructions, String resourceLink, boolean completed, LocalDate dateCompleted, int taskOrder,
			boolean extraTask, boolean template) throws DatabaseException, ValidationException{
		this.taskID = taskID;
		this.stageID = stageID;
		this.userID = userID;
		this.taskTypeID = taskTypeID;
		this.parentTaskID = parentTaskID;
		this.title = title;
		this.instructions = instructions;
		this.resourceLink = resourceLink;
		this.completed = completed;
		this.dateCompleted = dateCompleted;
		this.taskOrder = taskOrder;
		this.extraTask = extraTask;
		this.template = template;
		
		return databaseActionHandler.taskTemplateValidateAndCreate(this);
	}
	
	public int getTaskID(){
		return taskID;
	}

	public void setTaskID(int taskID){
		this.taskID = taskID;
	}
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getTaskTypeID() {
		return taskTypeID;
	}

	public void setTaskTypeID(int taskTypeID) {
		this.taskTypeID = taskTypeID;
	}

	public int getParentTaskID() {
		return parentTaskID;
	}

	public void setParentTaskID(int parentTaskID) {
		this.parentTaskID = parentTaskID;
	}

	public int getStageID() {
		return stageID;
	}

	public void setStageID(int stageID) {
		this.stageID = stageID;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getResourceLink() {
		return resourceLink;
	}

	public void setResourceLink(String resourceLink) {
		this.resourceLink = resourceLink;
	}
	
	public void setDateCompleted(LocalDate date){
		dateCompleted = date;
	}
	
	public LocalDate getDateCompleted(){
		return dateCompleted;
	}
	
	public int getTaskOrder() {
		return taskOrder;
	}

	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean isTemplate) {
		this.template = isTemplate;
	}

	public boolean isExtraTask() {
		return extraTask;
	}

	public void setExtraTask(boolean extraTask) {
		this.extraTask = extraTask;
	}

	public void setCompleted(boolean status){
		this.completed = status;
	}

	public boolean isParentTask(){
		return parentTaskID == 0;
	}

	
	@Override
	public boolean isCompleted() {
		return completed;
	}
	
	@Override
	public void markComplete() {
		completed = true;
		dateCompleted = LocalDate.now();
	}

	@Override
	public void markIncomplete() {
		completed = false;
	}

	@Override
	public int getPercentComplete() {
		if (isCompleted()) {
			return 100;
		} else {
			return 0;
		}
		//return (int)(((double)repetitionsCompleted/(double)repetitions) * 100);
	}

	//TODO redo this to get name from the database table
	public String getTaskTypeName(){
		return this.getClass().getSimpleName();
	}

	//in place so can be overridden by concrete classes
	@Override
	public boolean updateData(Task taskWithNewData) {
		return false;
	}
	
	private void transferDataToThisTask(Task task){
		
	}
	

}
