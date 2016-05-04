package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.invitations.Invitation;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public class UserClient extends User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int activeTreatmentPlanId;
	
	DatabaseActionHandler dao = new MySQLActionHandler();
	
	public UserClient(int userID, String userName, String firstName, String lastName, String email){
		super(userID, userName, firstName, lastName, email);
		this.setRoleID(Constants.CLIENT_ROLE_ID);
		this.addRole(Constants.USER_CLIENT);
		this.setRole(Constants.USER_CLIENT);
		activeTreatmentPlanId = -1;
		setMainMenuURL(Constants.URL_CLIENT_MAIN_MENU);
	}

	
	public void setActiveTreatmentPlanId(int treatmentPlanID){
		this.activeTreatmentPlanId = treatmentPlanID;
	}
	
	public int getActiveTreatmentPlanId(){
		return activeTreatmentPlanId;
	}
	
	public TreatmentPlan getActiveTreatmentPlan() throws ValidationException{
		return super.getTreatmentPlan(activeTreatmentPlanId);
	}

	public void loadAllClientTreatmentPlans() throws DatabaseException, ValidationException {
		this.setTreatmentPlanList(dao.userGetTreatmentPlans(getUserID()));
	}
	
	public List<TreatmentPlan> getAssignedTreatmentPlans() throws DatabaseException, ValidationException{
		boolean inProgress = false;
		boolean isCompleted = false;
		List<TreatmentPlan> assignedPlans = new ArrayList<>();
		
		for(TreatmentPlan plan : this.getTreatmentPlanList()){
			if(plan.isInProgress() == false && plan.isCompleted()==false){
				assignedPlans.add(plan);
			}
		}
		
		return assignedPlans;
	}
	
	public List<TreatmentPlan> getInProgressTreatmentPlans() throws DatabaseException, ValidationException{
		boolean inProgress = true;
		boolean isCompleted = false;
		List<TreatmentPlan> inProgressPlans = new ArrayList<>();
		
		for(TreatmentPlan plan : this.getTreatmentPlanList()){
			if(plan.isInProgress() == true && plan.isCompleted()==false){
				inProgressPlans.add(plan);
			}
		}
		
		return inProgressPlans;
	}
	
	public List<TreatmentPlan> getCompletedTreatmentPlans() throws DatabaseException, ValidationException{
		boolean inProgress = false;
		boolean isCompleted = true;
		List<TreatmentPlan> completedPlans = new ArrayList<>();
		
		for(TreatmentPlan plan : this.getTreatmentPlanList()){
			if(plan.isInProgress() == false && plan.isCompleted()==true){
				completedPlans.add(plan);
			}
		}
		
		return completedPlans;
	}


	@Override
	public boolean isAuthorizedForTreatmentPlan(int treatmentPlanID) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAuthorizedForStage(int stageID) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isAuthorizedForTask(int taskID) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void processInvitationAcceptance(Connection cn, String invitationCode) throws SQLException, ValidationException{

    	Invitation invitation = Invitation.load(cn, invitationCode);
    	
    	invitation.setAccepted(true);
    	invitation.setDateAccepted(LocalDateTime.now());
    	
    	invitation.update(cn);
    	
    	//TODO here can check for newUser type and handle for other scenarios such as when a therapist invites another therapist
    	dao.therapistCreateClientConnection(cn, invitation.getSenderUserID(), this.getUserID());
    	
    	try {
			User invitationSender = User.loadBasic(invitation.getSenderUserID());
			
			for(int treatmentPlanID : invitation.getTreatmentPlanIDsToCopy()){
	    		invitationSender.createTreatmentPlanFromTemplate(cn, this.getUserID(), treatmentPlanID);
	    	}
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	

    }


	@Override
	protected void performLoginSpecifics() throws DatabaseException {
		// so far nothing to do here
		
	}
}
