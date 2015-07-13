package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;

public class RelaxationTask extends Task {
	
	private int durationInMinutes;
	
	public RelaxationTask(String name, String description, int duration) {
		super(name, description);
		durationInMinutes = duration;

	}

}
