package com.cggcoding.utils.messaging;

public final class ErrorMessages {

	//Database errors
		public static final String GENERAL_DB_ERROR = "There seems to be a problem accessing your information from the database.  Please try again later.";
		
	//Log in
	public static final String INVALID_USERNAME_OR_PASSWORD = "Login failed.  The email and password combination is not found in our records.  Please try again.";

	//Treatment Plan Creation Stage One - Choosing plan name, description, and treatment issue
	public static final String PLAN_MISSING_INFO = "You must enter a plan name and description.";
	public static final String PLAN_TITLE_EXISTS = "That treatment plan name already exists in your profile.  Please choose another one.";
	public static final String ISSUE_MULTIPLE_SELECTED = "Invalid condition. Multiple treatment issues are selected. Ensure that only 1 treatment issue is selected.";
	public static final String ISSUE_NONE_SELECTED = "Invalid condition.  You must select a treatment issue for this plan";
	public static final String ISSUE_NAME_EXISTS = "The treatment issue already exists in the default choices or in your profile.  Please choose another name for the treatment issue.";
	public static final String ISSUE_NAME_MISSING = "You entered a blank treatment issue.  Please try again.";
	public static final String STAGE_TITLE_EXISTS = "The stage name you entered already exists in your profile. Please use another name. If you'd like to view or edit the existing task, <a href=#>click here</a>";

	//Stages
	public static final String STAGE_GOAL_VALIDATION_ERROR = "You must enter a goal description.";
	public static final String STAGE_UPDATE_NO_SELECTION = "Please select and load a stage.";
	
	//Tasks
	public static final String TASK_MISSING_INFO = "You must select a task type as well as enter a task name and instructions.";
	public static final String TASK_INVALID_ID = "There appears to be a problem with your task.  Please email customer support with a detailed explanation of what happened.";
	public static final String TASK_TITLE_EXISTS_FOR_STAGE = "That task title already exists for this stage.  Please choose a different title.";
	
	
	
}
