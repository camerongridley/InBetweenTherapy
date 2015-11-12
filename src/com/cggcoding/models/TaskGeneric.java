package com.cggcoding.models;

import java.time.LocalDateTime;
import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class TaskGeneric extends Task{
	private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();
	
	public TaskGeneric(){
		super();
	}
	
	private TaskGeneric(int taskID, int userID) {
		super(taskID, userID);
	}

	//constructor without taskID - for Tasks objects that haven't been saved to database and don't have a taskID yet
	protected TaskGeneric(int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			int taskOrder, boolean extraTask, boolean template) {
		//super(userID, taskTypeID, parentTaskID, title, instructions, resourceLink, extraTask, template);
		super(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template);
	}
	
	//full constructor
	private TaskGeneric(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			boolean completed, LocalDateTime dateCompleted, int taskOrder, boolean extraTask, boolean template) {
		//super(userID, taskTypeID, parentTaskID, title, instructions, resourceLink, extraTask, template);
		super(taskID, stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, completed, dateCompleted, taskOrder, extraTask, template);
	}
	
	//Static Factory Methods
	public static TaskGeneric getInstanceByID(int taskID, int userID){
		return new TaskGeneric(taskID, userID);
	}
	
	public static TaskGeneric getInstanceWithoutTaskID(int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			int taskOrder, boolean extraTask, boolean template){
		return new TaskGeneric(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template);
	}
	
	public static TaskGeneric getInstanceFull(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			boolean completed, LocalDateTime dateCompleted, int taskOrder, boolean extraTask, boolean template){
		return new TaskGeneric(taskID, stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, completed, dateCompleted, taskOrder, extraTask, template);
	}	
	

	/**This class is a concretized version of Task to map up with the GenericTask table in the database.  There is no "Task" table in the database
	 * due to the database design choice. The factory method saves a GerericTask to the database. Since this is to create a template, which is 
	 * therefore independent of any stage or treatment plan, parameters for stageID, parentTaskID and taskOrder are set to 0.  Additionally other
	 * parameters receive defaults: taskID = 0 since it has not been generated by the database, completed = false, dateCompleted = null, template=true
	 * @param taskID
	 * @param userID
	 * @param title
	 * @param instructions
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public static TaskGeneric getTemplateInstance(int userID, int taskTypeID, String title, String instructions, 
			String resourceLink, boolean extraTask){
		return new TaskGeneric(0, 0, userID, taskTypeID, 0, 
				title, instructions, resourceLink, false, null, 0, extraTask, true);
	}
	
	/*public static Task load(int taskID) throws DatabaseException{
		return databaseActionHandler.taskGenericLoad(taskID);
	}*/
	
	/*@Override
	public Object saveNew() throws DatabaseException, ValidationException{
		Task savedTask = super.saveNewGeneralDataInDatabase();
		saveNewAdditionalData(); //this does nothing here but just putting in place for sake of consistency with other task types
		return savedTask;
	}
	
	@Override
	public void update() throws DatabaseException, ValidationException{
		super.updateDataInDatabase();
		//for other tasks call update method for additional data
	}
	
	@Override
	public void delete() throws ValidationException, DatabaseException {
		// TODO  implement method
		
	}

	@Override
	public List<Object> copy(int numberOfCopies) {
		// TODO  implement method
		return null;
	}
	*/
	@Override
	protected boolean updateAdditionalData () {
		return true;//there is no additional data in GenericTask to update
	}

	@Override
	public Task loadAdditionalData() {
		//there is no additional data to load for GenericTask
		return this;
	}

	@Override
	protected void saveNewAdditionalData() throws DatabaseException, ValidationException {
		//there is no additional data to save for GenericTask
		
	}

	@Override
	public Task copy(){
		TaskGeneric gTask = getInstanceFull(0, getStageID(), getUserID(), getTaskTypeID(), getParentTaskID(), getTitle(), getInstructions(), getResourceLink(), 
					isCompleted(), getDateCompleted(), getTaskOrder(), isExtraTask(), false);
		
		return gTask;

	}
	
	@Override
	public Task copyAndSave(int stageID, int userID)throws DatabaseException, ValidationException {
		TaskGeneric copy = (TaskGeneric) copy();
		copy.setStageID(stageID);
		copy.setUserID(userID);
		
		return copy.saveNew();
	}

	@Override
	public void transferAdditionalData(Task taskWithNewData) {
		//there is no additional data with this task
		
	}
	
	
}
