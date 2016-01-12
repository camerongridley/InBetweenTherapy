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
    private List<TreatmentIssue> defaultTreatmentIssues;

    private static DatabaseActionHandler dao= new MySQLActionHandler();
        
    public UserTherapist(int userID, String email){
        super(userID, email);
        this.clientMap = new HashMap<>();
        this.defaultTreatmentIssues = new ArrayList<>();
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
    
    public Map<Integer, UserClient> loadClients() throws DatabaseException{
    	this.clientMap = dao.userGetClientsByTherapistID(this.getUserID());
    	return clientMap;
    }
    
    public List<TreatmentPlan> getAssignedClientTreatmentPlans(int clientID) throws DatabaseException, ValidationException{
    	return dao.userGetTherapistAssignedPlans(clientID, this.getUserID());
    }
    
}
