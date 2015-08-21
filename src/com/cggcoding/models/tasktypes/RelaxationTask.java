package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;
import com.cggcoding.models.Updateable;

public class RelaxationTask extends Task implements Updateable{
	
	private int durationInMinutes;

	public RelaxationTask(int taskID){
		super(taskID);
	}

	public RelaxationTask(int taskID, String name, String description, int repetitions, int duration) {
		super(taskID, name, description, repetitions);
		durationInMinutes = duration;
	}
	
	public int getDurationInMinutes(){
		return this.durationInMinutes;
	}

	@Override
	public boolean updateData(Task taskWithNewData) {
		//update universal properties
		this.setCompleted(taskWithNewData.isCompleted());
		this.setDateCompleted(taskWithNewData.getDateCompleted());

		//updateCogTask DB call goes here

		return true;//TODO returns true if DB update was success
	}
}
