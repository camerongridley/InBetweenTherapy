package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	private int id;
	private String email;
	private String encryptedPassword;
	private int activeTreatmentIssueId;
	private List<TreatmentIssue> txIssues;
	
	public User (int id, String email, String password){
		this.id = id;
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
