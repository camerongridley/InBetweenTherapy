package com.cggcoding.messaging.invitations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Invitation {
	private String invitationCode;
	private int sentFromUserID;
	private String sendToEmail;
	private String sendToName;
	private LocalDateTime dateInvited;
	private LocalDateTime dateAccepted;
	private boolean accepted;
	private List<Integer> treatmentPlanIDsToCopy;
	

	private Invitation(int sentFromUserID, String sendToEmail, String sendToName) {
		this.sentFromUserID = sentFromUserID;
		this.sendToEmail = sendToEmail;
		this.sendToName = sendToName;
	}
	
	public static Invitation createInvitation(int sentFromUserID, String sendToEmail, String sendToName){
		Invitation invitation = new Invitation(sentFromUserID, sendToEmail, sendToName);
		
		invitation.generateInvitationCode();

		return invitation; 
		
	}
	
	private String generateInvitationCode(){
		//generate random UUID
		String invitationCode = UUID.randomUUID().toString();
		
		//TODO write loop that checks if invitation code is a duplicate
		//check that the uuid isn't currently in database - extremely rare case, but possible
		
			//if exists then pick another and check for duplicate
		
		this.setInvitationCode(invitationCode);
		
		return invitationCode;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public int getSentFromUserID() {
		return sentFromUserID;
	}

	public void setSentFromUserID(int sentFromUserID) {
		this.sentFromUserID = sentFromUserID;
	}

	public String getSendToEmail() {
		return sendToEmail;
	}

	public void setSendToEmail(String sendToEmail) {
		this.sendToEmail = sendToEmail;
	}

	public String getSendToName() {
		return sendToName;
	}

	public void setSendToName(String sendToName) {
		this.sendToName = sendToName;
	}

	public LocalDateTime getDateInvited() {
		return dateInvited;
	}

	public void setDateInvited(LocalDateTime dateInvited) {
		this.dateInvited = dateInvited;
	}

	public LocalDateTime getDateAccepted() {
		return dateAccepted;
	}

	public void setDateAccepted(LocalDateTime dateAccepted) {
		this.dateAccepted = dateAccepted;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public List<Integer> getTreatmentPlanIDsToCopy() {
		return treatmentPlanIDsToCopy;
	}

	public void setTreatmentPlanIDsToCopy(List<Integer> treatmentPlanIDsToCopy) {
		this.treatmentPlanIDsToCopy = treatmentPlanIDsToCopy;
	}
	
}
