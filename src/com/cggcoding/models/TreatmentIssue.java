package com.cggcoding.models;

/**
 * Created by cgrid_000 on 8/7/2015.
 */
public class TreatmentIssue {
    private int treatmentIssueID;
    private String treatmentIssueName;
    private int userID;

    public TreatmentIssue(int treatmentIssueID, String treatmentIssueName) {
        this.treatmentIssueID = treatmentIssueID;
        this.treatmentIssueName = treatmentIssueName;
        this.userID = -1;
    }
    
    public TreatmentIssue(int treatmentIssueID, String treatmentIssueName, int userID) {
        this.treatmentIssueID = treatmentIssueID;
        this.treatmentIssueName = treatmentIssueName;
        this.userID = userID;
    }
    
    public TreatmentIssue( String treatmentIssueName, int userID) {
        this.treatmentIssueID = -1;
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
    
    
}
