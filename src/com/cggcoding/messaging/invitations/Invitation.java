package com.cggcoding.messaging.invitations;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public class Invitation {
	private String invitationCode;
	private int senderUserID;
	private String recipientEmail;
	private String recipientFirstName;
	private String recipientLastName;
	private LocalDateTime dateInvited;
	private LocalDateTime dateAccepted;
	private boolean accepted;
	private List<Integer> treatmentPlanIDsToCopy;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE; 

	private Invitation(int senderUserID, String recipientEmail, String recipientFirstName, String recipientLastName) {
		this.senderUserID = senderUserID;
		this.recipientEmail = recipientEmail;
		this.recipientFirstName = recipientFirstName;
		this.recipientLastName = recipientLastName;
		dateInvited = null;
		dateAccepted = null;
		accepted = false;
		treatmentPlanIDsToCopy = new ArrayList<>();
	}
	
	public Invitation(String invitationCode, int senderUserID, String recipientEmail, String recipientFirstName,
			String recipientLastName, LocalDateTime dateInvited, LocalDateTime dateAccepted, boolean accepted,
			List<Integer> treatmentPlanIDsToCopy) {
		super();
		this.invitationCode = invitationCode;
		this.senderUserID = senderUserID;
		this.recipientEmail = recipientEmail;
		this.recipientFirstName = recipientFirstName;
		this.recipientLastName = recipientLastName;
		this.dateInvited = dateInvited;
		this.dateAccepted = dateAccepted;
		this.accepted = accepted;
		this.treatmentPlanIDsToCopy = treatmentPlanIDsToCopy;
	}
	
	public static Invitation createInvitation(int senderUserID, String recipientEmail, String recipientFirstName, String recipientLastName){
		Invitation invitation = new Invitation(senderUserID, recipientEmail, recipientFirstName, recipientLastName);
		
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

	public int getSenderUserID() {
		return senderUserID;
	}

	public void setSenderUserID(int senderUserID) {
		this.senderUserID = senderUserID;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getRecipientFirstName() {
		return recipientFirstName;
	}

	public void setRecipientFirstName(String recipientFirstName) {
		this.recipientFirstName = recipientFirstName;
	}

	public String getRecipientLastName() {
		return recipientLastName;
	}

	public void setRecipientLastName(String recipientLastName) {
		this.recipientLastName = recipientLastName;
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
	
	public void addTreatmentPlanID(int treatmentPlanID){
		this.treatmentPlanIDsToCopy.add(treatmentPlanID);
	}
	
	public void removeTreatmentPlanID(int treatmentPlanID){
		for(int i=0; i < this.getTreatmentPlanIDsToCopy().size(); i++){
			if(treatmentPlanIDsToCopy.get(i)==treatmentPlanID){
				treatmentPlanIDsToCopy.remove(i);
			}
		}
	}
	
	public static Invitation load(Connection cn, String invitationCode) throws SQLException, ValidationException{
		Invitation invitation = dao.invitationLoad(cn, invitationCode);
		
		if(invitation==null){
			throw new ValidationException(ErrorMessages.INVITATION_NOT_FOUND);
		}
		
		return invitation;
	}
	
	public void update(Connection cn) throws SQLException, ValidationException{
		dao.invitationUpdate(cn, this);
	}
	
	public static void delete(String invitationCode) throws DatabaseException{
		Connection cn = null;
		
		try{
			cn = dao.getConnection();
			
			dao.invitationDelete(cn, invitationCode);
		} catch (SQLException e){
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		}finally{
			DbUtils.closeQuietly(cn);
		}
		
	}
	
	public String getFormattedDateInvited(){
		return this.getDateInvited().format(dateFormat);
	}
	
	public String getFormattedDateAccepted(){
		if(this.dateAccepted != null){
			return this.getDateAccepted().format(dateFormat);
		}else{
			return "";
		}
		
	}
}
