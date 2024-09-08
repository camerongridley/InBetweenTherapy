package com.cggcoding.messaging.invitations;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.SMTPEmailer;
import com.cggcoding.models.Stage;
import com.cggcoding.models.User;
import com.cggcoding.models.UserClient;
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

	public static Invitation sendInvitation(Invitation invitation, User sentFromUser, String sendToEmail) throws DatabaseException, ValidationException{

		//prepare the invitation
		String invitationLink = Constants.ROOT_URL + "/InvitationRegistration?email=" + invitation.getRecipientEmail() + "&firstName=" + invitation.getRecipientFirstName() + "&lastName=" + invitation.getRecipientLastName() + "&invitationCode=" + invitation.getInvitationCode();
		String subject = "Invitation to join DoItRight!";
		String body = "Dear " + invitation.getRecipientFirstName() + ",\n\n"
				+ sentFromUser.getFirstName() + " " + sentFromUser.getLastName() + " has invited you to join DoItRight! as a client. "
				+ "Please click the link below to be taken to the site and register.  Using the link provided will automatically "
				+ "connect you with your therapist. \n\n" + invitationLink + "\n\nOr if you'd prefer to go directly to " + Constants.ROOT_URL
				+ " and click \"Register\".  Then at the registration page, enter your enter the invitation code " + invitation.getInvitationCode()
				+ " when applicable."
				+ "\n\nNow get going!\n\nThe DoItRight Team";
		
		invitation.setDateInvited(LocalDateTime.now());
		
		//insert the invitation into the database
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			
			cn.setAutoCommit(false);
			
			if(dao.invitationAlreadyExists(cn, invitation)){
				throw new ValidationException(ErrorMessages.INVITATION_ALREADY_INVITED);
			}
			
			//TODO allow for an existing user to get linked up to a therapist
			if(User.loadBasicByEmail(cn, invitation.getRecipientEmail()) != null){
				throw new ValidationException(ErrorMessages.INTIVATION_USER_ALREADY_REGISTERED);
			}
			
			dao.invitationCreate(cn, invitation);
			
			//send the invitation via email
			SMTPEmailer.sendEmail(sendToEmail, subject, body);
			
			cn.commit();
		} catch (SQLException e) {//TODO add catching messaging exceptions here and throw validation messages
			e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DatabaseException(ErrorMessages.ROLLBACK_DB_ERROR);
			}
			
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		 } catch (AddressException e) {
				e.printStackTrace();
				try {
					System.out.println(ErrorMessages.ROLLBACK_DB_OP);
					cn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new DatabaseException(ErrorMessages.ROLLBACK_DB_ERROR);
				}
				throw new ValidationException(ErrorMessages.INVITATION_INVALID_EMAIL_ADDRESS);
		} catch (MessagingException e) {
			e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DatabaseException(ErrorMessages.ROLLBACK_DB_ERROR);
			}
			throw new ValidationException(ErrorMessages.INVITATION_UNSUCCESSFUL_SEND);
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
	    }
		
		
		
		return invitation;
	}
	
    

	
	public Invitation getInvitationStatus(int therpistUserID, String clientEmail){
		
		return null;
	}

	
}
