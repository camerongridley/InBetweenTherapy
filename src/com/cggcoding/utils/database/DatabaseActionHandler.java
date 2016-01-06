package com.cggcoding.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.Stage;
import com.cggcoding.models.StageGoal;
import com.cggcoding.models.StageTaskDetail;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.TaskTwoTextBoxes;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;

public interface DatabaseActionHandler {

	Connection getConnection() throws DatabaseException;

	//**************************************************
	// *************** Login Methods *******************
	//**************************************************

	boolean userValidate(String email, String password) throws DatabaseException;

	User userLoadInfo(String email, String password) throws DatabaseException;
	
	//**************************************************
	// *************** User Methods *******************
	//**************************************************
	public Map<Integer, UserClient> userGetClientsByTherapistID(int therapistID) throws DatabaseException;
	
	List<TreatmentPlan> userGetClientTreatmentPlans(int clientUserID, boolean inProgress, boolean isCompleted)
			throws DatabaseException, ValidationException;
	
	List<TreatmentPlan> userGetTherapistAssignedPlans(int clientUserID, int assignedByUserID)
			throws DatabaseException, ValidationException;

	//**************************************************************************************************
	//****************************** Treatment Plan Methods *************************************
	//**************************************************************************************************

	TreatmentPlan treatmentPlanLoadBasic(Connection cn, int treatmentPlanID) throws SQLException, ValidationException;

	List<Stage> treatmentPlanLoadStages(Connection cn, int treatmentPlanID) throws SQLException, ValidationException;

	boolean treatmentPlanValidateNewTitle(Connection cn, int userID, String planTitle)
			throws ValidationException, SQLException;

	TreatmentPlan treatmentPlanCreateBasic(Connection cn, TreatmentPlan treatmentPlan)
			throws SQLException, ValidationException;

	boolean treatmentPlanValidateUpdatedTitle(Connection cn, TreatmentPlan treatmentPlan)
			throws ValidationException, SQLException;

	void treatmentPlanUpdateBasic(Connection cn, TreatmentPlan treatmentPlan) throws SQLException, ValidationException;
	
	/**Gets all of the "default" TreatmentPlans, which means it returns all the the TreatmentPlans that have been created by an Admin user and can be copied for use by Therapist and Client users.
	 * It returns all TreatmentPlans that are owned by users of type Admin and have isTemplate as true.  It specifically excludes TreatmentPlan with id=0 since that TreatmentPlan is designated
	 * as a holder for all default Stages and is not eligible for copying or use by other user types.
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException 
	 */
	List<TreatmentPlan> treatmentPlanGetDefaults() throws DatabaseException, ValidationException;
	
	void treatmentPlanDelete(Connection cn, int treatmentPlanID) throws SQLException, ValidationException;
	
	List<Stage> treatmentPlanLoadStageTemplates(Connection cn, int treatmentPlanID) throws SQLException, ValidationException;
	
	List<Stage> treatmentPlanUpdateStageTemplates(Connection cn, int treatmentPlanID, List<Stage> stageTemplates)
			throws SQLException;
	
	//**************************************************************************************************
	//****************************************** Stage Methods *****************************************
	//**************************************************************************************************
	
	List<StageGoal> stageLoadGoals(Connection cn, int stageID) throws SQLException, ValidationException;

	List<Task> stageLoadTasks(Connection cn, int stageID) throws SQLException;

	Stage stageLoadBasic(Connection cn, int stageID) throws SQLException, ValidationException;
	
	boolean stageValidateNewTitle(Connection cn, Stage newStage) throws ValidationException, SQLException;

	Stage stageCreateBasic(Connection cn, Stage newStage) throws ValidationException, SQLException;

	boolean stageValidateUpdatedTitle(Connection cn, Stage newStage) throws ValidationException, SQLException;

	boolean stageUpdateBasic(Connection cn, Stage stage) throws ValidationException, SQLException;
	
	/**Gets all of the "default" Stages, which means it returns all the the Stages that have been created by an Admin user and can be copied for use by Therapist and Client users.
	 * It returns all Stages that are owned by users of type Admin and have isTemplate as true.  It specifically excludes Stage with id=0 since that Stage is designated
	 * as a holder for all default Tasks and is not eligible for copying or use by other user types.
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException 
	 */
	List<Stage> stagesGetDefaults() throws DatabaseException, ValidationException;
	
