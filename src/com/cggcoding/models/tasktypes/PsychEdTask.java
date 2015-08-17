package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;

public class PsychEdTask extends Task {

	public PsychEdTask (int taskID) {
		super(taskID);
	}

	//title of book or website...
	//specifier states chapter, page numbers, site section...
	public PsychEdTask(int taskID, String title, String specifier, int repetitions) {
		super(taskID, title, specifier, repetitions);
	}

}
