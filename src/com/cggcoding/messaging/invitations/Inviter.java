package com.cggcoding.messaging.invitations;

import java.util.UUID;

import com.cggcoding.messaging.SMTPEmailer;

public class Inviter{
	private InvitationDetail invitation;
	
	public Inviter(InvitationDetail invitation){
		this.invitation = invitation;
	}

	public InvitationDetail getInvitation() {
		return invitation;
	}

	public void setInvitation(InvitationDetail invitation) {
		this.invitation = invitation;
	}

	//TODO add client name and therapist name to the InvitationDetail
	public static boolean sendInvitation(InvitationDetail invitation){
		String uuid = UUID.randomUUID().toString();
		this.invitation.setInvitationCode(uuid);
		System.out.println("uuid = " + uuid);
		
		String subject = "Invitation to join DoItRight!";
		String body = "_____ has invited you to join DoItRight! as a client.  Please click the link "
				+ "below to be taken to the site and register.  Using the link provided will automatically "
				+ "connect you with your therapist.  Or if you'd prefer to go to the site on your own, visit doitright.cggcoding.com"
				+ "and click sign up.  Then at the registration page, enter your enter the invitation code " + invitation.getInvitationCode()
				+ " when applicable.";
		
		SMTPEmailer.sendEmail(therapistName, clientEmailAddress, subject, body);
		
		return true;
	}
	
	public InvitationDetail getInvitationStatus(int therpistUserID, String clientEmail){
		
		return null;
	}

	
}
