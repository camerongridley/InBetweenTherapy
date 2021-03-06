package com.cggcoding.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class UserAdmin extends User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TreatmentIssue> customTreatmentIssues;

	public UserAdmin(int userID, String userName, String firstName, String lastName, String email) {
		super(userID, userName, firstName, lastName, email);
		this.customTreatmentIssues = new ArrayList<>();
		setMainMenuURL(Constants.URL_ADMIN_MAIN_MENU);
	}

	@Override
	public boolean isAuthorizedForTreatmentPlan(int treatmentPlanID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAuthorizedForStage(int stageID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAuthorizedForTask(int taskID) {
		// TODO Auto-generated method stub
		return false;
	}

}
