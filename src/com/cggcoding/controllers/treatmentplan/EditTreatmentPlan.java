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
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.CommonServletFunctions;
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
		String forwardTo = "index.jsp";
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/

    	int treatmentPlanID = ParameterUtils.parseIntParameter(request, "treatmentPlanID");
    	int stageID = ParameterUtils.parseIntParameter(request, "stageID");
		int taskID = ParameterUtils.parseIntParameter(request, "taskID");
    	String planTitle = request.getParameter("planTitle");
    	String planDescription = request.getParameter("planDescription");
    	int defaultIssueID = ParameterUtils.parseIntParameter(request, "defaultTreatmentIssue");
    	int customIssueID = ParameterUtils.parseIntParameter(request, "customTreatmentIssue");

    	TreatmentPlan treatmentPlan = null;
    	
    	try {
    		if(user==null){
    			throw new ValidationException("Your session has expired.  Please log back in.");
    		}
    		
    		//set default lists in the request
    		CommonServletFunctions.setDefaultTreatmentIssuesInRequest(request);
    		CommonServletFunctions.setDefaultTreatmentPlansInRequest(request);
    		if(treatmentPlanID != 0) {
    			loadSelectedTreatmentPlanInRequest(request, treatmentPlanID);
    		}
    		
			if(user.hasRole("client")){
				//UserClient userClient = (UserClient)session.getAttribute("user");
				forwardTo = "clientMainMenu.jsp";
	
			} else if(user.hasRole("therapist")){
				//UserTherapist userTherapist = (UserTherapist)session.getAttribute("user");
				switch (requestedAction){
	            	case "plan-create-name-and-issue":
	                
	            	default:
				}
				
			} else if(user.hasRole("admin")){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
					
				
				switch (requestedAction){
		            case "plan-edit-start":
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
		            	break;
		            case "plan-edit-update":

		                if(planTitle.isEmpty() || planDescription.isEmpty()){
		                	throw new ValidationException(ErrorMessages.PLAN_MISSING_INFO);
		                }
		                
		                //detect which treatment issue source was used and validate
		                int treatmentIssueID = 0;
		                if(defaultIssueID == 0 && customIssueID == 0){
		                	throw new ValidationException(ErrorMessages.ISSUE_NONE_SELECTED);
		                }
		                if(defaultIssueID > 0 && customIssueID > 0){
		                	throw new ValidationException(ErrorMessages.ISSUE_MULTIPLE_SELECTED);
		                }
		                if(defaultIssueID > 0 && customIssueID <= 0){
		                	treatmentIssueID = defaultIssueID;
		                }
		                if(defaultIssueID <= 0 && customIssueID > 0){
		                	treatmentIssueID = customIssueID;
		                }
		                
		                treatmentPlan = TreatmentPlan.load(treatmentPlanID);
		                
		                treatmentPlan.setTitle(planTitle);
		                treatmentPlan.setDescription(planDescription);
		                treatmentPlan.setTreatmentIssueID(treatmentIssueID);
		                
		                
		                treatmentPlan.update();
		
		                request.setAttribute("treatmentPlan", treatmentPlan);
		                forwardTo = "/WEB-INF/jsp/admin-tools/admin-main-menu.jsp";
		                request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_UPDATED);
		            	break;
		            case "plan-edit-select-plan":
		        		int selectedDefaultTreatmentPlanID = ParameterUtils.parseIntParameter(request, "selectedDefaultTreatmentPlanID");
		        		if(selectedDefaultTreatmentPlanID != 0){
		        			loadSelectedTreatmentPlanInRequest(request, selectedDefaultTreatmentPlanID);
		        		} else {
		        			request.setAttribute("treatmentPlan", null);
		        		}
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
		            	break;
		            case "create-default-treatment-issue":
		            	CommonServletFunctions.createDefaultTreatmentIssue(request, user.getUserID());

						forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
		            	break;
		            case "stage-delete":
						treatmentPlan = TreatmentPlan.load(treatmentPlanID);
						treatmentPlan.deleteStage(stageID);
				    	request.setAttribute("treatmentPlan", treatmentPlan);
						
						forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
						break;
						
		            case "delete-plan":
		            	if(treatmentPlanID == 0){
		            		throw new ValidationException(ErrorMessages.PLAN_DELETE_ERROR);
		            	}else{
			            	TreatmentPlan.delete(treatmentPlanID);
			            	request.removeAttribute("treatmentPlan");
			            	request.setAttribute("successMessage", SuccessMessages.TREATMENT_PLAN_DELETED);
			            	
			            	//reload default options so dropdown list is properly updated
			            	CommonServletFunctions.setDefaultTreatmentPlansInRequest(request);
		            	}
		            	
		            	forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
		            	break;
		            
				}
			}
			
			
    	} catch (ValidationException | DatabaseException e) {
    		request.setAttribute("errorMessage", e.getMessage());
    		
    		//create a temporary treatmentPlan to hold info for plan that is in the process of creation
    		treatmentPlan = TreatmentPlan.getInstanceBasic(ParameterUtils.parseIntParameter(request, "treatmentPlanID"), user.getUserID(), request.getParameter("planTitle"), request.getParameter("planDescription"), 0, false, false, false,0,0);
    		request.setAttribute("treatmentPlan", treatmentPlan);
    		request.setAttribute("defaultTreatmentIssue", defaultIssueID);
    		request.setAttribute("existingCustomTreatmentIssue", customIssueID);
    		
    		forwardTo = "/WEB-INF/jsp/treatment-plans/treatment-plan-edit.jsp";
			//e.printStackTrace();
		}
    	
		request.getRequestDispatcher(forwardTo).forward(request,response);
	}
	
	private TreatmentPlan loadSelectedTreatmentPlanInRequest(HttpServletRequest request, int treatmentPlanID) throws DatabaseException, ValidationException{

    	TreatmentPlan treatmentPlan = TreatmentPlan.load(treatmentPlanID);
    	request.setAttribute("treatmentPlan", treatmentPlan);
    	return treatmentPlan;
	}
	
	
	
	/**
     * Gets the appropriate treatment issue id in the process of creating falsa new Treatment Plan.
     * There are 3 options for setting the issue type for the new plan.  Since only 1 issue can be selected, here we first get all the possible parameters from the request
     * and if more than one has been selected, notify the user that they can only choose 1.  Otherwise, use the selected treatment issue.
     * @param user
     * @param defaultIssueIDAsString
     * @param existingIssueIDAsString
     * @param newIssueName
     * @param request
     * @return the issueID of the selected TreatmentIssue
     * @throws ValidationException
     * @throws DatabaseException
     */
    private int getTreatmentIssueID(User user, String defaultIssueIDAsString, String existingIssueIDAsString, String newIssueName, HttpServletRequest request) throws ValidationException, DatabaseException{
        int issueID = 0;
        boolean hasNewCustomIssue = !newIssueName.isEmpty();

        int numOfIDs = 0;
        if(defaultIssueIDAsString != null){
	        if(!defaultIssueIDAsString.equals("")){
	            issueID = Integer.parseInt(defaultIssueIDAsString);
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
