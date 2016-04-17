package com.cggcoding.models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.messaging.invitations.Invitation;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;
import com.cggcoding.utils.messaging.ErrorMessages;

/**
 * Created by cgrid_000 on 8/8/2015.
 */
public class UserTherapist extends User implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private List<TreatmentIssue> coreTreatmentIssues;
    private List<TreatmentPlan> allAssignedClientPlans;
    private List<TreatmentPlan> activeAssignedClientPlans;
    private List<TreatmentPlan> unstartedAssignedClientPlans;
    private List<TreatmentPlan> completedAssignedClientPlans;
    
    private Map<Integer, UserClient> clientMap;
    private Map<UserClient, String> clientToUuidMap;
    private Map<String, UserClient> uuidToClientMap;
    
    private static DatabaseActionHandler dao= new MySQLActionHandler();
        
    public UserTherapist(int userID, String userName, String firstName, String lastName, String email){
        super(userID, userName, firstName, lastName, email);
        this.setRoleID(Constants.THERAPIST_ROLE_ID);
        this.addRole(Constants.USER_THERAPIST);
		this.setRole(Constants.USER_THERAPIST);
        
        this.coreTreatmentIssues = new ArrayList<>();
        this.allAssignedClientPlans = new ArrayList<>();
        setMainMenuURL(Constants.URL_THERAPIST_MAIN_MENU);
        this.clientMap = new LinkedHashMap<>();
        clientToUuidMap = new LinkedHashMap<>();
        uuidToClientMap = new LinkedHashMap<>();
    }

	public Map<Integer, UserClient> getClientMap() {
		return clientMap;
	}

	public void setClientMap(Map<Integer, UserClient> clientMap) {
		this.clientMap = clientMap;
	}

	public Map<UserClient, String> getClientToUuidMap() {
		return clientToUuidMap;
	}

	public void setClientToUuidMap(Map<UserClient, String> userIDToUUID) {
		this.clientToUuidMap = userIDToUUID;
	}

	public Map<String, UserClient> getUuidToClientMap() {
		return uuidToClientMap;
	}

	public void setUuidToClientMap(Map<String, UserClient> uuidToUserID) {
		this.uuidToClientMap = uuidToUserID;
	}

	public void addClient(UserClient client){
    	this.clientMap.put(client.getUserID(), client);
    }
    
    public UserClient getClient(int clientUserID){
    	return clientMap.get(clientUserID);
    }
    
    public Map<Integer, UserClient> loadClients() throws DatabaseException{
    	this.clientMap = dao.userGetClientsByTherapistID(this.getUserID());
    	return clientMap;
    }
    
    public List<TreatmentPlan> loadAllAssignedClientTreatmentPlans(int clientID) throws DatabaseException, ValidationException{
    	this.allAssignedClientPlans =  dao.userGetTherapistAssignedPlans(clientID, this.getUserID());
    	
    	return allAssignedClientPlans;
    }
    
    public List<TreatmentPlan> loadActiveAssignedClientTreatmentPlans(){
    	this.activeAssignedClientPlans = new ArrayList<>();
    	for(TreatmentPlan plan : allAssignedClientPlans){
    		if(plan.isInProgress()){
    			activeAssignedClientPlans.add(plan);
    		}
    	}
    	
    	return activeAssignedClientPlans;
    }
    
    public List<TreatmentPlan> loadCompletedAssignedClientTreatmentPlans(){
    	this.completedAssignedClientPlans = new ArrayList<>();
    	for(TreatmentPlan plan : allAssignedClientPlans){
    		if(plan.isCompleted()){
    			completedAssignedClientPlans.add(plan);
    		}
    	}
    	
    	return completedAssignedClientPlans;
    }
    
    public List<TreatmentPlan> loadUnstartedAssignedClientTreatmentPlans(){
    	this.unstartedAssignedClientPlans = new ArrayList<>();
    	for(TreatmentPlan plan : allAssignedClientPlans){
    		if(!plan.isCompleted() && !plan.isInProgress()){
    			unstartedAssignedClientPlans.add(plan);
    		}
    	}
    	
    	return unstartedAssignedClientPlans;
    }
    
    public UserClient getClientFromUUID(String uuid) throws ValidationException{
    	UserClient client = uuidToClientMap.get(uuid);
    	if(client==null){
    		throw new ValidationException(ErrorMessages.USER_NOT_FOUND);
    	}
    	
    	return client; 
    }
    
    public String getUUIDFromClient(UserClient client) throws ValidationException{
    	String uuid = clientToUuidMap.get(client);
    	if(uuid==null){
    		throw new ValidationException(ErrorMessages.USER_NOT_FOUND);
    	}
    	
    	return uuid;
    }

	@Override
	protected void performLoginSpecifics() throws DatabaseException {
		//first load the clients for this therapist
		loadClients();
		//now pair client userIDs with a uuid and put into maps - uuids are for use on the front end so as to not expose the actual client userID
		for(UserClient client : getClientMap().values()){
			//generate random UUID
			String userUUID = UUID.randomUUID().toString();;
			//in the off chance a uuid is chosen for more than 1 client, get a new one
			while(uuidToClientMap.containsKey(userUUID)){
				userUUID = UUID.randomUUID().toString();
			}
			
			clientToUuidMap.put(client, userUUID);
			uuidToClientMap.put(userUUID, client);
		}
	}
    
    @Override
    public void processInvitationAcceptance(Connection cn, String invitationCode) throws SQLException, ValidationException{

    	Invitation invitation = Invitation.load(cn, invitationCode);
    	
    	invitation.setAccepted(true);
    	invitation.setDateAccepted(LocalDateTime.now());
    	
    	invitation.update(cn);
    	
    	//TODO here can check for newUser type and handle for other scenarios such as when a therapist invites another therapist
    	dao.therapistCreateClientConnection(cn, invitation.getSenderUserID(), this.getUserID());
    	
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

    
}
