package com.cggcoding.controllers.therapist;

import java.util.UUID;

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

	public boolean sendInvitation(){
		String uuid = UUID.randomUUID().toString();
		this.invitation.setInvitationCode(uuid);
		System.out.println("uuid = " + uuid);
		
		return true;
	}
	
	public InvitationDetail getInvitationStatus(int therpistUserID, String clientEmail){
		
		return null;
	}

	
}
