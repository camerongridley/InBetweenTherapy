package com.cggcoding.utils.messaging;

public final class SuccessMessages {

	public SuccessMessages() {
	}
	
	//Log in
	//public static final String INVALID_USERNAME_OR_PASSWORD = "Login failed.  The email and password combination is not found in our records.  Please try again.";

	//Treatment Plan Creation Stage One - Choosing plan name, description, and treatment issue
	public static final String TREATMENT_PLAN_TEMPLATE_CREATE = "The treatment plan template has been successfully added to your profile.";
	public static final String TREATMENT_PLAN_UPDATED = "The treatment plan has been saved.";
	public static final String TREATMENT_PLAN_COMPLETED = "Congratulations!  You have completed treamtment. You are free to go back and review or repeat any of the stages or tasks if desired.";
	public static final String TREATMENT_PLAN_DELETED = "The treatment plan has been deleted.";
	public static final String TREATMENT_PLAN_COPIED_TO_CLIENT = "The treatment plan has successfully been assigned and copied into the client's profile.";
	
	//Stage Creation
	public static final String STAGE_TEMPLATE_BASIC_CREATE = "The basic stage template has been successfully added to your profile.";
	public static final String STAGE_ADDED_TO_TREATMENT_PLAN = "You have added the stage to your treatment plan.";
	public static final String STAGE_UPDATED = "The stage has been saved.";
	
	//Task Creation
	public static final String TASK_TEMPLATE_CREATE = "The task template has been successfully added to your profile.";
	public static final String TASK_ADDED_TO_STAGE = "The task has been successfully added to the stage.";
	public static final String TASK_UPDATED = "The task has been saved.";
	
	//INVITATIONS
	public static String INVITATION_SENT_SUCCESS = "The invitation was sent successfully.";

}
