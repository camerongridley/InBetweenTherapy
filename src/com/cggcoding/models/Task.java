package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;


/**
 * @author cgrid_000
 *
 */
public abstract class Task implements Serializable, Completable, DatabaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private int clientTaskOrder;
	private boolean extraTask;
	private boolean template;
	private int templateID;
	int clientRepetition;
	boolean disabled; //this property is not maintained in the database and is set upon or after loading.  It's purpose is to tell the view if assiciated control should be disabled
	
	private static DatabaseActionHandler dao= new MySQLActionHandler();
	
	//empty constructor necessary to allow static factory methods in subclasses
	protected Task(){
	}
	
	//basic parent Task that sets properties to defaults
	protected Task(int taskID, int userID) {
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
		this.clientTaskOrder = 0;
		this.extraTask = false;
		this.template = false;
		this.templateID = 0;
		this.clientRepetition = 1;

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
	 * @param clientTaskOrder
	 * @param extraTask
	 * @param template
	 */
	protected Task (int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink, int clientTaskOrder, boolean extraTask, boolean template, int templateID, int clientRepetition){
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
		this.clientTaskOrder = clientTaskOrder;
		this.extraTask = extraTask;
		this.template = template;
		this.templateID = templateID;
		this.clientRepetition = clientRepetition;
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
	 * @param clientTaskOrder
	 * @param extraTask
	 * @param template
	 */
	protected Task (int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink, boolean completed, LocalDateTime dateCompleted, int clientTaskOrder, boolean extraTask, boolean template, int templateID, int clientRepetition){
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
		this.clientTaskOrder = clientTaskOrder;
		this.extraTask = extraTask;
		this.template = template;
		this.templateID = templateID;
		this.clientRepetition = clientRepetition;
	}
	
	public static Task createTemplate(Task taskTemplate) throws ValidationException, DatabaseException{
		taskTemplate.setStageID(Constants.TEMPLATES_HOLDER_PRIMARY_KEY_ID);
		taskTemplate.setClientTaskOrder(Constants.TEMPLATE_ORDER_NUMBER);
		taskTemplate.setTemplate(true);
		
		taskTemplate.create();
		
		return taskTemplate;
	}
	
	public static Task load(int taskID) throws DatabaseException{
		Connection cn = null;
		Task task = null;

		try{
			cn = dao.getConnection();

			task = load(cn, taskID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }

		return task;

	}
	
	/**Loads a task.  
	 * CALLING METHOD IS REPOSONSIBLE FOR CLOSING THE CONNECTION PASSED TO THIS METHOD
	 * @param cn
	 * @param taskID
	 * @return
	 * @throws SQLException
	 */
	public static Task load(Connection cn, int taskID) throws SQLException {

		Task task = null;

		TaskGeneric genericTask = TaskGeneric.loadGeneric(cn, taskID);
		
		task = castGenericToType(genericTask);
		
		task.loadAdditionalData(cn, genericTask);

		return task;
	}
	
	protected abstract void loadAdditionalData(Connection cn, TaskGeneric genericTask) throws SQLException;
	
	
	/**Takes a TaskGeneric, looks at it's taskTypeID and based on that calls that Task subtype's convertFromGeneric() method.
	 * This is used primarily for when loading a task based solely on taskID and, therefore, the taskType would be unknown.
	 * Since TaskGeneric is the concretized version of Task, it is the main argument and has the actual taskTypeID set to that property.
	 * @param genericTask
	 * @return
	 */
	private static Task castGenericToType(TaskGeneric genericTask){
		Task task = null;
		switch(genericTask.getTaskTypeID()){
			case Constants.TASK_TYPE_ID_GENERIC_TASK:
				task = genericTask;
				break;
			case Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK:
				task = TaskTwoTextBoxes.convertFromGeneric(genericTask);
				break;
		}
		
		return task;
	}
	
	/**---Database Interaction an argument flag---
	 * Converts a Task from one type to another.  
	 * If updateDatabaseRows=false the sequence of events is: 
	 * 1) cast supplied Task argument to generic and set it's taskTypeID to the taskTypeToConvertTo argument
	 * 2) call castGenericToType
	 * 
	 * If updateDatabaseRows=true the sequence of events is: 
	 * 1) cast supplied Task argument to generic and set it's taskTypeID to the taskTypeToConvertTo argument
	 * 2) Task.deleteAdditionalData()
	 * 3) call castGenericToType
	 * 4) Task.createAdditionalData() - which will create all blank fields but allows for future updating
	 * @param task
	 * @param taskTypeIDtoConvertTo - the taskTypeID
	 * @param updateDataBaseRows
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public static Task convertToType(Task task, int taskTypeIDtoConvertTo, boolean updateDataBaseRows) throws DatabaseException, ValidationException{
		
		//if the taskTypeID of the task argument and taskTypeIDtoConvertTo are equal, then just pass the task back
		if(task.getTaskTypeID()!=taskTypeIDtoConvertTo){
			//castGenericToType only accepts TaskGenric objects so first convert 
			TaskGeneric genericTask = TaskGeneric.convertToGeneric(task);

			//set the typeID to convert to so castGenericToType knows what to cast to
			genericTask.setTaskTypeID(taskTypeIDtoConvertTo);
			
			if(updateDataBaseRows==false){
				//if type to convert to is TaskGeneric, then no further conversion is necessary; could eliminate this condition check if wanted, that would just mean the task was converted to generic twice
				if(taskTypeIDtoConvertTo != Constants.TASK_TYPE_ID_GENERIC_TASK){
					task = castGenericToType(genericTask);
				} else {
					task = genericTask;
				}

			} else {
				
				Connection cn = null;
		        
		        try {
		        	cn = dao.getConnection();
		        	cn.setAutoCommit(false);
		        	
		        	task.deleteAdditionalData(cn);
		        	
		        	//if type to convert to is TaskGeneric, then no further conversion is necessary
					if(taskTypeIDtoConvertTo != Constants.TASK_TYPE_ID_GENERIC_TASK){
						task = castGenericToType(genericTask);
					} else {
						task = genericTask;
					}
		        	
		        	task.createAdditionalData(cn);
		        	
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
			}
		}

		return task;
	}
	
	@Override
	public Task create()throws DatabaseException, ValidationException{
		Connection cn = null;
		Task task = null;

		try{
			cn = dao.getConnection();
			cn.setAutoCommit(false);
			
			task = create(cn);
			
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
		
		return task;
	}
	
	/**Creates a new task. CALLING METHOD IS REPOSONSIBLE FOR CLOSING THE CONNECTION PASSED TO THIS METHOD
	 * @param cn
	 * @return
	 * @throws ValidationException
	 * @throws SQLException
	 */
	protected Task create(Connection cn)throws ValidationException, SQLException{
		
		createGeneralData(cn);
		createAdditionalData(cn);
		
		return this;
	}
	
	@Override
	public void update() throws ValidationException, DatabaseException {
		Connection cn = null;
        
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	if(dao.taskValidate(cn, this)){	
        		update(cn);
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
	}
	
	protected void update(Connection cn) throws ValidationException, SQLException{
		dao.taskGenericUpdate(cn, this);
		updateAdditionalData(cn);
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
	
	protected void delete(Connection cn) throws SQLException {
		dao.taskDelete(cn, this.taskID);
	}
	
	public static void delete(int taskID) throws ValidationException, DatabaseException {
		Connection cn = null;

		try {
        	cn = dao.getConnection();
        	dao.taskDelete(cn, taskID);
            
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }	
	}
	
	
	/**Saves all of the fields in Task into the database table that holds the common fields for all tasks
	 * @param cn
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
	 * @param clientTaskOrder
	 * @param extraTask
	 * @param template
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 * @throws SQLException 
	 */
	protected Task createGeneralData(Connection cn) throws ValidationException, SQLException{
		
		//basic validation of the new task
		if(this.getTitle() == null || this.getTitle().isEmpty() || 
				this.getInstructions() == null || this.getInstructions().isEmpty() ||
						this.getTaskTypeID() == 0){
			throw new ValidationException(ErrorMessages.TASK_MISSING_INFO);
		}
		
		//do any validation that requires a database call and if ok make db insert call
		if(dao.taskValidate(cn, this)){
			Task savedTask = dao.taskGenericCreate(cn, this);
			this.taskID = savedTask.getTaskID();
		}

		return this;
	}
	
	protected abstract void createAdditionalData(Connection cn) throws ValidationException, SQLException;
	
	/**In place so can be overridden by concrete classes to use for saving subclass-specific data
	 * @param cn
	 * @param taskWithNewData
	 * @return true if update successful, false if error
	 * @throws ValidationException 
	 * @throws SQLException
	 */
	protected abstract boolean updateAdditionalData(Connection cn) throws ValidationException, SQLException;

	protected abstract void deleteAdditionalData(Connection cn) throws ValidationException, SQLException;
	
	/**Copies the task, setting the taskID to 0 and template=false since templates are unique. DOES NOT SAVE TO DATABASE.
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
	
	public int getClientTaskOrder() {
		return clientTaskOrder;
	}

	public void setClientTaskOrder(int clientTaskOrder) {
		this.clientTaskOrder = clientTaskOrder;
	}

	public boolean isExtraTask() {
		return extraTask;
	}

	public void setExtraTask(boolean extraTask) {
		this.extraTask = extraTask;
	}
	
	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}

	public int getClientRepetition() {
		return clientRepetition;
	}

	public void setClientRepetition(int clientRepetition) {
		this.clientRepetition = clientRepetition;
	}

	public boolean isTemplate() {
		return template;
	}

	public void setTemplate(boolean isTemplate) {
		this.template = isTemplate;
	}

	public void setCompleted(boolean status){
		this.completed = status;
	}

	public boolean isParentTask(){
		return parentTaskID == 0;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getDateCompletedFormatted(){
		if(dateCompleted != null){
			String amPM = " AM";
			StringBuilder dateBuilder = new StringBuilder();
			dateBuilder.append(dateCompleted.getMonthValue());
			dateBuilder.append("/");
			dateBuilder.append(dateCompleted.getDayOfMonth());
			dateBuilder.append("/");
			dateBuilder.append(dateCompleted.getYear());
			dateBuilder.append(" ");
			int hour = dateCompleted.getHour();
			if(hour>12){
				hour = hour - 12;
				amPM = " PM";
			}
			dateBuilder.append(hour);
			dateBuilder.append(":");
			
			//getMinutes() returns a single digit for values less than 10, so here we add a leading 0 to account for the first m in the format hh:mm
			int minutes = dateCompleted.getMinute();
			if(minutes < 10){
				dateBuilder.append("0" + minutes);
			}else{
				dateBuilder.append(minutes);
			}
			
			dateBuilder.append(amPM);
			
			
			return dateBuilder.toString();
		}else{
			return null;
		}
		
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

	public static List<Task> getCoreTasks() throws DatabaseException{
		return dao.taskGetCoreList();
	}
	
	public static Map<Integer, String> getTaskTypeMap() throws DatabaseException {
		return dao.taskTypesLoad();
	}
}
