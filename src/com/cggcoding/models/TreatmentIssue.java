package com.cggcoding.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Created by cgrid_000 on 8/7/2015.
 */
public class TreatmentIssue implements DatabaseModel{
    private int treatmentIssueID;
    private String treatmentIssueName;
    private int userID;
    
    private static DatabaseActionHandler dao= new MySQLActionHandler();

    public TreatmentIssue(int treatmentIssueID, String treatmentIssueName) {
        this.treatmentIssueID = treatmentIssueID;
        this.treatmentIssueName = treatmentIssueName;
        this.userID = 0;
    }
    
    public TreatmentIssue(int treatmentIssueID, String treatmentIssueName, int userID) {
        this.treatmentIssueID = treatmentIssueID;
        this.treatmentIssueName = treatmentIssueName;
        this.userID = userID;
    }
    
    public TreatmentIssue( String treatmentIssueName, int userID) {
        this.treatmentIssueID = 0;
        this.treatmentIssueName = treatmentIssueName;
        this.userID = userID;
    }

    public void setTreatmentIssueID(int treatmentIssueID){
    	this.treatmentIssueID = treatmentIssueID;
    }
    
    public int getTreatmentIssueID() {
        return treatmentIssueID;
    }

    public String getTreatmentIssueName() {
        return treatmentIssueName;
    }

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Override
	public Object saveNew() throws ValidationException, DatabaseException {
		Connection cn = null;
		TreatmentIssue savedIssue = null;
		
		try{
			cn = dao.getConnection();
	        	
	    	if(newNameIsValid(cn, true, this.treatmentIssueName, this.userID)){
	    		savedIssue = dao.treatmentIssueCreate(cn, this, this.userID);
	    	}
		} finally{
	    	DbUtils.closeQuietly(cn);
		}
		
		this.treatmentIssueID = savedIssue.getTreatmentIssueID();
		
		return savedIssue;
	}

	@Override
	public void update() throws ValidationException, DatabaseException {
		// TODO implement method
		
	}

	@Override
	public void delete() throws ValidationException, DatabaseException {
		// TODO implement method
		
	}
	
	private boolean newNameIsValid(Connection cn, boolean keepConnAlive, String issueName, int userID) throws ValidationException, DatabaseException{
		boolean result = false;
		
		try{
			if(cn == null){
				cn = dao.getConnection();
			}
	        	
	    	if(issueName.isEmpty() || issueName ==""){
	    		throw new ValidationException(ErrorMessages.ISSUE_NAME_MISSING);
	    	}else{
	    		if(dao.treatmentIssueValidateNewName(cn, issueName, userID)){
	    			result = true;
	    		} else {
	    			result = false;
	    			throw new ValidationException(ErrorMessages.ISSUE_NAME_EXISTS);
	    		}

	    	}
		} finally{
			if(!keepConnAlive){
	    		DbUtils.closeQuietly(cn);
	    	}
		}
    	
    	return result;

    }
    
    
}
