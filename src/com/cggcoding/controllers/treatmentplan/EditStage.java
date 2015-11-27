package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
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
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class EditStage
 */
@WebServlet("/EditStage")
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
		
		
		int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		String stageTitle = request.getParameter("stageTitle");
		String stageDescription = request.getParameter("stageDescription");
		Stage editedStage = null;
		
		
		try{
			request.setAttribute("defaultStageList", Stage.getDefaultStages());
			
			if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
								
				switch (requestedAction){
		            case "stage-edit-start" :
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            case "stage-edit-select-stage" :
		            	int selectedDefaultStageID = ParameterUtils.parseIntParameter(request, "selectedDefaultStageID");
		            	if(selectedDefaultStageID != 0){
		            		request.setAttribute("stage", Stage.load(selectedDefaultStageID));
		            	} else {
		            		request.setAttribute("stage", null);
		            	}
		            	
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            case "stage-edit-name" :
		            	editedStage = Stage.load(stageID);
		            	editedStage.setTitle(stageTitle);
		            	editedStage.setDescription(stageDescription);
		            	
		            	for(StageGoal goal: editedStage.getGoals()){
		            		goal.setDescription(request.getParameter("stageGoalDescription" + goal.getStageGoalID()));
		            	}
		            	
		            	editedStage.update();
		            	
		            	request.setAttribute("stage", editedStage);
		            	if(path.equals("editingPlanTemplate") || path.equals("creatingPlanTemplate")){
		            		request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
		            		request.setAttribute("treatmentPlan", TreatmentPlan.load(editedStage.getTreatmentPlanID()));
		            		request.setAttribute("defaultTreatmentIssues", TreatmentIssue.getDefaultTreatmentIssues());
		            		forwardTo = "/jsp/treatment-plans/treatment-plan-edit.jsp";
		            	}else{
		            		request.setAttribute("successMessage", SuccessMessages.STAGE_UPDATED);
		            		forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
		            	}

		            	break;
		            case "stage-edit-add-goal" :
		            	String goalDescription = request.getParameter("newStageGoalDescription");
		            	StageGoal goal = StageGoal.getInstanceWithoutID(stageID, goalDescription);
		            	goal.create();
		            	request.setAttribute("stage", Stage.load(stageID));
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		            	break;
		            	
		            case "stage-edit":
						editedStage = Stage.load(stageID);
						request.setAttribute("stage", editedStage);
						forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
						break;	
						
					case("delete-task"):
						editedStage = Stage.load(stageID);
						int taskToDeleteID = ParameterUtils.parseIntParameter(request, "taskID");
						editedStage.deleteTask(taskToDeleteID);
						
						request.setAttribute("stage", editedStage);
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
						break;
					case("delete-goal"):
						int goalID = ParameterUtils.parseIntParameter(request, "stageGoalID");
						StageGoal.delete(goalID);
						
						editedStage = Stage.load(stageID);
						request.setAttribute("stage", editedStage);
		            	forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
						break;
					
		            default:

		                forwardTo = "/jsp/admin-tools/admin-main-menu.jsp";
				}
			}
			
		} catch (ValidationException | DatabaseException e){
			//in case of error and user is sent back to page - re-populate the forms
			request.setAttribute("errorMessage", e.getMessage());
			
			request.setAttribute("stage", editedStage);
			request.setAttribute("stageTitle", stageTitle);
			request.setAttribute("stageDescription", stageDescription);
            forwardTo = "/jsp/treatment-plans/stage-edit.jsp";
		}
		
		request.getRequestDispatcher(forwardTo).forward(request, response);
	}

}
