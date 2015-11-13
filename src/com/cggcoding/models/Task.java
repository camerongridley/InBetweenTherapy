package com.cggcoding.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;


public abstract class Task implements Completable, DatabaseModel{
	private int taskID;
	private int stageID;
	private int userID;
	private int taskTypeID;
	private int parentTaskID;//if this task is a subtask, then the parent's taskID is set here. If this is a parenttask it equals 0;
	private String title;
	private String instructions;
	private String resourceLink;
	private boolean completed;
	private LocalDateTime dateCompleted;
	private int taskOrder;
	private boolean extraTask;
	private boolean template;
	
	private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();
	
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
	
	/**Constructor for use before Task is inserted into the database, so no taskID is available to set, therefore a temporary value of 0 is given to taskID.  Since this is a new task and
	 * will not have had a chance to be completed, the properties completed and dateCompleted are given default values.  All other propertied take the value of the supplied arguments.
	 * @param stageID
	 * @param userID
	 * @param taskTypeID
	 * @param parentTaskID
	 * @param title
	 * @param instructions
	 * @param resourceLink
	 * @param taskOrder
	 * @param extraTask
	 * @param template
	 */
	public Task (int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink, int taskOrder, boolean extraTask, boolean template){
		this.taskID = 0;
		this.stageID = stageID;
		this.userID = userID;
		this.taskTypeID = taskTypeID;
		this.parentTaskID = parentTaskID;
		this.title = title;
		this.instructions = instructions;
		this.resourceLink = resourceLink;
		this.completed = false;
		this.dateCompleted = null;
		this.taskOrder = taskOrder;
		this.extraTask = extraTask;
		this.template = template;
	}
	
	
	/**Full constructor where every property is set by a supplied argument.
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
	 */
	public Task (int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink, boolean completed, LocalDateTime dateCompleted, int taskOrder, boolean extraTask, boolean template){
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
	}
	
	public static Task createTemplate(Task taskTemplate) throws ValidationException, DatabaseException{
		//TODO delete this line once method working as desired - int userID, String title, String instructions, int taskTypeID, int parentTaskID, String resourceLink, boolean extraTask
		//Task taskTemplate = new TaskGeneric(Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, Constants.TEMPLATE_ORDER_NUMBER, extraTask, true);
		taskTemplate.setStageID(Constants.DEFAULTS_HOLDER_PRIMARY_KEY_ID);
		taskTemplate.setTaskOrder(Constants.TEMPLATE_ORDER_NUMBER);
		taskTemplate.setTemplate(true);
		
		taskTemplate.saveNew();
		
		return taskTemplate;
	}
	
	public static Task load(int taskID) throws DatabaseException{
		Task task =  databaseActionHandler.taskLoad(taskID);
		//task = task.loadAdditionalData();
		return task;

	}
	
	public abstract Task loadAdditionalData();
	
	/*
	public static Task castToType(Task task){
		switch(task.getTaskTypeID()){
		case Constants.TASK_TYPE_ID_GENERIC_TASK:
			task = (TaskGeneric)task;
			break;
		case Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK:
			task = (TaskTwoTextBoxes)task;
			break;
		}
		
		return task;
	}*/
	
	@Override
	public Task saveNew()throws DatabaseException, ValidationException{
		saveNewGeneralDataInDatabase();
		saveNewAdditionalData();
		
		return this;
	}
	
	@Override
	public void update() throws ValidationException, DatabaseException {
		databaseActionHandler.taskGenericUpdate(this);
		updateAdditionalData();
	}

	@Override
	public void delete() throws ValidationException, DatabaseException {
		databaseActionHandler.taskDelete(this.taskID);
		
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
	protected Task saveNewGeneralDataInDatabase() throws DatabaseException, ValidationException{
		Task savedTask = databaseActionHandler.taskValidateAndCreate(this);
		this.taskID = savedTask.getTaskID();
		
		return this;
	}
	
	protected abstract void saveNewAdditionalData() throws DatabaseException, ValidationException;
	
	/**In place so can be overridden by concrete classes to use for saving subclass-specific data
	 * @param taskWithNewData
	 * @return true if update successful, false if error
	 * @throws ValidationException 
	 * @throws DatabaseException 
	 */
	protected abstract boolean updateAdditionalData() throws DatabaseException, ValidationException;

	
	/**Copies the task, setting the taskID to 0 and template=false since templates are unique.
	 * @param stageID
	 * @param userID
	 * @return
	 */
	public abstract Task copy();
	
	/**Copies the task and then saves it in the database.  Sets the taskID to 0 and replaces stageID and userID with supplied arguments. Also sets template=false since templates are unique.
	 * @param stageID
	 * @param userID
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public abstract Task copyAndSave(int stageID, int userID) throws DatabaseException, ValidationException;
	
	public List<Task> copyMultiple(int stageID, int userID, int numberOfCopies) throws CloneNotSupportedException, DatabaseException, ValidationException{
		List<Task> listOfCopiedTasks = new ArrayList<>();
		for(int i = 0; i < numberOfCopies; i++){
			listOfCopiedTasks.add(copyAndSave(stageID, userID));
		}
		
		return listOfCopiedTasks;
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
	
	public void setDateCompleted(LocalDateTime date){
		dateCompleted = date;
	}
	
	public LocalDateTime getDateCompleted(){
		return dateCompleted;
	}
	
	public int getTaskOrder() {
		return taskOrder;
	}

	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}
	
	/**Since taskOrder is based off List indexes, it starts with 0.  So for displaying the order to users on the front end, add 1 so
	 *the order values start with 1.
	 * @return
	 */
	public int getTaskOrderForUserDisplay(){
		return taskOrder + 1;
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
		dateCompleted = LocalDateTime.now();
	}

	@Override
	public void markIncomplete() {
		completed = false;
	}

	@Override
	public double getPercentComplete() {
		if (isCompleted()) {
			return 1;
		} else {
			return 0;
		}
		//return (int)(((double)repetitionsCompleted/(double)repetitions) * 100);
	}

	public String getTaskTypeName(){
		return this.getClass().getSimpleName();
	}

	public void transferGeneralData(Task taskWithNewData) throws ValidationException, DatabaseException {
		//update all universal properties that can be modified by user
		if(!this.isCompleted() && taskWithNewData.isCompleted()){
			this.setCompleted(taskWithNewData.isCompleted());
			this.setDateCompleted(taskWithNewData.getDateCompleted());
		} else if(this.isCompleted() && !taskWithNewData.isCompleted()){
			this.setCompleted(taskWithNewData.isCompleted());
			this.setDateCompleted(null);
		}
		

		//update case-specific properties
		transferAdditionalData(taskWithNewData);
		
		//update in database
		update();		
	}
	
	public abstract void transferAdditionalData(Task taskWithNewData);

}
