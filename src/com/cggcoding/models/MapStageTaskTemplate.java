package com.cggcoding.models;

import java.sql.Connection;
import java.sql.SQLException;

import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

/**This class is always to be accessed through a Stage object since it is an extension of a Stage's functions
 * @author cgrid
 *
 */
public class MapStageTaskTemplate {

	private int stageID;
	private int taskID;
	private int taskOrder;
	private int templateRepetitions;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	
	public MapStageTaskTemplate(int stageID, int taskID, int taskOrder, int templateRepetitions) {
		this.stageID = stageID;
		this.taskID = taskID;
		this.taskOrder = taskOrder;
		this.templateRepetitions = templateRepetitions;
	}


	public int getStageID() {
		return stageID;
	}


	public void setStageID(int stageID) {
		this.stageID = stageID;
	}


	public int getTaskID() {
		return taskID;
	}


	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}


	public int getTaskOrder() {
		return taskOrder;
	}


	public void setTaskOrder(int taskOrder) {
		this.taskOrder = taskOrder;
	}


	public int getTemplateRepetitions() {
		return templateRepetitions;
	}


	public void setTemplateRepetitions(int templateRepetitions) {
		this.templateRepetitions = templateRepetitions;
	}
	
	protected void create(Connection cn) throws SQLException{
		dao.mapStageTaskTemplateCreate(cn, this);
	}
	

}
