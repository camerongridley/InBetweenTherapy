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

	//**************************************************************************************************
	//****************************** Treatment Plan Methods *************************************
	//**************************************************************************************************

	TreatmentPlan treatmentPlanValidateAndCreate(TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException;
	
	public TreatmentPlan treatmentPlanLoad(int treatmentPlanID) throws DatabaseException, ValidationException;
	
	/**Gets basic TreatmentPlan based on treatmentPlanID with none of it's lists (stages, etc.) populated.  If treatmentPlanID=1, return null because the TreatmentPlan.treatmentPlanID=1 is the TreatmentPlan that holds all Stage templates and should not ever be loaded.
	 * @param treatmentPlanID
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException 
	 */
	TreatmentPlan treatmentPlanLoadWithEmpyLists(int treatmentPlanID) throws DatabaseException, ValidationException;
	
	public TreatmentPlan treatmentPlanCopy(int userID, int treatmentPlanID, boolean isTemplate) throws ValidationException, DatabaseException;
	
	/**Gets all of the "default" TreatmentPlans, which means it returns all the the TreatmentPlans that have been created by an Admin user and can be copied for use by Therapist and Client users.
	 * It returns all TreatmentPlans that are owned by users of type Admin and have isTemplate as true.  It specifically excludes TreatmentPlan with id=0 since that TreatmentPlan is designated
	 * as a holder for all default Stages and is not eligible for copying or use by other user types.
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException 
	 */
	List<TreatmentPlan> treatmentPlanGetDefaults() throws DatabaseException, ValidationException;
	
	void treatmentPlanValidateAndUpdateBasic(TreatmentPlan treatmentPlan) throws DatabaseException, ValidationException;

	void treatmentPlanDelete(int treatmentPlanID) throws DatabaseException, ValidationException;
	
	void treatmentPlanDeleteStage(int stageID, List<Stage> stages) throws DatabaseException, ValidationException;

	//**************************************************************************************************
	//****************************************** Stage Methods *****************************************
	//**************************************************************************************************
	
	Stage stageLoad(int stageID) throws DatabaseException, ValidationException;
	
	/**Validates and if passes, creates stage.  If the Stage.isTemplate=true, then the treatmentPlanID foreign key is set to null before inserting into the database.
	 * @param stage
	 * @return
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	Stage stageValidateAndCreate(Stage stage) throws ValidationException, DatabaseException;
	
	boolean stageValidateAndUpdateBasic(Stage newStageTemplate) throws ValidationException, DatabaseException;
	
	/**Gets basic Stage based on stageID with none of it's lists (goals, tasks, etc.) populated.  If stageID=1, return null because the Stage.stageID=1 is the Stage that holds all Task templates and should not ever be loaded. 
	 * @param stageID
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	Stage stageLoadWithEmplyLists(int stageID) throws DatabaseException, ValidationException;
	
	/**Gets all of the "default" Stages, which means it returns all the the Stages that have been created by an Admin user and can be copied for use by Therapist and Client users.
	 * It returns all Stages that are owned by users of type Admin and have isTemplate as true.  It specifically excludes Stage with id=0 since that Stage is designated
	 * as a holder for all default Tasks and is not eligible for copying or use by other user types.
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException 
	 */
	List<Stage> stagesGetDefaults() throws DatabaseException, ValidationException;
	
	StageGoal stageGoalValidateAndCreate(StageGoal goal) throws DatabaseException, ValidationException;
	
	//List<Integer> stageGetTaskIDs(int stageID) throws DatabaseException, ValidationException;
	
	//List<StageGoal> stageLoadGoals(int stageID) throws DatabaseException, ValidationException;
	
	void stageDelete(int stageID) throws DatabaseException, ValidationException;

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
	
	Task taskLoad(int taskID) throws DatabaseException;
	
	Task taskGenericLoad(Connection cn, int taskID) throws SQLException;
	
	boolean taskTwoTextBoxesUpdateAdditionalData(TaskTwoTextBoxes twoTextBoxesTask) throws DatabaseException, ValidationException;
	
	/**Updates task with new data.  If taskToUpdate.isTemplate == true, then stageID foreign key is set to null before inserting
	 * @param taskToUpdate
	 * @return
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	boolean taskGenericUpdate(Task taskToUpdate) throws DatabaseException, ValidationException;
	
	Map<Integer, String> taskTypesLoad() throws DatabaseException;

	void taskDelete(int taskID) throws DatabaseException, ValidationException;

	Task taskTwoTextBoxesLoadAdditionalData(Connection cn, TaskGeneric genericTask) throws SQLException;

	

	

	




	

	

	
	

}