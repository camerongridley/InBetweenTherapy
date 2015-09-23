package com.cggcoding.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;


public class DefaultDatabaseCalls {
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	
	public DefaultDatabaseCalls() {

	}
	
	//TODO this could be a static method that really should go somewhere else like User?
	public static List<Stage> getDefaultStages() throws DatabaseException{
		return databaseActionHandler.stagesGetDefaults();
	}
	
	public static List<Task> getDefaultTasks() throws DatabaseException{
		return databaseActionHandler.taskGetDefaults();
	}
	
	public static Task getGenericTaskByID(int taskID) throws DatabaseException{
		return databaseActionHandler.taskGenericLoad(taskID);
	}

	public static Stage getDefaultStageByID(int selectedDefaultStageID) throws DatabaseException, ValidationException {
		return databaseActionHandler.stageLoad(selectedDefaultStageID);
	}
	
	public static Map<Integer, String> getTaskTypeMap() throws DatabaseException {
		return databaseActionHandler.taskTypesLoad();
	}
	
	public static ArrayList<TreatmentIssue> getDefaultTreatmentIssues() throws DatabaseException {
		return databaseActionHandler.treatmentIssueGetDefaults();
	}
}
