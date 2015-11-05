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
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;


/**This class is for any database calls to get lists and data associated with default values and used across different user roles
 * @author cgrid_000
 *
 */
public class DefaultDatabaseCalls {
	private static DatabaseActionHandler databaseActionHandler = new MySQLActionHandler();
	
	
	public DefaultDatabaseCalls() {

	}
	
	public static List<TreatmentPlan> getDefaultTreatmentPlans() throws DatabaseException, ValidationException {
		return databaseActionHandler.treatmentPlanGetDefaults();
	}
	
	public static List<Stage> getDefaultStages() throws DatabaseException, ValidationException{
		return databaseActionHandler.stagesGetDefaults();
	}
	
	public static List<Task> getDefaultTasks() throws DatabaseException{
		return databaseActionHandler.taskGetDefaults();
	}


	public static Map<Integer, String> getTaskTypeMap() throws DatabaseException {
		return databaseActionHandler.taskTypesLoad();
	}
	
	public static ArrayList<TreatmentIssue> getDefaultTreatmentIssues() throws DatabaseException {
		return databaseActionHandler.treatmentIssueGetDefaults();
	}
}
