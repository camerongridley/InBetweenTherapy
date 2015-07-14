package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;

public class RelaxationTask extends Task {
	
	private int durationInMinutes;
	
	public RelaxationTask(int id, String name, String description, int duration) {
		super(id, name, description);
		durationInMinutes = duration;

	}

}
