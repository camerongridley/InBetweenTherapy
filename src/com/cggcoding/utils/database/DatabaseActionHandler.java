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

	//**************************************************************************************************
	//****************************************** Stage Methods *****************************************
	//**************************************************************************************************
	
	Stage stageTemplateValidateAndCreate(Stage stageTemplate) throws ValidationException, DatabaseException;
	
	/* Removed so could make them private and wrap in stageTemplateValidateAndCreate and reduce number of connections opened
	private boolean stageValidateNewName(Connection cn, Stage newStage) throws ValidationException, DatabaseException;
	
	Stage stageTemplateCreate(Connection cn, Stage newStageTemplate) throws ValidationException, DatabaseException;
	*/
	
	boolean stageTemplateUpdate(Stage newStageTemplate) throws ValidationException, DatabaseException;
	
	Stage stageLoad(int stageID) throws DatabaseException, ValidationException;
	
	List<Stage> stagesGetDefaults() throws DatabaseException;
	
	StageGoal stageGoalValidateAndCreate(StageGoal goal) throws DatabaseException;

	//**************************************************************************************************
	//*************************************** Treatment Issue Methods **********************************
	//**************************************************************************************************

	TreatmentIssue treatmentIssueCreate(TreatmentIssue treatmentIssue) throws ValidationException, DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetDefaults() throws DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetListByUserID(int userID) throws DatabaseException;

	//**************************************************************************************************
	//*************************************** Task Methods **********************************
	//**************************************************************************************************
	
	Task taskTemplateValidateAndCreate(Task newTask) throws DatabaseException, ValidationException;
	
	
	Map<Integer, String> taskTypesLoad() throws DatabaseException;
	

}