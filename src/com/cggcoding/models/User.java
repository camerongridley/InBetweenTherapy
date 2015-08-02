package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int userID;
	private String email;
	private String encryptedPassword;
	private int activeTreatmentIssueId;
	private List<TreatmentIssue> txIssues;
	
	public User (int userID, String email, String password){
		this.userID = userID;
		this.email = email;
		this.encryptedPassword = password;
		this.txIssues = new ArrayList<>();
	}
	
	public void setTreatmentIssueList (List<TreatmentIssue> txIssues){
		this.txIssues = txIssues;
	}
	
	public List<TreatmentIssue> getTreatmentIssueList(){
		return txIssues;
	}
	
	public void addTreatmentIssue(TreatmentIssue txIssue){
		this.txIssues.add(txIssue);
	}
	
	public TreatmentIssue getTreatmentIssue(int txIssueId){
		return txIssues.get(txIssueId);
	}
	
	public void setActiveTreatmentIssueId (int stageId){
		this.activeTreatmentIssueId = stageId;
	}
	
	public int getActiveTreatmentIssueId(){
		return activeTreatmentIssueId;
	}
	
	public TreatmentIssue getActiveTreatmentIssue(){
		return txIssues.get(activeTreatmentIssueId);
	}
}
