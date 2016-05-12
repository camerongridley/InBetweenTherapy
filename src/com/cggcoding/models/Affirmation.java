package com.cggcoding.models;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public class Affirmation {

	private int affirmationID;
	private String affirmation;
	private int userID;
	
	private static DatabaseActionHandler dao= new MySQLActionHandler();
	
	public Affirmation(){
		
	}
	
	public Affirmation(int affirmationID, String affirmation, int userID) {
		super();
		this.affirmationID = affirmationID;
		this.affirmation = affirmation;
		this.userID = userID;
	}

	public int getAffirmationID() {
		return affirmationID;
	}

	public void setAffirmationID(int affirmationID) {
		this.affirmationID = affirmationID;
	}

	public String getAffirmation() {
		return affirmation;
	}

	public void setAffirmation(String affirmation) {
		this.affirmation = affirmation;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	

	public Affirmation create() throws ValidationException, DatabaseException {
		Connection cn = null;
		Affirmation savedAffirmation = null;

		
		try{
			cn = dao.getConnection();
			
			if(this.getAffirmation()==null || this.getAffirmation().isEmpty()){
				throw new ValidationException(ErrorMessages.AFFIRMATION_EMPTY);
			}
			
			savedAffirmation = dao.affirmationCreate(cn, this);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }
		
		return savedAffirmation;
		
	}
	
}
