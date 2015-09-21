package com.cggcoding.models.tasktypes;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Task;
import com.cggcoding.models.Updateable;

public class GenericTask extends Task implements Updateable {

	private GenericTask(){
	}
	private GenericTask(int taskID, int userID, int parentTaskID, String title, String instructions) {
		super(taskID, userID, parentTaskID, title, instructions);
	}

	public GenericTask(int taskID, int userID, String title, String instructions) {
		super(taskID, userID, title, instructions);
	}

	private GenericTask(int taskID, int userID) {
		super(taskID, userID);
	}
	

	private GenericTask(int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			int taskOrder, boolean extraTask, boolean template) {
		//super(userID, taskTypeID, parentTaskID, title, instructions, resourceLink, extraTask, template);
		super(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template);

	}

	/**This class is a concretized version of Task to map up with the GenericTask table in the database.  There is no "Task" table in the database
	 * due to the database design choice.
	 * @param taskID
	 * @param userID
	 * @param title
	 * @param instructions
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	public static GenericTask saveGenericTemplateInDatabase(int userID, int taskTypeID, String title, String instructions, 
			String resourceLink, boolean extraTask) throws DatabaseException, ValidationException{
		GenericTask task = new GenericTask();
		//since this is to create a template, which is therefore independent of any stage or treatment plan, parameters for stageID, parentTaskID and taskOrder are set to 0
		//Additionally other parameters receive defaults: taskID = 0 since it has not been generated by the database, completed = false, dateCompleted = null, template=true
		return (GenericTask)task.saveGeneralDataForTemplateInDatabase(0, 0, userID, taskTypeID, 0, 
				title, instructions, resourceLink, false, null, 0, extraTask, true);

	}
	
	public static GenericTask getInstanceWithoutTaskID(int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			int taskOrder, boolean extraTask, boolean template){
		return new GenericTask(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template);
	}
	
	public static GenericTask getInstance(int taskID, int stageID, int userID, int taskTypeID, int parentTaskID, String title, String instructions, String resourceLink,
			int taskOrder, boolean extraTask, boolean template){
		return new GenericTask(stageID, userID, taskTypeID, parentTaskID, title, instructions, resourceLink, taskOrder, extraTask, template);
	}
	
}