	List<Task> stageLoadTaskTemplates(Connection cn, int stageID) throws SQLException;
	
	List<Task> stageUpdateTaskTemplates(Connection cn, int stageID, List<Task> taskTemplates) throws SQLException;
	
	//**************************************************************************************************
	//*************************************** Stage Goal Methods ***************************************
	//**************************************************************************************************
	StageGoal stageGoalCreate(Connection cn, StageGoal stageGoal) throws SQLException, ValidationException;
	
	void stageDelete(Connection cn, int stageID) throws SQLException, ValidationException;

	boolean stageGoalUpdate(Connection cn, StageGoal goal) throws ValidationException, SQLException;

	void stageGoalDelete(Connection cn, int stageGoalID) throws SQLException, ValidationException;
	
	
	//**************************************************************************************************
	//*************************************** StageTaskDetail Methods ***************************************
	//**************************************************************************************************
	Map<Integer, StageTaskDetail> stageTaskDetailsLoad(Connection cn, int stageID)
			throws SQLException, ValidationException;

	//**************************************************************************************************
	//*************************************** Treatment Issue Methods **********************************
	//**************************************************************************************************

	TreatmentIssue treatmentIssueCreate(Connection cn, TreatmentIssue treatmentIssue, int userID) throws ValidationException, SQLException;

	boolean treatmentIssueValidateNewName(Connection cn, String issueName, int userID)
			throws ValidationException, SQLException;
	
	boolean treatmentIssueValidateUpdatedName(Connection cn, TreatmentIssue issue)
			throws ValidationException, SQLException;
	
	ArrayList<TreatmentIssue> treatmentIssueGetDefaults() throws DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetListByUserID(int userID) throws DatabaseException;

	boolean treatmentIssueUpdate(Connection cn, TreatmentIssue issue) throws ValidationException, SQLException;

	void treatmentIssueDelete(Connection cn, int treatmentIssueID) throws SQLException, ValidationException;

	//**************************************************************************************************
	//*************************************** Task Methods **********************************
	//**************************************************************************************************
	List<Task> taskGetDefaults() throws DatabaseException;
	
	Task taskGenericLoad(Connection cn, int taskID) throws SQLException;
	
	Map<Integer, String> taskTypesLoad() throws DatabaseException;

	void taskDelete(Connection cn, int taskID) throws SQLException;

	Task taskTwoTextBoxesLoadAdditionalData(Connection cn, TaskGeneric genericTask) throws SQLException;

	void taskTwoTextBoxesCreateAdditionalData(Connection cn, TaskTwoTextBoxes twoTextBoxesTask) throws SQLException;

	boolean taskValidate(Connection cn, Task newTask) throws ValidationException, SQLException;

	Task taskGenericCreate(Connection cn, Task newTask) throws SQLException;

	boolean taskGenericUpdate(Connection cn, Task taskToUpdate) throws SQLException;

	boolean taskTwoTextBoxesUpdateAdditionalData(Connection cn, TaskTwoTextBoxes twoTextBoxesTask)
			throws SQLException, ValidationException;

	
	
	
	//**************************************************************************************************
	//*************************************** Misc Methods **********************************
	//**************************************************************************************************
	boolean throwValidationExceptionIfTemplateHolderID(int templateHolderObjectID) throws ValidationException;

	boolean throwValidationExceptionIfNull(Object o) throws ValidationException;

	void mapsTaskStageTemplateCreate(Connection cn, int taskTemplateID, int stageTemplateID, int taskOrder) throws SQLException;

	boolean mapsTaskStageTemplateValidate(Connection cn, int taskTemplateID, int stageTemplateID)
			throws ValidationException, SQLException;

	void mapsStageTreatmentPlanTemplateCreate(Connection cn, int stageTemplateID, int treatmentPlanTemplateID, int stageOrder)
			throws SQLException;

	boolean mapsStageTreatmentPlanTemplateValidate(Connection cn, int stageTemplateID, int treatmentPlanTemplateID)
			throws ValidationException, SQLException;

	void mapsTaskStageTemplateDelete(Connection cn, int taskID) throws SQLException;

	void mapsStageTreatmentPlanTemplateDelete(Connection cn, int stageID) throws SQLException;

	

	

	

	

	


	







	

	

	




	

	

	
	

}