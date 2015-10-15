package com.cggcoding.utils.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.StageGoal;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.tasktypes.GenericTask;

public interface DatabaseActionHandler {

	Connection getConnection();

	//**************************************************
	// *************** Login Methods *******************
	//**************************************************

	boolean userValidate(String email, String password) throws DatabaseException;

	User userLoadInfo(String email, String password) throws DatabaseException;

	//**************************************************************************************************
	//****************************** Treatment Plan Methods *************************************
	//**************************************************************************************************

	TreatmentPlan treatmentPlanValidateAndCreateBasic(TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException;
	
	TreatmentPlan treatmentPlanLoadWithEmpyLists(int treatmentPlanID) throws DatabaseException;
	
	List<Integer> treatmentPlanGetStageIDs(int treatmentPlanID) throws DatabaseException;
	
	List<TreatmentPlan> treatmentPlanGetDefaults() throws DatabaseException;

	//**************************************************************************************************
	//****************************************** Stage Methods *****************************************
	//**************************************************************************************************
	
	/**Validates and if passes, creates stage.  If the Stage.isTemplate=true, then the treatmentPlanID foreign key is set to null before inserting into the database.
	 * @param stage
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	Stage stageValidateAndCreate(Stage stage) throws ValidationException, DatabaseException;
	
	/* Removed so could make them private and wrap in stageTemplateValidateAndCreate and reduce number of connections opened
	private boolean stageValidateNewName(Connection cn, Stage newStage) throws ValidationException, DatabaseException;
	
	Stage stageTemplateCreate(Connection cn, Stage newStageTemplate) throws ValidationException, DatabaseException;
	*/
	
	boolean stageUpdate(Stage newStageTemplate) throws ValidationException, DatabaseException;
	
	Stage stageLoadWithEmplyLists(int stageID) throws DatabaseException, ValidationException;
	
	List<Stage> stagesGetDefaults() throws DatabaseException;
	
	StageGoal stageGoalValidateAndCreate(StageGoal goal) throws DatabaseException;
	
	Map<Integer, Integer> stageGetTaskIDTypeMap(int stageID) throws DatabaseException;
	
	List<StageGoal> stageLoadGoals(int stageID) throws DatabaseException;

	//**************************************************************************************************
	//*************************************** Treatment Issue Methods **********************************
	//**************************************************************************************************

	TreatmentIssue treatmentIssueValidateAndCreate(TreatmentIssue treatmentIssue, int userID) throws ValidationException, DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetDefaults() throws DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetListByUserID(int userID) throws DatabaseException;

	//**************************************************************************************************
	//*************************************** Task Methods **********************************
	//**************************************************************************************************
	List<Task> taskGetDefaults() throws DatabaseException;
	
	/**Validates and if passes, creates task.  If the Task.isTemplate=true, then the stageID foreign key is set to null before inserting into the database.
	 * @param newTask
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	Task taskValidateAndCreate(Task newTask) throws DatabaseException, ValidationException;
	
	Task taskGenericLoad(int taskID) throws DatabaseException;
	
	Task taskTwoTextBoxesLoad(int taskID) throws DatabaseException;
	
	/**Updates task with new data.  If taskToUpdate.isTemplate == true, then stageID foreign key is set to null before inserting
	 * @param taskToUpdate
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	boolean taskGenericUpdate(Task taskToUpdate) throws DatabaseException, ValidationException;
	
	Map<Integer, String> taskTypesLoad() throws DatabaseException;
	

}