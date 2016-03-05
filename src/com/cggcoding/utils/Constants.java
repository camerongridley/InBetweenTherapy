package com.cggcoding.utils;

import com.cggcoding.models.TaskGeneric;
import com.cggcoding.models.TaskTwoTextBoxes;

public class Constants {

	public Constants() {
		
	}
	
	public static final int ADMIN_ROLE_ID = 1;
	public static final int THERAPIST_ROLE_ID = 2;
	public static final int CLIENT_ROLE_ID = 3;
	
	public static final String USER_ADMIN = "admin";
	public static final String USER_CLIENT = "client";
	public static final String USER_THERAPIST = "therapist";
	
	//The defaults holder id is the primary key value for any database models that are responsible for holding templates of child classes.
	//This primarily refers to the TreatmentPlan Stage Defaults Holder Plan and the Stage Task Defaults Holder Plan.
	public static final int TEMPLATES_HOLDER_PRIMARY_KEY_ID = 1;
	
	public static final int TEMPLATE_ORDER_NUMBER = 0;
	
	
	//Task Type IDs - This should list all of the task types and their primary key values
	public static final int TASK_TYPE_ID_GENERIC_TASK = 1;
	public static final int TASK_TYPE_ID_TWO_TEXTBOXES_TASK = 2;
	
	private static final String TASK_TYPE_NAME_TASK_GENERIC = new TaskGeneric().getTaskTypeName();
	private static final String TASK_TYPE_NAME_TASK_TWO_TEXTBOXES = new TaskTwoTextBoxes().getTaskTypeName();
	
	//These methods allow access to these values via JSTL when this class is put into the context
	public static final String getTaskTypeNameTaskGeneric() {
		return TASK_TYPE_NAME_TASK_GENERIC;
	}
	public static final String getTaskTypeNameTaskTwoTextboxes() {
		return TASK_TYPE_NAME_TASK_TWO_TEXTBOXES;
	}
	
	//Paths
	public static final String PATH_TEMPLATE_TREATMENT_PLAN = "templateTreatmentPlan";
	public static final String PATH_TEMPLATE_STAGE = "templateStage";
	public static final String PATH_TEMPLATE_TASK = "templateTask";
	
	public static final String PATH_MANAGE_CLIENT = "manageClients";
	public static final String PATH_CLIENT_TREATMENT_PLAN = "clientTreatmentPlan";
	public static final String PATH_CLIENT_STAGE = "clientStage";

	//URLs
	public static final String URL_ADMIN_MAIN_MENU = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
	public static final String URL_THERAPIST_MAIN_MENU = "/WEB-INF/jsp/therapist-tools/therapist-main-menu.jsp";
	public static final String URL_CLIENT_MAIN_MENU = "/WEB-INF/jsp/client-tools/client-main-menu.jsp";
	
	public static final String URL_THERAPIST_MANAGE_CLIENT_MAIN = "/WEB-INF/jsp/therapist-tools/manage-clients-main.jsp";
	public static final String URL_THERAPIST_MANAGE_CLIENT_PLANS = "/WEB-INF/jsp/therapist-tools/manage-client-plans.jsp";
	
	public static final String URL_CLIENT_SELECT_PLAN = "/WEB-INF/jsp/client-tools/select-plan.jsp";
	
	public static final String URL_CREATE_TREATMENT_PLAN = "/WEB-INF/jsp/treatment-plans/treatment-plan-create.jsp";
	public static final String URL_CREATE_STAGE = "/WEB-INF/jsp/treatment-plans/stage-create.jsp";
	public static final String URL_CREATE_TASK = "/WEB-INF/jsp/treatment-plans/task-create.jsp";
	public static final String URL_EDIT_TREATMENT_PLAN = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
	public static final String URL_EDIT_STAGE = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
	public static final String URL_EDIT_TASK = "/WEB-INF/jsp/treatment-plans/task-edit.jsp";

	public static final String URL_INDEX = "/index.jsp";
	public static final String URL_LOGIN = "/login.jsp";
	public static final String URL_REGISTRATION = "/registration.jsp";

	public static final String URL_RUN_TREATMENT_PLAN = "/WEB-INF/jsp/client-tools/run-treatment-plan.jsp";
	public static final String URL_STAGE_COMPLETE = "/WEB-INF/jsp/client-tools/stage-complete.jsp";
	
	public static final String URL_ERROR_GENERAL = "/WEB-INF/jsp/error.jsp";
	public static final String URL_ERROR_UNAUTHORIZED_ACCESS = "/WEB-INF/jsp/unauthorized-access.jsp";
	
	
	
}
