package com.cggcoding.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class TaskTwoTextBoxes extends Task implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String extraTextLabel1;
	private String extraTextValue1;
	private String extraTextLabel2;
	private String extraTextValue2;
	
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	public TaskTwoTextBoxes(){
		super();
	}
	
	private TaskTwoTextBoxes(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title,
			String instructions, String resourceLink, boolean completed, LocalDateTime dateCompleted, int taskOrder,
			boolean extraTask, boolean template, int templateID, int repetitions,
			String extraTextLabel1, String extraTextValue1,
			String extraTextLabel2, String extraTextValue2) {
		super(taskID, stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, completed,
				dateCompleted, taskOrder, extraTask, template, templateID, repetitions);
		this.extraTextLabel1 = extraTextLabel1;
		this.extraTextValue1 = extraTextValue1;
		this.extraTextLabel2 = extraTextLabel2;
		this.extraTextValue2 = extraTextValue2;
	}
	
	private TaskTwoTextBoxes(int taskID, int userID){
		super(taskID, userID);
		this.extraTextLabel1 = "";
		this.extraTextValue1 = "";
		this.extraTextLabel2 = "";
		this.extraTextValue2 = "";
	}
	
	public static TaskTwoTextBoxes getInstanceBareBones(int taskID, int userID){
		return new TaskTwoTextBoxes(taskID, userID);
	}
	
	public static TaskTwoTextBoxes getInstanceFull(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title,	String instructions, 
			String resourceLink, boolean completed, LocalDateTime dateCompleted, int taskOrder,boolean extraTask, boolean template, int templateID, int repetitions,
			String extraTextLabel1, String extraTextValue1,
			String extraTextLabel2, String extraTextValue2){
		
		return new TaskTwoTextBoxes(taskID, stageID, userID, taskTypeID, parentTaskID, title,instructions, 
			resourceLink, completed, dateCompleted, taskOrder, extraTask, template, templateID, repetitions,
			extraTextLabel1, extraTextValue1,
			extraTextLabel2, extraTextValue2);
	}
	
	public static TaskTwoTextBoxes addDataToGenericTask(TaskGeneric genericTask, String extraTextLabel1, String extraTextValue1,String extraTextLabel2, String extraTextValue2){
		return new TaskTwoTextBoxes(genericTask.getTaskID(), genericTask.getStageID(), genericTask.getUserID(), genericTask.getTaskTypeID(), genericTask.getParentTaskID(), genericTask.getTitle(),
				genericTask.getInstructions(), genericTask.getResourceLink(), genericTask.isCompleted(), genericTask.getDateCompleted(), genericTask.getTaskOrder(),
				genericTask.isExtraTask(), genericTask.isTemplate(), genericTask.getTemplateID(), genericTask.getRepetitions(),
				extraTextLabel1, extraTextValue1,
				extraTextLabel2, extraTextValue2);
	}
	
	public String getExtraTextLabel1() {
		return extraTextLabel1;
	}

	public void setExtraTextLabel1(String extraTextLabel1) {
		this.extraTextLabel1 = extraTextLabel1;
	}

	public String getExtraTextValue1() {
		return extraTextValue1;
	}

	public void setExtraTextValue1(String extraTextValue1) {
		this.extraTextValue1 = extraTextValue1;
	}

	public String getExtraTextLabel2() {
		return extraTextLabel2;
	}

	public void setExtraTextLabel2(String extraTextLabel2) {
		this.extraTextLabel2 = extraTextLabel2;
	}

	public String getExtraTextValue2() {
		return extraTextValue2;
	}

	public void setExtraTextValue2(String extraTextValue2) {
		this.extraTextValue2 = extraTextValue2;
	}

	/*public static Task load(int taskID) throws DatabaseException {
		return databaseActionHandler.taskTwoTextBoxesLoad(taskID);
		//this method currently uses a SQL query that joins the generic and two-textbox table so can do in one call.  
		//If wanted to break it up into separate calls then would probably load general data here and then call loadAdditionalData() 
	}*/
	
	//XXX see note for loadAdditionalData()
	@Override
	protected void saveNewAdditionalData() throws DatabaseException, ValidationException{
		//databaseActionHandler.taskTwoTextBoxesSaveNewAdditionalData(this);
	}
	
	@Override
	protected boolean updateAdditionalData() throws DatabaseException, ValidationException {
		return databaseActionHandler.taskTwoTextBoxesUpdateAdditionalData(this);
	}

	@Override
	public Task loadAdditionalData() {
		/*XXX - this is doing nothing now and is not ever called.  If I change the DAO so that the connection is passed 
		 * around the models, then I will need to update this so there is a call to the TwoTextBoxes db table 
		 * here and the load for this is a 2-step process vs being a one-step process using a join in the SQL*/
		return this;

	}
	
	@Override
	public void transferAdditionalData(Task taskWithNewData) {
			TaskTwoTextBoxes newData = (TaskTwoTextBoxes)taskWithNewData;

			this.setExtraTextLabel1(newData.getExtraTextLabel1());
			this.setExtraTextValue1(newData.getExtraTextValue1());
			this.setExtraTextLabel2(newData.getExtraTextLabel2());
			this.setExtraTextValue2(newData.getExtraTextValue2());
		
	}

	@Override
	public Task copy(){
		TaskTwoTextBoxes task =  getInstanceFull(0, getStageID(), getUserID(), getTaskTypeID(), getParentTaskID(), getTitle(), getInstructions(), getResourceLink(), 
					isCompleted(), getDateCompleted(), getTaskOrder(), isExtraTask(), false, getTemplateID(), getRepetitions(),
					extraTextLabel1, extraTextValue1, extraTextLabel2, extraTextValue2);
		
		return task;
	}
	
	@Override
	public Task copyAndSave(int stageID, int userID) throws DatabaseException, ValidationException {
		TaskTwoTextBoxes task =  (TaskTwoTextBoxes)copy();
		
		return task.saveNew();
	}


}
