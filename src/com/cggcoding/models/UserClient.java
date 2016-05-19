package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

	private int activeTreatmentPlanID;
	private Affirmation affirmation;
	private int loginStreak;
	private String loginStreakMessage;
	private int activityStreak;
	private String activityStreakMessage;
	
	DatabaseActionHandler dao = new MySQLActionHandler();
	
	public UserClient(int userID, String userName, String firstName, String lastName, String email){
		super(userID, userName, firstName, lastName, email);
		this.setRoleID(Constants.CLIENT_ROLE_ID);
		this.addRole(Constants.USER_CLIENT);
		this.setRole(Constants.USER_CLIENT);
		activeTreatmentPlanID = -1;
		this.affirmation = new Affirmation();
		setMainMenuURL(Constants.URL_CLIENT_MAIN_MENU);
		loginStreak = 0;
		loginStreakMessage = "";
		activityStreak = 0;
		activityStreakMessage = "";
	}

	
	public void setActiveTreatmentPlanID(int treatmentPlanID){
		this.activeTreatmentPlanID = treatmentPlanID;
	}
	
	public int getActiveTreatmentPlanID(){
		return activeTreatmentPlanID;
	}
	
	public Affirmation getAffirmation() {
		return affirmation;
	}


	public void setAffirmation(Affirmation affirmation) {
		this.affirmation = affirmation;
	}


	public int getLoginStreak() {
		return loginStreak;
	}


	public String getLoginStreakMessage() {
		return loginStreakMessage;
	}


	public void setLoginStreakMessage(String loginStreakMessage) {
		this.loginStreakMessage = loginStreakMessage;
	}


	public void setLoginStreak(int loginStreak) {
		this.loginStreak = loginStreak;
	}
	

	public int getActivityStreak() {
		return activityStreak;
	}


	public void setActivityStreak(int activityStreak) {
		this.activityStreak = activityStreak;
	}
	

	public String getActivityStreakMessage() {
		return activityStreakMessage;
	}


	public void setActivityStreakMessage(String activityStreakMessage) {
		this.activityStreakMessage = activityStreakMessage;
	}


	public void updateActiveTreatmentPlanID() throws DatabaseException{
		Connection cn = null;
  		
        try {
        	cn = dao.getConnection();  	
        	dao.userClientUpdateActiveTreatmentPlanID(cn, this);
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(cn);
        }
		
	}
	
	public TreatmentPlan getActiveTreatmentPlan(){
		try {
			return super.getTreatmentPlan(activeTreatmentPlanID);
		} catch (ValidationException e) {
			return null;
		}
		//return TreatmentPlan.load(getActiveTreatmentPlanID());
		
	}

	public void loadAllClientTreatmentPlans() throws DatabaseException, ValidationException {
		Connection cn = null;
  		
        try {
        	cn = dao.getConnection();  	
        	loadAllClientTreatmentPlans(cn);
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(cn);
        }
	}
	
	public void loadAllClientTreatmentPlans(Connection cn) throws SQLException, ValidationException {
		this.setTreatmentPlanList(dao.userGetTreatmentPlans(cn, getUserID()));
	}
	
	public List<TreatmentPlan> getAssignedTreatmentPlans() throws DatabaseException, ValidationException{
		/*boolean inProgress = false;
		boolean isCompleted = false;*/
		List<TreatmentPlan> assignedPlans = new ArrayList<>();
		
		for(TreatmentPlan plan : this.getTreatmentPlanList()){
			if(plan.isInProgress() == false && plan.isCompleted()==false){
				assignedPlans.add(plan);
			}
		}
		
		return assignedPlans;
	}
	
	public List<TreatmentPlan> getInProgressTreatmentPlans() throws DatabaseException, ValidationException{
		/*boolean inProgress = true;
		boolean isCompleted = false;*/
		List<TreatmentPlan> inProgressPlans = new ArrayList<>();
		
		for(TreatmentPlan plan : this.getTreatmentPlanList()){
			if(plan.isInProgress() == true && plan.isCompleted()==false){
				inProgressPlans.add(plan);
			}
		}
		
		return inProgressPlans;
	}
	
	public List<TreatmentPlan> getCompletedTreatmentPlans() throws DatabaseException, ValidationException{
		/*boolean inProgress = false;
		boolean isCompleted = true;*/
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

	public void setRandomDailyAffirmation(Connection cn) throws SQLException{
		List<Affirmation> allAffirmations = dao.getAllAffirmations(cn, this);
		Random rand = new Random();
		
		setAffirmation(allAffirmations.get(rand.nextInt(allAffirmations.size())));
		
	}

	@Override
	protected void performLoginSpecifics(Connection cn) throws ValidationException, SQLException {
		LoginHistory loginHx = new LoginHistory(this.getUserID(), LocalDateTime.now());
		loginHx.create(cn);
		loginHx.deleteOldEntries(cn, Constants.DAYS_OF_LOGIN_HISTORY_TO_KEEP, loginHx);
		int loginStreak = calculateLoginStreak(cn);
		this.setLoginStreak(loginStreak);
		int activityStreak = calculateActivityStreak(cn);
		this.setActivityStreak(activityStreak);
		this.loadAllClientTreatmentPlans(cn);
		
	}

	/**Assumes that the LoginHistory List loaded from database is in DESCENDING order by date
	 * @param cn
	 * @param mostRecentLogin
	 * @throws SQLException
	 */
	private int calculateLoginStreak(Connection cn) throws SQLException {
		List<LoginHistory> loginHistoryList = dao.loginHistoryLoadAll(cn, getUserID());
		List<LocalDateTime> loginHxLDT = new ArrayList<>();
		//start loginStreak at 1 since at the very least the current login will count 
		int streak = 0;

		for(LoginHistory loginHx : loginHistoryList){
			loginHxLDT.add(loginHx.getLoginDateTime());
		}
		
		streak = calculateStreakByDate(loginHxLDT);

		return streak;
		//commented out since sorting is done on the database side, if this changes, may need to modify the compareTo method of LoginHistory
		//Collections.sort(loginHistoryList);
		
		
	}
	
	public int calculateActivityStreak(Connection cn) throws SQLException {
		List<LocalDateTime> datesOfCompletedTasks = dao.userClientGetDatesOfCompletedTasks(cn, getUserID());
		
		//start activityStreak at 0 since no tasks have been completed after just logging in
		int streak = 0;
		streak = calculateStreakByDate(datesOfCompletedTasks);

		if(streak <= 3){
			this.setActivityStreakMessage("Keep it up.");
		} else if(streak <= 5){
			this.setActivityStreakMessage("You're on a roll.  Keep it up!");
		} else if(streak <= 7){
			this.setActivityStreakMessage("Nice! You're really cruising now!");
		} else if(streak >= 10){
			this.setActivityStreakMessage("You're on fire!");
		}
		
		return streak;
	}
	
	private int calculateStreakByDate(List<LocalDateTime> dateListForAnalysis){
		int streak = 0;
		LocalDateTime newerLDT = LocalDateTime.now();
		
		//first check if there is an entry for today - this adds to the streak but it is not necessary for maintaining a streak 
		//since the client could perform an activity later today that would maintain the streak
		if(dateListForAnalysis.get(0).getYear()==newerLDT.getYear() && 
				dateListForAnalysis.get(0).getDayOfYear()==newerLDT.getDayOfYear()){
			streak++;
		}
		
		//now add up streak for all entries up until today
		for(LocalDateTime ldt : dateListForAnalysis){
			LocalDateTime olderLDT = ldt;
			if(newerLDT.getYear()==olderLDT.getYear() 
					&& newerLDT.getDayOfYear()-olderLDT.getDayOfYear()==1){
				streak++;
				newerLDT = olderLDT;
			}
			
		}
		
		return streak;
	}
	
	public void updateStatistics() throws DatabaseException{
		Connection cn = null;
  		
        try {
        	cn = dao.getConnection();  	
        	int streak = calculateActivityStreak(cn);
        	this.setActivityStreak(streak);
        } catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(cn);
        }
	}
	
}
