package com.cggcoding.controllers.treatmentplan;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.models.Stage;
import com.cggcoding.models.Task;
import com.cggcoding.models.TreatmentIssue;
import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.utils.CommonServletFunctions;
import com.cggcoding.utils.Constants;
import com.cggcoding.utils.ParameterUtils;
import com.cggcoding.utils.messaging.ErrorMessages;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by cgrid_000 on 8/12/2015.
 * 
 */
@WebServlet("/secure/CreateTreatmentPlan")
public class CreateTreatmentPlan extends HttpServlet implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*--Common Servlet variables that should be in every controller--*/
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String forwardTo = Constants.URL_INDEX;
		String requestedAction = request.getParameter("requestedAction");
		String path = request.getParameter("path");
		request.setAttribute("path", path);
		/*-----------End Common Servlet variables---------------*/
		
		
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    	int selectedCoreIssueID = ParameterUtils.parseIntParameter(request, "coreTreatmentIssueID");
    	int selectedCustomIssueID = ParameterUtils.parseIntParameter(request, "customTreatmentIssueID");

    	
    	try {
    		//currently the logged in user will always be the owner of a new TreatmentPlan
    		owner = user;
    		
			if(user.hasRole(Constants.USER_CLIENT)){
				//UserClient userClient = (UserClient)session.getAttribute("user");
				forwardTo = "clientMainMenu.jsp";
	
			} else if(user.hasRole(Constants.USER_THERAPIST)){
				//UserTherapist userTherapist = (UserTherapist)session.getAttribute("user");
				switch (requestedAction){
	            case "plan-create-name-and-issue":
	                
	            default:
				}
				
			} else if(user.hasRole(Constants.USER_ADMIN)){
				UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
				ArrayList<TreatmentIssue> coreTreatmentIssues = TreatmentIssue.getCoreTreatmentIssues();		
				request.setAttribute("coreTreatmentIssues", coreTreatmentIssues);
				
				switch (requestedAction){
					case "create-core-treatment-issue":
		            	CommonServletFunctions.createCoreTreatmentIssue(request, user.getUserID());
	
						forwardTo = Constants.URL_CREATE_TREATMENT_PLAN;
		            	break;
					case "plan-create-start":

						forwardTo = Constants.URL_CREATE_TREATMENT_PLAN;
						break;
		            case "plan-create-name":
		            	
		                if(planTitle.isEmpty() || planDescription.isEmpty()){
		                	throw new ValidationException(ErrorMessages.PLAN_MISSING_INFO);
		                }
		                
		              //detect which treatment issue source was used and validate
		                int treatmentIssueID = 0;
		                if(selectedCoreIssueID == 0 && selectedCustomIssueID == 0){
		                	throw new ValidationException(ErrorMessages.ISSUE_NONE_SELECTED);
		                }
		                if(selectedCoreIssueID > 0 && selectedCustomIssueID > 0){
		                	throw new ValidationException(ErrorMessages.ISSUE_MULTIPLE_SELECTED);
		                }
		                if(selectedCoreIssueID > 0 && selectedCustomIssueID <= 0){
		                	treatmentIssueID = selectedCoreIssueID;
		                }
		                if(selectedCoreIssueID <= 0 && selectedCustomIssueID > 0){
		                	treatmentIssueID = selectedCustomIssueID;
		                }

		                treatmentPlan = TreatmentPlan.getInstanceWithoutID(planTitle, user.getUserID(), planDescription, treatmentIssueID);
		                
		                if(path.equals(Constants.PATH_TEMPLATE_TREATMENT_PLAN)){
		                	treatmentPlan.setTemplate(true);
		                }else{
		                	treatmentPlan.setTemplate(false);
		                }
		                
		                //submit to be validated and if passes then inserted into database and get the treatmentplan with autogenerated id returned
		                treatmentPlan.createBasic();
		
		                request.setAttribute("treatmentPlan", treatmentPlan);
		                forwardTo = Constants.URL_EDIT_TREATMENT_PLAN;
		                break;
				}

			}
			
			request.setAttribute("owner", owner);
			request.setAttribute("planTitle", planTitle);
    		request.setAttribute("planDescription", planDescription);
    		request.setAttribute("selectedCoreIssueID", selectedCoreIssueID);
    		request.setAttribute("selectedCustomTreatmentIssue", selectedCustomIssueID);
			
    	} catch (ValidationException | DatabaseException e) {
    		request.setAttribute("errorMessage", e.getMessage());
    		request.setAttribute("planTitle", planTitle);
    		request.setAttribute("planDescription", planDescription);
    		request.setAttribute("selectedCoreIssueID", selectedCoreIssueID);
    		request.setAttribute("selectedCustomTreatmentIssue", selectedCustomIssueID);

    		forwardTo = Constants.URL_CREATE_TREATMENT_PLAN;
			//e.printStackTrace();
		}
    	
		request.getRequestDispatcher(forwardTo).forward(request,response);
		
    }

    
    
    
}
