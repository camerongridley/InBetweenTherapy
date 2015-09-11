package com.cggcoding.utils.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
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

	TreatmentPlan treatmentPlanCreateBasic(TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException;

	//**************************************************************************************************
	//****************************************** Stage Methods *****************************************
	//**************************************************************************************************
	boolean stageValidateNewName(String stageName, int userID) throws ValidationException, DatabaseException;
	
	Stage stageTemplateCreate(Stage newStageTemplate) throws ValidationException, DatabaseException;
	
	boolean stageTemplateUpdate(Stage newStageTemplate) throws ValidationException, DatabaseException;
	
	Stage stageLoad(int userID, int stageID) throws DatabaseException, ValidationException;
	
	Stage stageLoad(int selectedDefaultStageID) throws DatabaseException, ValidationException;
	
	List<Stage> stagesGetDefaults() throws DatabaseException;
	
	

	//**************************************************************************************************
	//*************************************** Treatment Issue Methods **********************************
	//**************************************************************************************************

	TreatmentIssue treatmentIssueCreate(TreatmentIssue treatmentIssue) throws ValidationException, DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetDefaults(int adminUserID) throws DatabaseException;

	ArrayList<TreatmentIssue> treatmentIssueGetListByUserID(int userID) throws DatabaseException;

	

	

}