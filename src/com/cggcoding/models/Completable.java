package com.cggcoding.models;

public interface Completable {
	
	void markComplete();
	
	void markIncomplete();
	
	boolean isCompleted();
}
