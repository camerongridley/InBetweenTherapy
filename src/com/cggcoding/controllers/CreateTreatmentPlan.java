package com.cggcoding.controllers;

import com.cggcoding.models.TreatmentPlan;
import com.cggcoding.models.User;
import com.cggcoding.models.UserAdmin;
import com.cggcoding.models.UserClient;
import com.cggcoding.models.UserTherapist;
import com.cggcoding.utils.database.MySQLActionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * Created by cgrid_000 on 8/12/2015.
 * 
 */
@WebServlet("/CreateTreatmentPlan")
public class CreateTreatmentPlan extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	DataSource datasource = (DataSource)request.getServletContext().getAttribute("datasource");
    	User user = (User)session.getAttribute("user");
    	String forwardTo = "index.jsp";
    	String creationStep = request.getParameter("treatmentPlanCreationStep");
    	
		if(user.hasRole("client")){
			//UserClient userClient = (UserClient)session.getAttribute("user");
			forwardTo = "clientMainMenu.jsp";

		} else if(user.hasRole("therapist")){
			//UserTherapist userTherapist = (UserTherapist)session.getAttribute("user");
			switch (creationStep){
            case "planNameAndIssue":
                String planName = request.getParameter("planName");
                String planDescription = request.getParameter("planDescription");
                int txIssueID = getTreatmentPlanID(request);
                TreatmentPlan newPlan = new TreatmentPlan(planName, planDescription, txIssueID);
                request.setAttribute("newPlan", newPlan);
                forwardTo = "/createplan/createtxplan-stages.jsp";
                break;
            default:
        }
			forwardTo = "clientMainMenu.jsp";
			
		} else if(user.hasRole("admin")){
			UserAdmin userAdmin = (UserAdmin)session.getAttribute("user");
			MySQLActionHandler dbActionHandler = new MySQLActionHandler(datasource);
			
			
			
			switch (creationStep){
			case "begining":
				//get treatment issues associated with admin role
				ArrayList<String> treatmentIssues = dbActionHandler.getDefaultTreatmentIssues();
				break;
            case "planNameAndIssue":
                String planName = request.getParameter("planName");
                String planDescription = request.getParameter("planDescription");
                int txIssueID = getTreatmentPlanID(request);
                TreatmentPlan newPlan = new TreatmentPlan(planName, planDescription, txIssueID);
                request.setAttribute("newPlan", newPlan);
                forwardTo = "/createplan/createtxplan-stages.jsp";
                break;
            case "stageAndTask":
            	
            	forwardTo = "/createplan/createtxplan-review.jsp";
            	break;
			}
        
        request.getRequestDispatcher(forwardTo).forward(request,response);

		}
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    
    //There are 3 options for setting the issue type for the new plan.  Since only 1 issue can be selected, here we first get all the possible parameters from the request
    //and if more than one has been selected, notify the user that they can only choose 1.  Otherwise, use the selected treatment issue.
    private int getTreatmentPlanID(HttpServletRequest request){
        String defaultIssueID = request.getParameter("defaultTxIssue");
        String existingCustomIssueID = request.getParameter("existingCustomTxIssue");
        String newCustomIssueName = request.getParameter("newCustomTxIssue");

        int issueID = -1;

        int numOfIDs = 0;
        if(!defaultIssueID.equals("")){
            issueID = Integer.parseInt(defaultIssueID);
            numOfIDs++;
        }
        if(!existingCustomIssueID.equals("")){
            issueID = Integer.parseInt(existingCustomIssueID);
            numOfIDs++;
        }
        if(!newCustomIssueName.equals("")){
            //here we need a db call insert new custom issue then select the id that is generated
            //issueID = dbHelper.createNewTreatmentIssue(newCustomIssueName);
            issueID = 13; //until db is set up we'll hardcode a temp value
            numOfIDs++;
        }

        try {
            if(numOfIDs > 1){
                throw new Exception("Invalid condition. Multiple treatment issues are selected. Ensure that only 1 treatmentIssue id is in the request object.");
            }else if(numOfIDs < 1) {
                throw new Exception("Invalid condition.  There were no treatment isseues selected.  Ensure that at least 1 treatmentIssue id is in the request object.");
            } else {

            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return issueID;
    }
}
