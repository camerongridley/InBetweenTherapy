package com.cggcoding.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.utils.database.DatabaseActionHandler;
import com.cggcoding.utils.database.MySQLActionHandler;

/**
 * Created by cgrid_000 on 8/8/2015.
 */
public class UserTherapist extends User implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Integer, UserClient> clientMap;
    private List<TreatmentIssue> coreTreatmentIssues;
    private List<TreatmentPlan> allAssignedClientPlans;
    private List<TreatmentPlan> activeAssignedClientPlans;
    private List<TreatmentPlan> unstartedAssignedClientPlans;
    private List<TreatmentPlan> completedAssignedClientPlans;

    private static DatabaseActionHandler dao= new MySQLActionHandler();
        
    public UserTherapist(int userID, String userName, String firstName, String lastName, String email){
        super(userID, userName, firstName, lastName, email);
        this.clientMap = new HashMap<>();
        this.coreTreatmentIssues = new ArrayList<>();
        this.allAssignedClientPlans = new ArrayList<>();
    }

	public Map<Integer, UserClient> getClientMap() {
		return clientMap;
	}

	public void setClientMap(Map<Integer, UserClient> clientMap) {
		this.clientMap = clientMap;
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
    	this.unstartedAssignedClientPlans = new ArrayList<>();
    	for(TreatmentPlan plan : allAssignedClientPlans){
    		if(plan.isCompleted()){
    			completedAssignedClientPlans.add(plan);
    		}
    	}
    	
    	return completedAssignedClientPlans;
    }
    
    public List<TreatmentPlan> loadUnstartedAssignedClientTreatmentPlans(){
    	this.completedAssignedClientPlans = new ArrayList<>();
    	for(TreatmentPlan plan : allAssignedClientPlans){
    		if(!plan.isCompleted() && !plan.isInProgress()){
    			unstartedAssignedClientPlans.add(plan);
    		}
    	}
    	
    	return unstartedAssignedClientPlans;
    }
    
}
