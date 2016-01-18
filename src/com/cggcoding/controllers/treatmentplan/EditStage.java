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
		/*-----------End Common Servlet variables---------------*/
		
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
		int originalOrder = ParameterUtils.parseIntParameter(request, "templateTaskOrder");
		int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		String stageTitle = request.getParameter("stageTitle");
		String stageDescription = request.getParameter("stageDescription");
		Stage editedStage = null;
		int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
		/*This variable helps remember where to send the user back to when they are done editing the Task.
		If the Task being edited is a template the stageID will be TEMPLATE_HOLDER_ID, and not the Stage template being working on.
		If the Task being edited is part of a client's plan, then the stageID will be the stageID that is contained within the task.
		Need to maintain it between requests*/
		int planToReturnTo = treatmentPlanID;
		request.setAttribute("treatmentPlanID", planToReturnTo);
		
		try{
			request.setAttribute("defaultStageList", Stage.getDefaultStages());
			
			if(user.hasRole(Constants.USER_ADMIN)){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
		            case "stage-edit-start" :
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            	
		            case "stage-edit-select-stage" :
		            	int selectedDefaultStageID = ParameterUtils.parseIntParameter(request, "selectedDefaultStageID");
		            	if(selectedDefaultStageID != 0){
		            		request.setAttribute("stage", Stage.load(selectedDefaultStageID));
		            	} else {
		            		request.setAttribute("stage", null);
		            	}
		            	
		            	if(path.equals("treatmentPlanTemplate")){
							request.setAttribute("warningMessage", WarningMessages.EDITING_STAGE_TEMPLATE);
						}
		            	
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
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
		            	
		            	//For Admin/Templates
		            	//get the repetition value for each task template inside this stage template and set it the edited stage to be updated when stage.update() is called
		            	for(MapStageTaskTemplate stageTaskInfo : editedStage.getMapStageTaskTemplates()){
		            		int templateReps = ParameterUtils.parseIntParameter(request, "taskTemplateRepetitions" + stageTaskInfo.getTaskID());
		            		stageTaskInfo.setTemplateRepetitions(templateReps);
		            		//get order info from request and set in stageTaskInfo here if decide to change so order is a dropdown choice
		            	}	      
		            	
		            	editedStage.update();//OPTIMIZE could create a new method that takes all relevant info and calls static method in stage that loads and updates all with the same connection
		            	
		            	//TODO deprecated?  delete? retrieveStageTaskDetails(request, editedStage);
		            	
		            	request.setAttribute("stage", editedStage);
		            	if(path.equals("treatmentPlanTemplate")){
		            		request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
		            		request.setAttribute("treatmentPlan", TreatmentPlan.load(planToReturnTo));
		            		request.setAttribute("defaultTreatmentIssues", TreatmentIssue.getDefaultTreatmentIssues());
		            		CommonServletFunctions.setDefaultTreatmentPlansInRequest(request);
		            		forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
		            	}else{
		            		request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
		            		forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
		            	}

		            	break;
		            	
		            case "stage-edit-add-goal" :
		            	addGoal(request, stageID);
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            	
		            case("delete-goal"):
						int goalID = ParameterUtils.parseIntParameter(request, "stageGoalID");
						StageGoal.delete(goalID);
						
						editedStage = Stage.load(stageID);
						request.setAttribute("stage", editedStage);
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						break;
						
		            //TODO remove this and have calling forms use stage-edit-select-stage instead
		            case "stage-edit":
						editedStage = Stage.load(stageID);
						request.setAttribute("stage", editedStage);
						
						if(path.equals("treatmentPlanTemplate")){
							request.setAttribute("warningMessage", WarningMessages.EDITING_STAGE_TEMPLATE);
						}
						
						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						break;	
						
					case("delete-task"):
						deleteTask(request, stageID);
					
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						break;
					
					case("increase-task-order"):					
						
						editedStage = Stage.load(stageID);
						
						editedStage.orderIncrementTemplateTask(taskID, originalOrder);
						
						request.setAttribute("stage", editedStage);
						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						break;
						
					case("decrease-task-order"):
						editedStage = Stage.load(stageID);
					
						editedStage.orderDecrementTemplateTask(taskID, originalOrder);
						
						request.setAttribute("stage", editedStage);
						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						break;
		            default:

		                forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
				}
			} else if(user.hasRole(Constants.USER_THERAPIST)){
				switch(requestedAction){
					//TODO XXX  DON'T KEEP THIS! - First address the todo tag earlier that address getting rid of the requestedAction "stage-edit"
					case "stage-edit":
						editedStage = Stage.load(stageID);
						request.setAttribute("stage", editedStage);
						
						if(path.equals("treatmentPlanTemplate")){
							request.setAttribute("warningMessage", WarningMessages.EDITING_STAGE_TEMPLATE);
						}
						
						forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
						break;	
					}
			}
				
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("errorMessage", e.getMessage());
			
			request.setAttribute("stage", editedStage);
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
			request.setAttribute("treatmentPlanID", planToReturnTo);
            forwardTo = "/WEB-INF/jsp/treatment-plans/stage-edit.jsp";
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
