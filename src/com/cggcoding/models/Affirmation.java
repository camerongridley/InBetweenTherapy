package com.cggcoding.models;

public class Affirmation {

	private int affirmationID;
	private String affirmation;
	private int userID;
	
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
	
	
	
}
