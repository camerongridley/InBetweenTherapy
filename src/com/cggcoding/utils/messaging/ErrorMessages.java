package com.cggcoding.utils.messaging;

public final class ErrorMessages {

	//Database errors
	public static final String CONNECTION_IS_NULL = "Unable to establish a connection to the database.";	
	public static final String GENERAL_DB_ERROR = "There seems to be a problem performing the database operation you requested.  Please try again or contact customer support.";
	public static final String ROLLBACK_DB_OP = "An error has occured while performing the database opertion you request and the data has been rolled back to its previous state.";
	public static final String ROLLBACK_DB_ERROR = "An error has occured while performing the database opertion you request and also a problem reverting your data back to it's original state.  Please contact customer support.";
		
	//General Validation errors
	public static final String GENERAL_VALIDATION_ERROR = "There was an error validating your request.  Please try againg or contact customer support.";
	public static final String VALIDATION_ERROR_UPDATING_DATA = "The data you are trying to update contains invalid information.  Please try again.";
	public static final String DEFAULTS_HOLDER_ID_SELECTED = "It appears you are trying to access restricted data.  Please be sure to make a valid selection and try again.";
	public static final String OBJECT_IS_NULL = "It appears there is no database record for what you are looking for.  Please be sure to make a valid selection and try again.  If you have further problems please contact customer support.";
	public static final String NOTHING_SELECTED = "You have not selected anything.  Please make a selection and try again.";
	public static final String INVALID_SELECTION = "Something is missing!  There seems to be some missing information on our end that is needed to do what you're asking.  Please try again or contact customer support.";
	
	
	//Log in
	public static final String INVALID_USERNAME_OR_PASSWORD = "Login failed.  The email and password combination is not found in our records.  Please try again.";
	
	//Authorization
	public static final String UNAUTHORIZED_ACCESS = "You do not have the appropriate authorization for the requested action.  If you need further assistance, plase contact customer support.";
	
	//Treatment Plan Creation Stage One - Choosing plan name, description, and treatment issue
	public static final String PLAN_MISSING_INFO = "You must enter a plan name and description.";
	public static final String PLAN_TITLE_EXISTS = "That treatment plan name already exists in your profile.  Please choose another one.";
	public static final String ISSUE_MULTIPLE_SELECTED = "Invalid condition. Multiple treatment issues are selected. Ensure that only 1 treatment issue is selected.";
	public static final String ISSUE_NONE_SELECTED = "Invalid condition.  You must select a treatment issue for this plan";
	public static final String ISSUE_NAME_EXISTS = "The treatment issue already exists in the default choices or in your profile.  Please choose another name for the treatment issue.";
	public static final String ISSUE_NAME_MISSING = "You entered a blank treatment issue.  Please try again.";
	public static final String STAGE_TITLE_EXISTS = "The stage name you entered already exists in your profile. Please use another name. If you'd like to view or edit the existing task, <a href=#>click here</a>";
	public static final String PLAN_DELETE_ERROR = "There is no treatment plan selected to delete.";
	public static final String STAGES_IS_EMPTY = "There are no stages in this treatment plan. A plan must have at least one stage to be assigned to a client.";
	public static final String PLAN_IS_NOT_TEMPLATE = "You appear to be trying to add a stage template to a treatment plan that is not a template.  This is not allowed.  Please try again or contact customer support.";
	public static final String PLAN_CONTAINS_STAGE_TEMPLATE = "This treatment plan template already contains the stage you've chosen.  Go to the Edit Stage page if you'd like to edit this stage's template.";

	//Stages
	public static final String STAGE_GOAL_VALIDATION_ERROR = "You must enter a goal description.";
	public static final String STAGE_UPDATE_NO_SELECTION = "Please select and load a stage.";
	public static final String STAGE_TITLE_DESCRIPTION_MISSING = "You must enter a stage name and description.";
	public static final String STAGE_IS_NOT_TEMPLATE = "You appear to be trying to add a task template to a stage that is not a template.  This is not allowed.  Please try again or contact customer support.";
	public static final String STAGE_CONTAINS_TASK_TEMPLATE = "This stage template already contains the task you've chosen.  Go to the Edit Task page to increase the number of repetitions for this task.";
	public static final String STAGE_CLIENT_ONLY_ALLOWED_IN_PLAN_TEMPLATE = "Client Stages can only be added to Treatment Plans that are NOT templates.  It appears the Treatment Plan you have loaded is a template.  Please try again.";
	public static final String STAGE_LOCKED = "This stage is locked and cannot be saved.  Make sure you have completed previous stages in order to unlock and be able to make changes to this stage.";
	
	//Tasks
	public static final String TASK_MISSING_INFO = "You must select a task type as well as enter a task name and instructions.";
	public static final String TASK_INVALID_ID = "There appears to be a problem with your task.  Please email customer support with a detailed explanation of what happened.";
	public static final String TASK_TITLE_EXISTS_FOR_STAGE = "That task title already exists for this stage.  Please choose a different title.";
	
	//User
	public static final String NO_PLAN_FOUND_FOR_USER = "That treatment plan cannot be found for this user.  Please try again or contact customer support";
	
	//StageTaskTemplateMap
	public static final String TASK_IS_FIRST = "You cannot increase this tasks order any more as it is already first.";
	public static final String TASK_IS_LAST = "You cannot decrease this tasks order any more as it is already last.";
	
	//Other
	public static final String USER_NOT_FOUND = "We cannont find the user you are looking for.  Please try again or contact customer support.";
	public static final String PASSWORDS_DONT_MATCH = "The passwords you entered do not match.  Please try again.";
	public static final String USERNAME_ALREADY_EXISTS = "That username already exists.  Please pick another one.";
	public static final String MISSING_USER_INFORMATION = "Oops! It looks like we are missing some information that is needed to register.  Please fill out all of the required fields.";
	public static final String EMAIL_ALREADY_EXISTS = "That email already is already being used by another user.  Please pick another one or contact customer support for further assistance.";
	
	public static final String INVITATION_NOT_FOUND = "There was no invitation found with that invitation code.  Please try again or contact the person who invited you to request a new invitation.";
	public static final String INVITATION_INVALID_EMAIL_ADDRESS = "The email address you entered is invalid.  The invitation was not sent.  Please try again.";
	public static final String INVITATION_UNSUCCESSFUL_SEND = "The invitation email failed to send properly.  Please check the email address you are sending to and try again.";
	public static final String INVITATION_ALREADY_INVITED = "You have already sent that person an invitation.";
	public static final String INTIVATION_USER_ALREADY_REGISTERED = "A user with that email address is already registered.";
	
	
	
	
}
