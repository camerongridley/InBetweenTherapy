package com.cggcoding.utils;

import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.TaskTwoTextBoxes;

public class Constants {

	public Constants() {
		
	}
	
	public static final int ADMIN_ROLE_ID = 1;
	
	public static final String USER_ADMIN = "admin";
	public static final String USER_CLIENT = "client";
	public static final String USER_THERAPIST = "therapist";
	
	//The defaults holder id is the primary key value for any database models that are responsible for holding templates of child classes.
	//This primarily refers to the TreatmentPlan Stage Defaults Holder Plan and the Stage Task Defaults Holder Plan.
	public static final int DEFAULTS_HOLDER_PRIMARY_KEY_ID = 1;
	
	public static final int TEMPLATE_ORDER_NUMBER = 0;
	
	
	//Task Type IDs - This should list all of the task types and their primary key values
	public static final int TASK_TYPE_ID_GENERIC_TASK = 1;
	public static final int TASK_TYPE_ID_TWO_TEXTBOXES_TASK = 2;
	
	private static final String TASK_TYPE_NAME_TASK_GENERIC = new TaskGeneric().getTaskTypeName();
	private static final String TASK_TYPE_NAME_TASK_TWO_TEXTBOXES = new TaskTwoTextBoxes().getTaskTypeName();
	
	//These methods allow access to these values via JSTL when this class is put into the context
	public static String getTaskTypeNameTaskGeneric() {
		return TASK_TYPE_NAME_TASK_GENERIC;
	}
	public static String getTaskTypeNameTaskTwoTextboxes() {
		return TASK_TYPE_NAME_TASK_TWO_TEXTBOXES;
	}
	
	//Paths
	public static String PATH_TEMPLATE_TREATMENT_PLAN = "templateTreatmentPlan";
	public static String PATH_TEMPLATE_STAGE = "templateStage";
	public static String PATH_TEMPLATE_TASK = "templateTask";
	
	public static String PATH_CLIENT_TREATMENT_PLAN = "clientTreatmentPlan";

	//URLs
	public static String ADMIN_MAIN_MENU = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
	public static String THERAPIST_MAIN_MENU = "/WEB-INF/jsp/therapist-tools/therapist-main-menu.jsp";
	public static String CLIENT_MAIN_MENU = "/WEB-INF/jsp/client-tools/client-main-menu.jsp";
	
	public static String THERAPIST_MANAGE_CLIENT_MAIN = "/WEB-INF/jsp/therapist-tools/manage-clients-main.jsp";
	
	public static String CREATE_TREATMENT_PLAN = "/WEB-INF/jsp/treatment-plans/treatment-plan-create.jsp";
	public static String CREATE_STAGE = "/WEB-INF/jsp/treatment-plans/stage-create.jsp";
	public static String CREATE_TASK = "/WEB-INF/jsp/treatment-plans/task-create.jsp";
	public static String EDIT_TREATMENT_PLAN = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
	public static String EDIT_STAGE = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
	public static String EDIT_TASK = "/WEB-INF/jsp/treatment-plans/task-edit.jsp";
	

	
	
}
