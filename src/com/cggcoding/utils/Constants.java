package com.cggcoding.utils;

public class Constants {

	public Constants() {
		
	}
	
	public static final String USER_ADMIN = "admin";
	public static final String USER_CLIENT = "client";
	public static final String USER_THERAPIST = "therapist";
	
	//The template holder id is the primary key value for any database models that are responsible for holding templates of child classes.
	//This primarily refers to the TreatmentPlan Stage Defaults Holder Plan and the Stage Task Defaults Holder Plan.
	public static final int DEFAULTS_HOLDER_PRIMARY_KEY_ID = 1;
	
	public static final int TEMPLATE_ORDER_NUMBER = 0;
	
	
	//Task Type IDs - This should list all of the task types and their primary key values
	public static final int TASK_TYPE_ID_GENERIC_TASK = 1;
	public static final int TASK_TYPE_ID_TWO_TEXTBOXES_TASK = 2;
	
}
