package com.cggcoding.models;

import java.time.LocalDate;


public abstract class Task implements Completable{
	private int id;
	private String name;
	private String description;
	private boolean completed;
	private LocalDate dateCompleted;
	
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
	
	public void setDateCompleted(LocalDate date){
		dateCompleted = date;
	}
	
	public LocalDate getDateCompleted(){
		return dateCompleted;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}
	
	@Override
	public void markComplete() {
		completed = true;
		dateCompleted = LocalDate.now();
	}

	@Override
	public void markIncomplete() {
		completed = false;
	}

	
	
}
