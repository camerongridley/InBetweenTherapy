package com.cggcoding.utils.messaging;

public final class ErrorMessages {
	
	//Log in
	public static final String INVALID_USERNAME_OR_PASSWORD = "Login failed.  The email and password combination is not found in our records.  Please try again.";

	//Treatment Plan Creation Stage One - Choosing plan name, description, and treatment issue
	public static final String PLAN_NAME_EXISTS = "That treatment plan name already exists in your profile.  Please choose another one.";
	public static final String USER_SELECTED_MULTIPLE_ISSUES = "Invalid condition. Multiple treatment issues are selected. Ensure that only 1 treatment issue is selected.";
	public static final String USER_SELECTED_NO_ISSUE = "Invalid condition.  You must select a treatment issue for this plan";
	public static final String ISSUE_NAME_EXISTS = "The new custom treatment issue already exists in your profile.  Please choose another name for the treatment issue.";
	public static final String STAGE_NAME_EXISTS = "The stage name you entered already exists in your profile. Please use another name. If you'd like to view or edit the existing task, <a href=#>click here</a>";

	
	//Database errors
	public static final String GENERAL_DB_ERROR = "There seems to be a problem accessing your information from the database.  Please try again later.";
	
}
