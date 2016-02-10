package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
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
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	
	/**
	 * Default constructor.  Designated public so Task.getTaskType() can be accessed outside of the package.
	 */
	public TaskTwoTextBoxes(){
		super();
	}
	
	private TaskTwoTextBoxes(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title,
			String instructions, String resourceLink, boolean completed, LocalDateTime dateCompleted, int clientTaskOrder,
			boolean extraTask, boolean template, int templateID, int clientRepetition,
			String extraTextLabel1, String extraTextValue1,
			String extraTextLabel2, String extraTextValue2) {
		super(taskID, stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, completed,
				dateCompleted, clientTaskOrder, extraTask, template, templateID, clientRepetition);
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
			String resourceLink, boolean completed, LocalDateTime dateCompleted, int clientTaskOrder, boolean extraTask, boolean template, int templateID, int clientRepetition,
			String extraTextLabel1, String extraTextValue1,
			String extraTextLabel2, String extraTextValue2){
		
		return new TaskTwoTextBoxes(taskID, stageID, userID, taskTypeID, parentTaskID, title,instructions, 
			resourceLink, completed, dateCompleted, clientTaskOrder, extraTask, template, templateID, clientRepetition,
			extraTextLabel1, extraTextValue1,
			extraTextLabel2, extraTextValue2);
	}
	
	/**Converts a TaskGeneric object to TaskTwoTextBoxes.  Changes the TaskTypeID to Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK and transfers all other common fields from the generic task.
	 * @param genericTask
	 * @param extraTextLabel1
	 * @param extraTextValue1
	 * @param extraTextLabel2
	 * @param extraTextValue2
	 * @return
	 */
	public static TaskTwoTextBoxes addDataToGenericTask(TaskGeneric genericTask, String extraTextLabel1, String extraTextValue1,String extraTextLabel2, String extraTextValue2){
		return new TaskTwoTextBoxes(genericTask.getTaskID(), genericTask.getStageID(), genericTask.getUserID(), Constants.TASK_TYPE_ID_TWO_TEXTBOXES_TASK, genericTask.getParentTaskID(), genericTask.getTitle(),
				genericTask.getInstructions(), genericTask.getResourceLink(), genericTask.isCompleted(), genericTask.getDateCompleted(), genericTask.getClientTaskOrder(),
				genericTask.isExtraTask(), genericTask.isTemplate(), genericTask.getTemplateID(), genericTask.getClientRepetition(),
				extraTextLabel1, extraTextValue1,
				extraTextLabel2, extraTextValue2);
	}
	
	public static Task convertFromGeneric(TaskGeneric genericTask){
		return addDataToGenericTask(genericTask, null, null, null, null);
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

	@Override
	protected void createAdditionalData(Connection cn) throws ValidationException, SQLException{
		dao.taskTwoTextBoxesCreateAdditionalData(cn, this);
	}
	
	@Override
	protected boolean updateAdditionalData(Connection cn) throws ValidationException, SQLException {
		return dao.taskTwoTextBoxesUpdateAdditionalData(cn, this);
	}
	
	@Override
	protected void deleteAdditionalData(Connection cn) throws ValidationException, SQLException {
		dao.taskTwoTextBoxesDeleteAdditionalData(cn,super.getTaskID());
		
	}

	@Override
	protected void loadAdditionalData(Connection cn, TaskGeneric genericTask) throws SQLException {
		transferAdditionalData(dao.taskTwoTextBoxesLoadAdditionalData(cn, genericTask));
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
					isCompleted(), getDateCompleted(), getClientTaskOrder(), isExtraTask(), false, getTemplateID(), getClientRepetition(),
					extraTextLabel1, extraTextValue1, extraTextLabel2, extraTextValue2);
		
		return task;
	}
	
	@Override
	public Task copyAndSave(int stageID, int userID) throws DatabaseException, ValidationException {
		TaskTwoTextBoxes task =  (TaskTwoTextBoxes)copy();
		task.setStageID(stageID);
		task.setUserID(userID);
		
		return task.create();
	}

	


}
