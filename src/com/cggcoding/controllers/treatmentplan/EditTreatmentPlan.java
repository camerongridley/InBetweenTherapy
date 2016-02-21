package com.cggcoding.controllers.treatmentplan;

import java.io.IOException;
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
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;
import com.cggcoding.utils.messaging.SuccessMessages;

/**
 * Servlet implementation class EditTreatmentPlan
 */
@WebServlet("/secure/EditTreatmentPlan")
public class EditTreatmentPlan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditTreatmentPlan() {

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


    	String planTitle = request.getParameter("planTitle");
    	String planDescription = request.getParameter("planDescription");
    	int coreTreatmentIssueID = ParameterUtils.parseIntParameter(request, "coreTreatmentIssueID");
    	int customIssueID = ParameterUtils.parseIntParameter(request, "customTreatmentIssueID");
    	
    	try {
    		
    		//Here we check that a treatmentPlan has been selected (currently the only time this is true is with path plan-edit-selection).
    		//If so, then load it and use it's userID prop to get it's owner
    		if(treatmentPlanID != 0){
    			treatmentPlan = TreatmentPlan.load(treatmentPlanID);
        		ownerUserID = treatmentPlan.getUserID();
        		
        		//Set the User var "owner". If the owner of the plan that is being edited is different than the logged in user, then load the appropriate owner info
	    		if(ownerUserID==user.getUserID()){
	    			owner = user;
	    		} else {
	    			owner = User.loadBasic(treatmentPlan.getUserID());
	    		}
	    		
    		}
    		
    		
    		
    		
    		//OPTIMIZE re-consider use of CommonServletFunctions - maybe make another class that is CommonServletDatabaseCalls and passes a connection. In this page it is possible that in 1 request, CommonServletFunctions will open and close 3 connections
    		//set default lists in the request
    		CommonServletFunctions.setCoreTreatmentIssuesInRequest(request);
    		CommonServletFunctions.setCoreTreatmentPlansInRequest(request);

			if(user.hasRole(Constants.USER_ADMIN)|| user.hasRole(Constants.USER_THERAPIST)){
				
				//First perform actions desired that are independent of the requestedAction
				if(user.hasRole(Constants.USER_ADMIN)){
					
                }
                
                if(user.hasRole(Constants.USER_THERAPIST)){

                }
                
                //Now run actions specific to requestedAction
				switch (requestedAction){
					//Forwards to page that allows for selecting the plan user wants to edit
		            case "plan-edit-selection"://
		            	forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
		            	break;
		            //Updates the plan's basic info
		            case "plan-edit-update":
		            	//if Save button pressed, run the following.  If Cancel button was pressed then skip and just forward to appropriate page
						if(request.getParameter("submitButton").equals("save")){
							//detect which treatment issue source was used and validate
			                int treatmentIssueID = determineTreatmentIssueID(coreTreatmentIssueID, customIssueID);
			                
			                //updateTreatmentPlan(request, treatmentPlan, treatmentPlanID, planTitle, planDescription, treatmentIssueID);
			                if(treatmentPlanID==0){
			            		throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
			            	}
			            	
			                if(planTitle.isEmpty() || planDescription.isEmpty()){
			                	throw new ValidationException(ErrorMessages.PLAN_MISSING_INFO);
			                }

			                //TODO possibly change this to use a static method TreatmentPlan.updateBasic(planTitle, planDescription, treatmentIssueID);???
			                
			                //treatmentPlan = TreatmentPlan.load(treatmentPlanID);
			                
			                treatmentPlan.setTitle(planTitle);
			                treatmentPlan.setDescription(planDescription);
			                treatmentPlan.setTreatmentIssueID(treatmentIssueID);
			                
			                treatmentPlan.update();
			                
			                request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_UPDATED);
						}
		                
						//regardless of whether Save or Cancel button was pressed this determines where to forward and what to set in request
		                if(user.hasRole(Constants.USER_ADMIN)){
		                	forwardTo = Constants.URL_ADMIN_MAIN_MENU;
		                }
		                
		                if(user.hasRole(Constants.USER_THERAPIST)){
		                	switch(path){
		                		case Constants.PATH_MANAGE_CLIENT:
			                		UserTherapist userTherapist = (UserTherapist)user;
			    				
				    				//set the default treatment plans and the custom plans for this therapist into the request
				    				request.setAttribute("coreTreatmentPlansList", TreatmentPlan.getCoreTreatmentPlans());
				    				
				    				User client = User.loadBasic(treatmentPlan.getUserID());
			        				request.setAttribute("client", client);
				    				
				                	userTherapist.loadAllAssignedClientTreatmentPlans(ownerUserID);
				            		request.setAttribute("activeAssignedClientPlans", userTherapist.loadActiveAssignedClientTreatmentPlans());
				            		request.setAttribute("unstartedAssignedClientPlans", userTherapist.loadUnstartedAssignedClientTreatmentPlans());
				            		request.setAttribute("completedAssignedClientPlans", userTherapist.loadCompletedAssignedClientTreatmentPlans());
					                
					                forwardTo = Constants.URL_THERAPIST_MANAGE_CLIENT_PLANS;
					                
					                break;
					                
		                		default: 
		                			user.getMainMenuURL();
		                	} 
		                	
		                }
		                
		            	break;
		            case "plan-edit-load-plan":
		            	request.setAttribute("scrollTo", "selectTreatmentPlan");
		            	forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
		            	break;
		            case "create-new-treatment-issue":
		            	CommonServletFunctions.createCoreTreatmentIssue(request, user.getUserID());

						forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
		            	break;
		            case "stage-delete":
						//TODO delete: treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						treatmentPlan.deleteStage(stageID);
						
						forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
						break;
						
		            case "delete-plan":
		            	if(treatmentPlanID == 0){
		            		throw new ValidationException(ErrorMessages.PLAN_DELETE_ERROR);
		            	}else{
			            	TreatmentPlan.delete(treatmentPlanID);
			            	request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_DELETED);
			            	
			            	treatmentPlan = null;
			            	
			            	//reload default options so dropdown list is properly updated
			            	CommonServletFunctions.setCoreTreatmentPlansInRequest(request);
		            	}
		            	
		            	forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
		            	break;
		            
				}
	    		
