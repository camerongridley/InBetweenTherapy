package com.cggcoding.models;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private int userID;
	private String email;
	private List<String> roles;
	
	public User (int userID, String email){
		this.userID = userID;
		this.email = email;
		roles = new ArrayList<>();
	}

	public List<String> getRoles(){
		return roles;
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

	public String getEmail() {
		return email;
	}
}
