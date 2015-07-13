package com.cggcoding.models;

import java.util.Date;

public abstract class Task implements Completable{
	private int id;
	private String name;
	private String description;
	private boolean completed;
	private Date dateCompleted;
	
	public Task(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDateCompleted(Date date){
		dateCompleted = date;
	}
	
	public Date getDateCompleted(){
		return dateCompleted;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}
	
	@Override
	public void markComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markIncomplete() {
		// TODO Auto-generated method stub
		
	}

	
	
}
