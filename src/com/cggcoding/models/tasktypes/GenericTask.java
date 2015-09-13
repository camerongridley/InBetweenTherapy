package com.cggcoding.models.tasktypes;

import com.cggcoding.models.Task;
import com.cggcoding.models.Updateable;

public class GenericTask extends Task implements Updateable {

	public GenericTask(int taskID, int userID, int parentTaskID, String name, String instructions) {
		super(taskID, userID, parentTaskID, name, instructions);
		// TODO Auto-generated constructor stub
	}

	public GenericTask(int taskID, int userID, String name, String instructions) {
		super(taskID, userID, name, instructions);
		// TODO Auto-generated constructor stub
	}

	public GenericTask(int taskID, int userID) {
		super(taskID, userID);
		// TODO Auto-generated constructor stub
	}



}
