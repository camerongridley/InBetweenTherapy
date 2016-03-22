package com.cggcoding.messaging.invitations;

import java.time.LocalDateTime;

public class InvitationDetail {
	private String invitationCode;
	private int therapistUserID;
	private String clientEmail;
	private LocalDateTime dateInvited;
	private LocalDateTime dateAccepted;
	private boolean accepted;
	

	private InvitationDetail(int therapistUserID, String clientEmail) {
		this.therapistUserID = therapistUserID;
		this.clientEmail = clientEmail;
	}
	
	public static InvitationDetail createInvitation(int therapistUserID, String clientEmail){
		return new InvitationDetail(therapistUserID, clientEmail);
		
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public int getTherapistUserID() {
		return therapistUserID;
	}

	public void setTherapistUserID(int therapistUserID) {
		this.therapistUserID = therapistUserID;
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail;
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
}
