package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class TreatmentIssue implements Serializable, DatabaseModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	public Object create() throws ValidationException, DatabaseException {
		Connection cn = null;
		TreatmentIssue savedIssue = null;
		
		if(isIssuePresent()){
		
			try{
				cn = dao.getConnection();
				
				if(dao.treatmentIssueValidateNewName(cn, this.treatmentIssueName, userID)){
					dao.treatmentIssueCreate(cn, this, userID);
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			} finally {
				DbUtils.closeQuietly(cn);
		    }
		
		}
		
		return savedIssue;
		
	}

	@Override
	public void update() throws ValidationException, DatabaseException {
		if(isIssuePresent()){
			Connection cn = null;
			
			try{
				cn = dao.getConnection();
				
				if(dao.treatmentIssueValidateUpdatedName(cn, this)){
					dao.treatmentIssueUpdate(cn, this);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			} finally {
				DbUtils.closeQuietly(cn);
		    }

		}
		
	}

	private boolean isIssuePresent() throws ValidationException {
		if(this.treatmentIssueName.isEmpty() || this.treatmentIssueName ==""){
    		throw new ValidationException(ErrorMessages.ISSUE_NAME_MISSING);
    	}else {
    		return true;
    	}
	}

	@Override
	public void delete() throws ValidationException, DatabaseException {
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			
			dao.treatmentIssueDelete(cn, this.treatmentIssueID);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }
		
	}
    
	public static ArrayList<TreatmentIssue> getCoreTreatmentIssues() throws DatabaseException {
		return dao.treatmentIssueGetCoreList();
	}
}
