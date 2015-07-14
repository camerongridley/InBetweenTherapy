package com.cggcoding.models;

import java.util.Date;

public abstract class Task implements Completable{
	private int id;
	private String name;
	private String description;
	private boolean completed;
	private Date dateCompleted;
	
	public Task(int id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = false;
	}
	
	public int getId(){
		return id;
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