				//set the TreatmentPlan in the request after all actions with it have been completed
				request.setAttribute("taskTypeMap", Task.getTaskTypeMap());
				request.setAttribute("coreTasks", Task.getCoreTasks());
				request.setAttribute("treatmentPlan", treatmentPlan);
				request.setAttribute("stage", stage);
				request.setAttribute("task", task);
				request.setAttribute("owner", owner);
				
				
			} else if(user.hasRole(Constants.USER_CLIENT)){
				//UserClient userClient = (UserClient)session.getAttribute("user");
				forwardTo = "clientMainMenu.jsp";
				request.setAttribute("erorMessage", ErrorMessages.UNAUTHORIZED_ACCESS);
				//UNSURE consider creating a UnauthorizedAccessException and throwing that here
			}
			
			
    	} catch (ValidationException | DatabaseException e) {
    		request.setAttribute("errorMessage", e.getMessage());
    		
    		//create a temporary treatmentPlan to hold title and description info that might be changed for plan that is in the process of creation - UNSURE if this will cause accidental replacement of the other fields that I am just supplying false and 0 to
    		treatmentPlan = TreatmentPlan.getInstanceBasic(ParameterUtils.parseIntParameter(request, "treatmentPlanID"), user.getUserID(), request.getParameter("planTitle"), request.getParameter("planDescription"), 0, false, false, false,0,0,0, Constants.ADMIN_ROLE_ID);
    		request.setAttribute("treatmentPlan", treatmentPlan);
    		request.setAttribute("coreTreatmentIssueID", coreTreatmentIssueID);
    		request.setAttribute("existingCustomTreatmentIssue", customIssueID);
    		request.setAttribute("owner", owner);
    		
    		forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
			//e.printStackTrace();
		}
    	
		request.getRequestDispatcher(forwardTo).forward(request,response);
	}
	
	//TODO delete?
	/*private TreatmentPlan loadSelectedTreatmentPlanInRequest(HttpServletRequest request, int treatmentPlanID) throws DatabaseException, ValidationException{
		TreatmentPlan treatmentPlan = null;
		if(treatmentPlanID != 0){
			treatmentPlan = TreatmentPlan.load(treatmentPlanID);
	    	request.setAttribute("treatmentPlan", treatmentPlan);
		} else {
			request.setAttribute("treatmentPlan", null);
		}
    	
    	return treatmentPlan;
	}*/

	//TODO delete?
	/**Validates that there are values for planID, title, and description.  Constructs TreatmentPlan with new data and calls update method.  Finally puts the updated plan back in the request as"treatmentPlan".
	 * @param request
	 * @param treatmentPlan
	 * @param treatmentPlanID
	 * @param planTitle
	 * @param planDescription
	 * @param treatmentIssueID
	 * @throws ValidationException
	 * @throws DatabaseException
	 */
	/*private void updateTreatmentPlan(HttpServletRequest request, TreatmentPlan treatmentPlan, int treatmentPlanID, String planTitle, String planDescription, int treatmentIssueID) throws ValidationException, DatabaseException{
		if(treatmentPlanID==0){
    		throw new ValidationException(ErrorMessages.NOTHING_SELECTED);
    	}
    	
        if(planTitle.isEmpty() || planDescription.isEmpty()){
        	throw new ValidationException(ErrorMessages.PLAN_MISSING_INFO);
        }
        
        
        
        //TODO possibly change this to use a static method TreatmentPlan.updateBasic(planTitle, planDescription, treatmentIssueID);???
        //treatmentPlan = TreatmentPlan.load(treatmentPlanID);
        
        treatmentPlan.setTitle(planTitle);
        treatmentPlan.setDescription(planDescription);
        treatmentPlan.setTreatmentIssueID(treatmentIssueID);
        
        
        treatmentPlan.update();

        request.setAttribute("treatmentPlan", treatmentPlan);
	}*/
	
	private int determineTreatmentIssueID(int coreIssueID, int customIssueID) throws ValidationException{
		//detect which treatment issue source was used and validate
        int treatmentIssueID = 0;
        if(coreIssueID == 0 && customIssueID == 0){
        	throw new ValidationException(ErrorMessages.ISSUE_NONE_SELECTED);
        }
        if(coreIssueID > 0 && customIssueID > 0){
        	throw new ValidationException(ErrorMessages.ISSUE_MULTIPLE_SELECTED);
        }
        if(coreIssueID > 0 && customIssueID <= 0){
        	treatmentIssueID = coreIssueID;
        }
        if(coreIssueID <= 0 && customIssueID > 0){
        	treatmentIssueID = customIssueID;
        }
        
        return treatmentIssueID;
	}
	
	/**
     * Gets the appropriate treatment issue id in the process of creating falsa new Treatment Plan.
     * There are 3 options for setting the issue type for the new plan.  Since only 1 issue can be selected, here we first get all the possible parameters from the request
     * and if more than one has been selected, notify the user that they can only choose 1.  Otherwise, use the selected treatment issue.
     * @param user
     * @param coreIssueIDAsString
     * @param existingIssueIDAsString
     * @param newIssueName
     * @param request
     * @return the issueID of the selected TreatmentIssue
     * @throws ValidationException
     * @throws DatabaseException
     */
    private int getTreatmentIssueID(User user, String coreIssueIDAsString, String existingIssueIDAsString, String newIssueName, HttpServletRequest request) throws ValidationException, DatabaseException{
        int issueID = 0;
        boolean hasNewCustomIssue = !newIssueName.isEmpty();

        int numOfIDs = 0;
        if(coreIssueIDAsString != null){
	        if(!coreIssueIDAsString.equals("")){
	            issueID = Integer.parseInt(coreIssueIDAsString);
	            numOfIDs++;
	        }
        }
        
        if(existingIssueIDAsString != null){
        	if(!existingIssueIDAsString.equals("")){
	            issueID = Integer.parseInt(existingIssueIDAsString);
	            numOfIDs++;
        	}
        }    

        if(hasNewCustomIssue){
            numOfIDs++;
        }

        if(numOfIDs > 1){
            throw new ValidationException(ErrorMessages.ISSUE_MULTIPLE_SELECTED);
        }else if(numOfIDs < 1) {
            throw new ValidationException(ErrorMessages.ISSUE_NONE_SELECTED);
        } else {
        	//if there are no validation problems and there is a new custom issue name, add the new issue to the database and get its id
        	if(hasNewCustomIssue){
	        	TreatmentIssue issue = new TreatmentIssue(newIssueName, user.getUserID());
				issue.create();// = user.createTreatmentIssue(issue);
	            issueID = issue.getTreatmentIssueID();
        	}
        	
        }


        return issueID;
    }
}
