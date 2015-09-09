package com.cggcoding.utils.database;

import java.sql.Connection;
import java.util.ArrayList;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;

public interface DatabaseActionHandler {

	Connection getConnection();

	/**************************************************
	 *************** Login Methods ********************
	 **************************************************/

	boolean validateUser(String email, String password) throws DatabaseException;

	User getUserInfo(String email, String password) throws DatabaseException;

	/**************************************************************************************************
	 ****************************** Treatment Plan Methods *************************************
	 **************************************************************************************************/

	TreatmentPlan createTreatmentPlanBasic(TreatmentPlan treatmentPlan) throws ValidationException, DatabaseException;

	/**************************************************************************************************
	 ****************************************** Stage Methods *****************************************
	 **************************************************************************************************/

	Stage createStageTemplate(Stage newStageTemplate) throws ValidationException, DatabaseException;

	/**************************************************************************************************
	 *************************************** Treatment Issue Methods **********************************
	 **************************************************************************************************/

	TreatmentIssue createTreatmentIssue(TreatmentIssue treatmentIssue) throws ValidationException, DatabaseException;

	ArrayList<TreatmentIssue> getDefaultTreatmentIssues(int adminUserID) throws DatabaseException;

	ArrayList<TreatmentIssue> getTreatmentIssuesList(int userID) throws DatabaseException;

}