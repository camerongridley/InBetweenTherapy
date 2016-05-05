package com.cggcoding.utils.messaging;

public class WarningMessages {

	public WarningMessages() {
		
	}
	
	public static final String EDITING_TASK_TEMPLATE = "You are editing a template, so any changes you make to this Task will propagate to all Stage templates that use this Task.  Please edit with care.";
	public static final String EDITING_STAGE_TEMPLATE = "You are editing a template, so any changes you make to this Stage will propagate to all Treatment Plan templates that use this Stage.  Please edit with care.";
	public static final String CLIENT_TREATMENT_PLAN_DISABLED = "This plan is currently disabled.  Please select it as your primary treatment plan in your Treatment Plan Management page to enable it.";

}
