package com.cggcoding.helpers;

import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;


public class DefaultDatabaseCalls {
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	
	public DefaultDatabaseCalls() {

	}
	
	//TODO this could be a static method that really should go somewhere else
	public static List<Stage> getDefaultStages() throws DatabaseException{
		return databaseActionHandler.stagesGetDefaults();
	}
	

	public static Stage getDefaultStageByID(int selectedDefaultStageID) throws DatabaseException, ValidationException {
		return databaseActionHandler.stageLoad(selectedDefaultStageID);
	}
	
}
