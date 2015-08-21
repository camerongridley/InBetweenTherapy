package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;
import com.cggcoding.models.Updateable;

public class PsychEdTask extends Task implements Updateable{

	public PsychEdTask (int taskID) {
		super(taskID);
	}

	//title of book or website...
	//specifier states chapter, page numbers, site section...
	public PsychEdTask(int taskID, String title, String specifier, int repetitions) {
		super(taskID, title, specifier, repetitions);
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
