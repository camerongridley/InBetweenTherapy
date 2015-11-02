package com.cggcoding.models;

import java.util.List;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

/**
 * Created by cgrid_000 on 8/7/2015.
 */
public class TreatmentIssue implements DatabaseModel{
    private int treatmentIssueID;
    private String treatmentIssueName;
    private int userID;
    
    private static DatabaseActionHandler databaseActionHandler= new MySQLActionHandler();

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
		TreatmentIssue savedIssue = databaseActionHandler.treatmentIssueValidateAndCreate(this, userID);
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
    
    
}
