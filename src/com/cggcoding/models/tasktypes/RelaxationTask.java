package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;

public class RelaxationTask extends Task {
	
	private int durationInMinutes;

	public RelaxationTask(int taskID){
		super(taskID);
	}

	public RelaxationTask(int taskID, String name, String description, int duration) {
		super(taskID, name, description);
		durationInMinutes = duration;
	}
	
	public int getDurationInMinutes(){
		return this.durationInMinutes;
	}
	
}
