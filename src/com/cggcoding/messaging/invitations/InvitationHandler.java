package com.cggcoding.messaging.invitations;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.SMTPEmailer;
import com.cggcoding.models.Stage;
import com.cggcoding.models.User;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public class InvitationHandler{
	private Invitation invitation;
	private static DatabaseActionHandler dao = new MySQLActionHandler();

	
	public InvitationHandler(Invitation invitation){
		this.invitation = invitation;
	}

	public Invitation getInvitation() {
		return invitation;
	}

	public void setInvitation(Invitation invitation) {
		this.invitation = invitation;
	}

	public static boolean sendInvitation(Invitation invitation, User sentFromUser, String sendToEmail) throws DatabaseException, ValidationException{

		//prepare the invitation
		String subject = "Invitation to join DoItRight!";
		String body = "Dear " + invitation.getRecipientFirstName() + ",\n\n"
				+ sentFromUser.getFirstName() + " " + sentFromUser.getLastName() + " has invited you to join DoItRight! as a client. "
				+ "Please click the link below to be taken to the site and register.  Using the link provided will automatically "
				+ "connect you with your therapist.  Or if you'd prefer to go directly to " + Constants.rootURL
				+ " and click \"Register\".  Then at the registration page, enter your enter the invitation code " + invitation.getInvitationCode()
				+ " when applicable."
				+ "\n\nNow get going!\n\nThe DoItRight Team";
		
		invitation.setDateInvited(LocalDateTime.now());
		
		//insert the invitation into the database
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			
			cn.setAutoCommit(false);
			if(dao.invitationCheckForExisting(cn, invitation)){
				dao.invitationCreate(cn, invitation);
			}else{
				throw new ValidationException("You have already sent that person and invitation.");
			}
			
			cn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DatabaseException(ErrorMessages.ROLLBACK_DB_ERROR);
			}
			
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
	    }
		
		//send the invitation via email
		SMTPEmailer.sendEmail(sendToEmail, subject, body);
		
		return true;
	}
	
	public Invitation getInvitationStatus(int therpistUserID, String clientEmail){
		
		return null;
	}

	
}
