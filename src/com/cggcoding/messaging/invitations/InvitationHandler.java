package com.cggcoding.messaging.invitations;

import com.cggcoding.messaging.SMTPEmailer;
import com.cggcoding.models.User;
import com.cggcoding.utils.Constants;

public class InvitationHandler{
	private Invitation invitation;
	
	public InvitationHandler(Invitation invitation){
		this.invitation = invitation;
	}

	public Invitation getInvitation() {
		return invitation;
	}

	public void setInvitation(Invitation invitation) {
		this.invitation = invitation;
	}

	public static boolean sendInvitation(Invitation invitation, User sentFromUser, String sendToEmail){

		String subject = "Invitation to join DoItRight!";
		String body = "Dear " + invitation.getSendToName() + ",\n\n"
				+ sentFromUser.getFirstName() + " " + sentFromUser.getLastName() + " has invited you to join DoItRight! as a client. "
				+ "Please click the link below to be taken to the site and register.  Using the link provided will automatically "
				+ "connect you with your therapist.  Or if you'd prefer to go directly to " + Constants.rootURL
				+ " and click \"Register\".  Then at the registration page, enter your enter the invitation code " + invitation.getInvitationCode()
				+ " when applicable."
				+ "\n\nNow get going!\n\nThe DoItRight Team";
		
		SMTPEmailer.sendEmail(sendToEmail, subject, body);
		
		return true;
	}
	
	public Invitation getInvitationStatus(int therpistUserID, String clientEmail){
		
		return null;
	}

	
}
