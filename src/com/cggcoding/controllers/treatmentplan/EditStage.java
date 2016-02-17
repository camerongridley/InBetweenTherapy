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
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		/*-----------Common Treatment Plan object variables------------*/
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		TreatmentPlan treatmentPlan = null;
		Stage stage = null;
		Task task = null;
		int ownerUserID = 0;
		User owner = null;
		/*-----------End Treatment Plan object variables---------------*/
		

		int originalOrder = ParameterUtils.parseIntParameter(request, "taskOrder");
		String stageTitle = request.getParameter("stageTitle");
		String stageDescription = request.getParameter("stageDescription");
		
		try{
    		//TODO make sure to remove ownerUserID and clientUserID from edit jsps since I have switched things to not need to maintain this value - get it from treatmentPlan/stage/task			
			//Here we check that a stage has been selected (currently the only time this is true isn't with path plan-edit-selection).
    		//If so, then load it and use it's userID prop to get it's owner
    		if(stageID != 0){
    			stage = Stage.load(stageID);
    			treatmentPlan = TreatmentPlan.loadBasic(treatmentPlanID);//only need basic info such as title so use loadBasic()
        		ownerUserID = stage.getUserID();
        		
        		//Set the User var "owner". If the owner of the plan that is being edited is different than the logged in user, then load the appropriate owner info
	    		if(ownerUserID==user.getUserID()){
	    			owner = user;
	    		} else {
	    			owner = User.loadBasic(stage.getUserID());
	    		}
	    		
	    		//if this Stage is a template, remind the user that all instances of this stage will be changed
	    		if(stage.isTemplate()){
					request.setAttribute("warningMessage", WarningMessages.EDITING_STAGE_TEMPLATE);
				}
    		}
    		
    		//OPTIMIZE move to only relevant requestedActions
			request.setAttribute("coreStagesList", Stage.getCoreStages());
			
			if(user.hasRole(Constants.USER_ADMIN) || user.hasRole(Constants.USER_THERAPIST)){
								
				switch (requestedAction){
		            case "stage-edit-start" :
		            	forwardTo = Constants.URL_EDIT_STAGE;
		            	break;
		            	
		            case "select-stage" :
		            	if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN)){
		            		request.setAttribute("treatmentPlan", TreatmentPlan.load(treatmentPlanID));
		            	}
		            	forwardTo = Constants.URL_EDIT_STAGE;
		            	break;
		            	
		            case "stage-edit-basic-info" :
		            	if(stageID==0){
		            		throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
		            	}

		            	stage.setTitle(stageTitle);
		            	stage.setDescription(stageDescription);
		            	
		            	for(StageGoal goal: stage.getGoals()){
		            		goal.setDescription(request.getParameter("stageGoalDescription" + goal.getStageGoalID()));
		            	}
		            	
		            	if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN) || path.equals(Constants.PATH_TEMPLATE_STAGE)){
		            		//get the repetition value for each task template inside this stage template and set it the edited stage to be updated when stage.update() is called
			            	for(MapStageTaskTemplate stageTaskInfo : stage.getMapStageTaskTemplates()){
			            		int templateReps = ParameterUtils.parseIntParameter(request, "taskTemplateRepetitions" + stageTaskInfo.getTaskID());
			            		stageTaskInfo.setTemplateTaskRepetitions(templateReps);
			            		//get order info from request and set in stageTaskInfo here if decide to change so order is a dropdown choice
			            	}	
		            	}
		            	
		            	stage.update();//OPTIMIZE could create a new method that takes all relevant info and calls static method in stage that loads and updates all with the same connection
		            	
		            	request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
		            	
		            	switch(path){
		            		//case for both Client and Template TreatmentPlan as well as ManageClient is the same
		            		case Constants.PATH_TEMPLATE_TREATMENT_PLAN:
		            		case Constants.PATH_CLIENT_TREATMENT_PLAN:
		            		case Constants.PATH_MANAGE_CLIENT:
		            			treatmentPlan =  TreatmentPlan.load(treatmentPlanID);
			            		request.setAttribute("defaultTreatmentIssues", TreatmentIssue.getDefaultTreatmentIssues());
			            		CommonServletFunctions.setDefaultTreatmentPlansInRequest(request);
			            		
			            		//forward to same place regardless of if user is an admin or therapist
			            		forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
			            		//make stage null since we are done with it and don't want it to be null in the request when returning to edit treatment plan url
			            		stage = null;
		            			break;
			            		
		            		case Constants.PATH_TEMPLATE_STAGE:
		            			if(user.getRole().equals(Constants.USER_ADMIN)){
			            			forwardTo = Constants.URL_ADMIN_MAIN_MENU;
			            		} else if(user.getRole().equals(Constants.USER_THERAPIST)){
			            			forwardTo = Constants.URL_THERAPIST_MAIN_MENU;
			            		}

		            			break;

		            		case Constants.PATH_CLIENT_STAGE:
		            			//this should only be a therapist, so only 1 place to forwardTo
		            			if(user.getRole().equals(Constants.USER_THERAPIST)){
		            				UserTherapist userTherapist = (UserTherapist)user;
				    				
				    				//set the default treatment plans and the custom plans for this therapist into the request
				    				request.setAttribute("coreTreatmentPlansList", TreatmentPlan.getCoreTreatmentPlans());
				    				
				                	userTherapist.loadAllAssignedClientTreatmentPlans(ownerUserID);
				            		request.setAttribute("activeAssignedClientPlans", userTherapist.loadActiveAssignedClientTreatmentPlans());
				            		request.setAttribute("unstartedAssignedClientPlans", userTherapist.loadUnstartedAssignedClientTreatmentPlans());
				            		request.setAttribute("completedAssignedClientPlans", userTherapist.loadCompletedAssignedClientTreatmentPlans());
				            						            		
			            			forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
			            		} else {
			            			forwardTo = Constants.URL_INDEX;
			            		}
		            			
		            			break;
		            	}
		            	
		            	
		            	request.setAttribute("warningMessage", null);
		            	
		            	break;
		            	
		            case "stage-edit-add-goal" :
		            	String goalDescription = request.getParameter("newStageGoalDescription");
		            	StageGoal goal = StageGoal.getInstanceWithoutID(stageID, goalDescription);
		            	stage.saveNewGoal(goal);
		            	
		            	forwardTo = Constants.URL_EDIT_STAGE;
		            	break;
		            	
		            case("delete-goal"):
						int goalID = ParameterUtils.parseIntParameter(request, "stageGoalID");
		            	stage.deleteGoal(goalID);

		            	forwardTo = Constants.URL_EDIT_STAGE;
						break;
						
					case("delete-task"):
						deleteTask(request, stage);
					
		            	forwardTo = Constants.URL_EDIT_STAGE;
						break;
					
					case("increase-task-order"):					
						
						//TODO delete? stage = Stage.load(stageID);
						
						stage.orderIncrementTask(taskID, originalOrder);
						
						//TODO delete? request.setAttribute("stage", stage);
						forwardTo = Constants.URL_EDIT_STAGE;
						break;
						
					case("decrease-task-order"):
						//TODO delete? stage = Stage.load(stageID);
					
						stage.orderDecrementTask(taskID, originalOrder);
						
						//TODO delete? request.setAttribute("stage", stage);
						forwardTo = Constants.URL_EDIT_STAGE;
						break;
		            default:

		                forwardTo = Constants.URL_ADMIN_MAIN_MENU;
				}
				
				request.setAttribute("treatmentPlan", treatmentPlan);
				request.setAttribute("stage", stage);
				request.setAttribute("task", task);
				request.setAttribute("owner", owner);
				
			} else if(user.hasRole(Constants.USER_CLIENT)){
				forwardTo = "clientMainMenu.jsp";
				request.setAttribute("erorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
				//UNSURE consider creating a UnauthorizedAccessException and throwing that here
			}
				
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("errorMessage", e.getMessage());
			
			request.setAttribute("stage", stage);
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("treatmentPlanID", treatmentPlanID);
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
	/*private List<MapStageTaskTemplate> retrieveStageTaskDetails(HttpServletRequest request, Stage stage){
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
		
	}*/
	
	
	/**Deletes task from the stage (Stage determines if deleted from task table or mapping table).  This method gets the taskID to delete from the request.
	 * @param request
	 * @param stageID
	 * @param taskID
	 * @throws DatabaseException
	 * @throws ValidationException
	 */
	private void deleteTask(HttpServletRequest request, Stage stage) throws DatabaseException, ValidationException{
		int taskToDeleteID = ParameterUtils.parseIntParameter(request, "taskID");
		stage.deleteTask(taskToDeleteID);
		
		request.setAttribute("stage", stage);
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
