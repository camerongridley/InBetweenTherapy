package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.StageGoal;
import com.cggcoding.models.MapStageTaskTemplate;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;
import com.cggcoding.utils.messaging.WarningMessages;

/**
 * Servlet implementation class EditStage
 */
@WebServlet("/secure/EditStage")
public class EditStage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditStage() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		int clientUserID = ParameterUtils.parseIntParameter(request, "clientUserID");
		request.setAttribute("clientUserID", clientUserID);
		/*-----------End Common Servlet variables---------------*/
		
		int originalOrder = ParameterUtils.parseIntParameter(request, "templateTaskOrder");
		String stageTitle = request.getParameter("stageTitle");
		String stageDescription = request.getParameter("stageDescription");
		Stage editedStage = null;
		/*This variable helps remember where to send the user back to when they are done editing the Task.
		If the Task being edited is a template the stageID will be TEMPLATE_HOLDER_ID, and not the Stage template being working on.
		If the Task being edited is part of a client's plan, then the stageID will be the stageID that is contained within the task.
		Need to maintain it between requests*/
		int planToReturnTo = treatmentPlanID;
		String mainMenu = "";
		request.setAttribute("treatmentPlanID", planToReturnTo);
		
		try{
			request.setAttribute("defaultStageList", Stage.getDefaultStages());
			
			if(user.hasRole(Constants.USER_ADMIN) || user.hasRole(Constants.USER_THERAPIST)){
								
				switch (requestedAction){
		            case "stage-edit-start" :
		            	forwardTo = Constants.URL_EDIT_STAGE;
		            	break;
		            	
		            case "stage-edit-select-stage" :
		            	if(stageID != 0){
		            		request.setAttribute("stage", Stage.load(stageID));
		            	} else {
		            		request.setAttribute("stage", null);
		            	}
		            	
		            	if(path.equals("treatmentPlanTemplate")){
							request.setAttribute("warningMessage", WarningMessages.EDITING_STAGE_TEMPLATE);
						}
		            	
		            	forwardTo = Constants.URL_EDIT_STAGE;
		            	break;
		            	
		            case "stage-edit-basic-info" :
		            	if(stageID==0){
		            		throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
		            	}

		            	editedStage = Stage.load(stageID);
		            	editedStage.setTitle(stageTitle);
		            	editedStage.setDescription(stageDescription);
		            	
		            	for(StageGoal goal: editedStage.getGoals()){
		            		goal.setDescription(request.getParameter("stageGoalDescription" + goal.getStageGoalID()));
		            	}
		            	
		            	if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN)){
		            		//get the repetition value for each task template inside this stage template and set it the edited stage to be updated when stage.update() is called
			            	for(MapStageTaskTemplate stageTaskInfo : editedStage.getMapStageTaskTemplates()){
			            		int templateReps = ParameterUtils.parseIntParameter(request, "taskTemplateRepetitions" + stageTaskInfo.getTaskID());
			            		stageTaskInfo.setTemplateRepetitions(templateReps);
			            		//get order info from request and set in stageTaskInfo here if decide to change so order is a dropdown choice
			            	}	
		            	}
		            	
		            	editedStage.update();//OPTIMIZE could create a new method that takes all relevant info and calls static method in stage that loads and updates all with the same connection
		            	
		            	request.setAttribute("stage", editedStage);
		            	request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
		            	
		            	
		            	//FIXME  This is all fucked up.  Really need to reorganize this.  They are not using the right logic or evaluating the proper conditions.
		            	if(path.equals("treatmentPlanTemplate")){
		            		
		            		request.setAttribute("treatmentPlan", TreatmentPlan.load(planToReturnTo));
		            		request.setAttribute("defaultTreatmentIssues", TreatmentIssue.getDefaultTreatmentIssues());
		            		CommonServletFunctions.setDefaultTreatmentPlansInRequest(request);
		            		
		            		forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
		            	}else if(path.equals(Constants.PATH_CLIENT_TREATMENT_PLAN)){
		            		if(user.hasRole(Constants.USER_THERAPIST)){
			            		UserTherapist userTherapist = (UserTherapist)user;
			    				
			    				//set the default treatment plans and the custom plans for this therapist into the request
			    				request.setAttribute("defaultTreatmentPlanList", TreatmentPlan.getDefaultTreatmentPlans());
			    				
			                	userTherapist.loadAllAssignedClientTreatmentPlans(clientUserID);
			            		request.setAttribute("activeAssignedClientPlans", userTherapist.loadActiveAssignedClientTreatmentPlans());
			            		request.setAttribute("unstartedAssignedClientPlans", userTherapist.loadUnstartedAssignedClientTreatmentPlans());
			            		request.setAttribute("completedAssignedClientPlans", userTherapist.loadCompletedAssignedClientTreatmentPlans());
			            		
			            		CommonServletFunctions.setClientInRequest(request, userTherapist, clientUserID);
			            		
			            		mainMenu = Constants.URL_EDIT_TREATMENT_PLAN;
			            	}
						}else{
							if(user.hasRole(Constants.USER_ADMIN)){
								forwardTo = Constants.URL_ADMIN_MAIN_MENU;
							}
							
							if(user.hasRole(Constants.USER_THERAPIST)){
								forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
							}
		            	}

		            	break;
		            	
		            case "stage-edit-add-goal" :
		            	addGoal(request, stageID);
		            	forwardTo = Constants.URL_EDIT_STAGE;
		            	break;
		            	
		            case("delete-goal"):
						int goalID = ParameterUtils.parseIntParameter(request, "stageGoalID");
						StageGoal.delete(goalID);
						
						editedStage = Stage.load(stageID);
						request.setAttribute("stage", editedStage);
		            	forwardTo = Constants.URL_EDIT_STAGE;
						break;
						
					case("delete-task"):
						deleteTask(request, stageID);
					
		            	forwardTo = Constants.URL_EDIT_STAGE;
						break;
					
					case("increase-task-order"):					
						
						editedStage = Stage.load(stageID);
						
						editedStage.orderIncrementTemplateTask(taskID, originalOrder);
						
						request.setAttribute("stage", editedStage);
						forwardTo = Constants.URL_EDIT_STAGE;
						break;
						
					case("decrease-task-order"):
						editedStage = Stage.load(stageID);
					
						editedStage.orderDecrementTemplateTask(taskID, originalOrder);
						
						request.setAttribute("stage", editedStage);
						forwardTo = Constants.URL_EDIT_STAGE;
						break;
		            default:

		                forwardTo = Constants.URL_ADMIN_MAIN_MENU;
				}
			} else if(user.hasRole(Constants.USER_CLIENT)){
				forwardTo = "clientMainMenu.jsp";
				request.setAttribute("erorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
				//UNSURE consider creating a UnauthorizedAccessException and throwing that here
			}
				
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("errorMessage", e.getMessage());
			
			request.setAttribute("stage", editedStage);
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("treatmentPlanID", planToReturnTo);
            forwardTo = Constants.URL_EDIT_STAGE;
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}
	
	//TODO deprecated? delete?
	/**Gets all data that is associated with stageTaskDetailsMaps - i.e. tasks that are part of a stage template
	 * @param request
	 * @param stage
	 * @return
	 */
	private List<MapStageTaskTemplate> retrieveStageTaskDetails(HttpServletRequest request, Stage stage){
		List<MapStageTaskTemplate> stageTaskDetails = new ArrayList<>();
		String[] taskIDs = request.getParameterValues("allTaskIDs");
		for(int i = 0; i < taskIDs.length; i++){
			System.out.println(taskIDs[i]);
			
		}
		
		for(Task task : stage.getTasks()){
			int templateOrder = ParameterUtils.parseIntParameter(request, "templateTaskOrder" + task.getTaskID());
			int templateRepetitions = ParameterUtils.parseIntParameter(request, "taskRep" + task.getTaskID());;
			stageTaskDetails.add(new MapStageTaskTemplate(stage.getStageID(), task.getTaskID(), templateOrder, templateRepetitions));
		}
		
		return null;
		
	}
	
	private void updateStageBasicInfo(){
		
	}
	
	/**Deletes task from the stage (Stage determines if deleted from task table or mapping table).  This method gets the taskID to delete from the request.
	 * @param request
	 * @param stageID
	 * @param taskID
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	private void deleteTask(HttpServletRequest request, int stageID) throws DatabaseException, ValidationException{
		Stage editedStage = Stage.load(stageID);
		int taskToDeleteID = ParameterUtils.parseIntParameter(request, "taskID");
		editedStage.deleteTask(taskToDeleteID);
		
		request.setAttribute("stage", editedStage);
	}
	
	/**Gets the new goal description from the request and creates the new goal for the stage specified by stageID.  Finally it puts the updated stage in the request as attribute "stage".
	 * @param request
	 * @param stageID
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	private void addGoal(HttpServletRequest request, int stageID) throws ValidationException, DatabaseException{
		String goalDescription = request.getParameter("newStageGoalDescription");
    	StageGoal goal = StageGoal.getInstanceWithoutID(stageID, goalDescription);
    	goal.create();
    	request.setAttribute("stage", Stage.load(stageID));
	}

}
