package com.cggcoding.models;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.invitations.Invitation;
import com.cggcoding.security.PasswordEncryptionService;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

public abstract class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userID;
	private String email;
	private String userName;
	private String firstName;
	private String lastName;
	private int roleID;
	private List<String> roles;
	private String role;
	private List<TreatmentPlan> treatmentPlanList;
	private String mainMenuURL;
	
	private static DatabaseActionHandler dao = new MySQLActionHandler();
	
	public User (int userID, String userName, String firstName, String lastName, String email){
		this.userID = userID;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roleID = 0;
		roles = new ArrayList<>();
		this.treatmentPlanList = new ArrayList<>();
		this.mainMenuURL = "";
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getUserID(){
		return userID;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public List<String> getRoles(){
		return roles;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean hasRole(String roleName){

		for(String name : roles){
			if (name.equals(roleName)){
				return true;
			}
		}
		return false;
	}

	public void addRole(String roleName){
		roles.add(roleName);
	}

	public void setTreatmentPlanList(List<TreatmentPlan> treatmentPlanList){
		this.treatmentPlanList = treatmentPlanList;
	}

	public List<TreatmentPlan> getTreatmentPlanList(){
		return treatmentPlanList;
	}

	public String getMainMenuURL() {
		return mainMenuURL;
	}

	public void setMainMenuURL(String mainMenuURL) {
		this.mainMenuURL = mainMenuURL;
	}

	public void addTreatmentPlan(TreatmentPlan treatmentPlan){
		this.treatmentPlanList.add(treatmentPlan);
	}

	public TreatmentPlan getTreatmentPlan(int treatmentPlanID) throws ValidationException{
		TreatmentPlan planFound = null;
		for(TreatmentPlan plan : treatmentPlanList){
			if(plan.getTreatmentPlanID()==treatmentPlanID){
				planFound = plan;
			}
		}
		
		if(planFound == null){
			throw new ValidationException(ErrorMessages.NO_PLAN_FOUND_FOR_USER);
		}
		return planFound;
 
	}

	public abstract boolean isAuthorizedForTreatmentPlan(int treatmentPlanID) throws DatabaseException;
	
	public abstract boolean isAuthorizedForStage(int stageID);
	
	public abstract boolean isAuthorizedForTask(int taskID);
	
	protected boolean userOwnsTreatmentPlan(Connection cn, int treatmentPlanID) throws SQLException{
		return dao.userOwnsTreatmentPlan(cn, this, treatmentPlanID);
	}
	
	protected TreatmentPlan createTreatmentPlanFromTemplate(Connection cn, int userIDTakingNewPlan, int treatmentPlanIDToCopy) throws SQLException, ValidationException{
		TreatmentPlan newPlan = TreatmentPlan.loadBasic(cn, treatmentPlanIDToCopy);
		//TreatmentPlan newPlan = TreatmentPlan.getInstanceWithoutID(planToCopy.getTitle(), this.userID, planToCopy.getDescription(), planToCopy.getTreatmentIssueID());
		
		//most of these should be set to their defaults, but am just resetting them here as a precaution
		newPlan.setUserID(userIDTakingNewPlan);
		newPlan.setTemplate(false);
		newPlan.setTemplateID(treatmentPlanIDToCopy);
    	newPlan.setAssignedByUserID(this.userID);
    	newPlan.setInProgress(false);
    	newPlan.setCompleted(false);
    	newPlan.setActiveViewStageIndex(0);
    	
    	

    	newPlan.createBasic(cn);
    	
    	//loop through and change all the userIDs to the userID supplied by the method argument
    	//OPTIMIZE O(N3) complexity here with 3 nested for loops.  Is there a better way to do this?
    	
    	for(MapTreatmentPlanStageTemplate planStageInfo : newPlan.getTreatmentPlanStageTemplateMapList()){
    		newPlan.createStageFromTemplate(cn, planStageInfo.getStageID(), planStageInfo);
    	}
    	
    	/*if ever switch to have copy client plans, then this code would be useful
    	 * for(Stage stage : planToCopy.getStages()){
    		MapTreatmentPlanStageTemplate planStageInfo = newPlan.getMappedStageTemplateByStageID(stage.getStageID());
    		newPlan.copyStageIntoClientTreatmentPlan(cn, stage, planStageInfo);
    	}*/
    	
    	return newPlan;
	}
	
	public TreatmentPlan createTreatmentPlanFromTemplate(int userIDTakingNewPlan, int treatmentPlanIDToCopy) throws ValidationException, DatabaseException{
		Connection cn = null;
		
		TreatmentPlan newPlan = null;

        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
        	
        	createTreatmentPlanFromTemplate(cn, userIDTakingNewPlan, treatmentPlanIDToCopy);
        	
        	cn.commit();
        	
        } catch (SQLException | ValidationException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
			
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
        }
		
		
    	
    	return newPlan;
	}
	
	//TODO delete
	/*public TreatmentPlan copyTreatmentPlanForClient(int userIDTakingNewPlan, int treatmentPlanIDBeingCopied, boolean isTemplate) throws ValidationException, DatabaseException{
    	TreatmentPlan planToCopy = TreatmentPlan.load(treatmentPlanIDBeingCopied);
    	planToCopy.setTemplate(isTemplate);//XXX won't this always be false since plans owned by clients can never be templates?
    	planToCopy.setTemplateID(planToCopy.getTreatmentPlanID());
    	planToCopy.setUserID(userIDTakingNewPlan);
    	planToCopy.setAssignedByUserID(this.userID);
    	//loop through and change all the userIDs to the userID supplied by the method argument
    	//OPTIMIZE O(N3) complexity here with 3 nested for loops.  Is there a better way to do this?
    	for(Stage stage : planToCopy.getStages()){
    		stage.setUserID(userIDTakingNewPlan);
    		stage.setTemplate(false);
    		stage.setTemplateID(stage.getStageID());
    		//OPTIMIZE getMappedStageTemplateByID uses another loop to get the return value, so even more Big O complexity...
    		MapTreatmentPlanStageTemplate stageDetail = planToCopy.getMappedStageTemplateByStageID(stage.getStageID());
    		if(stageDetail != null){
    			stage.setClientStageOrder(stageDetail.getTemplateStageOrder());
    		}
    		
    		
    		List<Task> taskRepetitionsAdded = new ArrayList<>();
    		for(Task task : stage.getTasks()){
    			task.setUserID(userIDTakingNewPlan);
    			task.setTemplate(false);
    			task.setTemplateID(task.getTaskID());
    			
    			MapStageTaskTemplate taskDetail = stage.getMappedTaskTemplateByTaskID(task.getTaskID());
    			int taskReps = 1;
    			int taskOrder = 0;
    			if(taskDetail!=null){
    				taskReps = taskDetail.getTemplateTaskRepetitions();
        			taskOrder = taskDetail.getTemplateTaskOrder();//for now this isn't used since the task orders are going to change as repetitions are created
    			}
    			
    			for(int i = 0; i<taskReps; i++){
    				Task taskRep = task.copy();
    				taskRep.setClientRepetition(i+1);
    				
    				//if more than 1 repetition, change the Task title to reflect repetition number
    				if(taskReps > 1){
    					taskRep.setTitle(task.getTitle() + " (" + (i+1) + ")");
    				}
    				
    				taskRep.setClientTaskOrder(taskRepetitionsAdded.size());
    				
    				taskRepetitionsAdded.add(taskRep);
    			}
	
    		}//end Task loop
    		
    		//reset the Stage's Task list with new list that has been populated with repetitions
    		stage.setTasks(taskRepetitionsAdded);
    		
    		//TODO before moving on to next stage in loop, update the task orders to account for any repetitions added?
    	}//end Stage loop
    	
    	//save the plan - which is responsible for updating treatmentPlanID in all the child objects
    	return planToCopy.create();
    	
    }*/
	
	public static User loadBasic(int userID) throws DatabaseException, ValidationException{
		return dao.userLoadByID(userID);
	}
	
	public static User create(Connection cn, User newUser, byte[] encryptedPassword, byte[] salt) throws SQLException{
		return dao.userCreateNewUser(cn, newUser, encryptedPassword, salt);
	}
	
	/**Allows user to update their user account information - can include a new password or not.
	 * @param clearTextPasswordToAuthenticate - User is required to enter their password to update account info, so this is the password they entered and needs to be authenticated
	 * @param newClearTextPassword - nullable - contains new password in the case the user wants to change their password
	 * @param newClearTextPasswordConfirm - nullable - contains confirmation of new password

	 * @throws DatabaseException
	 * @throws ValidationException 
	 */
	public void update(String clearTextPasswordToAuthenticate, String newClearTextPassword, String newClearTextPasswordConfirm) throws DatabaseException, ValidationException{
		
		Connection cn = null;
		
		try{
			cn = dao.getConnection();

			update(cn, clearTextPasswordToAuthenticate, newClearTextPassword, newClearTextPasswordConfirm);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
		} finally {
			DbUtils.closeQuietly(cn);
	    }
	}
	
	protected void update(Connection cn, String clearTextPasswordToAuthenticate, String newClearTextPassword, String newClearTextPasswordConfirm) throws SQLException, ValidationException {
		UserPassword newUserPassword = encryptNewPassword(newClearTextPassword, newClearTextPasswordConfirm);
		
		if(passwordAuthenticated(cn, this.getEmail(), clearTextPasswordToAuthenticate)){
			if(newClearTextPassword == null || newClearTextPassword.isEmpty()){
				dao.userUpdate(cn, this, null);
			}else{
				dao.userUpdate(cn, this, newUserPassword);
			}
			
		}else{
			throw new ValidationException(ErrorMessages.INVALID_USERNAME_OR_PASSWORD);
		}

	}
	
	public static User loadBasicByEmail(Connection cn, String emailAddress) throws SQLException, ValidationException{
		return dao.userLoadByEmailAddress(cn, emailAddress);
	}
	
	public static User registerNewUser(String userName, String firstName, String lastName, String password, String passwordConfirm, String email, String roleType, String invitationCode) throws ValidationException, DatabaseException{
		
		if(userName.equals("") || firstName.equals("") || lastName.equals("") || password.equals("") || passwordConfirm.equals("") || email.equals("") || roleType == null || roleType.equals("")){
			throw new ValidationException(ErrorMessages.MISSING_USER_INFORMATION);
		}
		
		UserPassword userPassword = encryptNewPassword(password, passwordConfirm);

		Connection cn = null;
		User newUser = null;
	
		try {
			cn = dao.getConnection();
			cn.setAutoCommit(false);
		
			//validate that they userName is available
			if(!dao.userValidateNewUsername(cn, userName)){
				throw new ValidationException(ErrorMessages.USERNAME_ALREADY_EXISTS);
			}
			
			if(!dao.userValidateNewEmail(cn, email)){
				throw new ValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);
			}
			
			//create user
			switch (roleType){
				case Constants.USER_ADMIN:
					newUser = new UserAdmin(0, userName, firstName, lastName, email);
					break;
				case Constants.USER_THERAPIST:
					newUser = new UserTherapist(0, userName, firstName, lastName, email);
					break;
					
				case Constants.USER_CLIENT:
					newUser = new UserClient(0, userName, firstName, lastName, email);
					break;
			}

			User.create(cn, newUser, userPassword.getEncryptedPassword(), userPassword.getPasswordSalt());

			if(!invitationCode.equals("")){
				newUser.processInvitationAcceptance(cn, invitationCode);
			}

			cn.commit();
			
		} catch (SQLException | ValidationException e) {
			e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else {
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
			
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
	    }
		 
		 return newUser;
		 
	}
	
	 
	
	public static User login(String email, String passwordToCheck) throws ValidationException, DatabaseException{

		Connection cn = null;
		User user = null;
		
		try {
			cn = dao.getConnection();
			cn.setAutoCommit(false);

			if(passwordAuthenticated(cn, email, passwordToCheck)){
				user = dao.userLoadInfo(cn, email, passwordToCheck);
				user.performLoginSpecifics();
			}
			
			
			if(user == null){
				throw new ValidationException(ErrorMessages.INVALID_USERNAME_OR_PASSWORD);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
	    }

		return user;
	}
	
	/**
	 * Perform actions needed that are specific to each user type
	 * @throws DatabaseException 
	 */
	protected abstract void performLoginSpecifics() throws DatabaseException;
	
	public static boolean passwordAuthenticated(Connection cn, String email, String passwordToAuthenticate) throws SQLException, ValidationException{
		PasswordEncryptionService passwordService = new PasswordEncryptionService();
		UserPassword userPassword = null;
		boolean authenticated = false;
		
		if(passwordToAuthenticate == null || passwordToAuthenticate.isEmpty()){
			throw new ValidationException(ErrorMessages.PASSWORD_MISSING);
		}
		userPassword = dao.userGetEncryptedPasswordAndSalt(cn, email);
		
		try {
			authenticated = passwordService.authenticate(passwordToAuthenticate, userPassword.getEncryptedPassword(), userPassword.getPasswordSalt());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authenticated;
	}
	
	public abstract void processInvitationAcceptance(Connection cn, String invitationCode) throws SQLException, ValidationException;
	public List<Invitation> getInvitationsSent() throws DatabaseException, ValidationException{
		Connection cn = null;
		List<Invitation> invitationList = new ArrayList<>();
		
        try {
        	cn = dao.getConnection();
        	cn.setAutoCommit(false);
		
			
			
			List<String> invitationCodes = dao.invitationGetSentInvitationCodes(cn, this.getUserID());
			
			for(String code : invitationCodes){
				invitationList.add(Invitation.load(cn, code));
			}
			
			cn.commit();
        	
        } catch (SQLException | ValidationException e) {
        	e.printStackTrace();
			try {
				System.out.println(ErrorMessages.ROLLBACK_DB_OP);
				cn.rollback();
			} catch (SQLException e1) {
				System.out.println(ErrorMessages.ROLLBACK_DB_ERROR);
				e1.printStackTrace();
			}
			if(e.getClass().getSimpleName().equals("ValidationException")){
				throw new ValidationException(e.getMessage());
			}else if(e.getClass().getSimpleName().equals("DatabaseException")){
				throw new DatabaseException(ErrorMessages.GENERAL_DB_ERROR);
			}
			
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DbUtils.closeQuietly(cn);
        }
		
		return invitationList;
	}
	
	/**
	 * @param password
	 * @param passwordConfirm
	 * @return UserPassword - can be null - holds newly generated hashed password and salt
	 * @throws ValidationException
	 */
	public static UserPassword encryptNewPassword(String password, String passwordConfirm) throws ValidationException{
		PasswordEncryptionService passwordService = new PasswordEncryptionService();
		byte[] encryptedPassword = null;
		byte[] salt = null;
		UserPassword userPassword = null;
		
		//validate that the passwords match
		if(!password.equals(passwordConfirm)){
			throw new ValidationException(ErrorMessages.PASSWORDS_DONT_MATCH);
		}
		try {
			salt = passwordService.generateSalt();
			encryptedPassword = passwordService.getEncryptedPassword(password, salt);
			userPassword = new UserPassword(encryptedPassword, salt);
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userPassword;
	}
	
	@Override
	public String toString(){
		return "User id:" + email + ", User email: " + email;
	}

	
}
