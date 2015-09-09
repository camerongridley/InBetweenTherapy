package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

public class UserAdmin extends User{
	private List<TreatmentIssue> customTreatmentIssues;

	public UserAdmin(int userID, String email) {
		super(userID, email);
		this.customTreatmentIssues = new ArrayList<>();
	}

	public ArrayList<TreatmentIssue> getDefaultTreatmentIssues(DataSource datasource) throws DatabaseException {
		DatabaseActionHandler mysqlActionHandler = new MySQLActionHandler(datasource);
		return mysqlActionHandler.getDefaultTreatmentIssues(super.getUserID());
	}
	
	

}
