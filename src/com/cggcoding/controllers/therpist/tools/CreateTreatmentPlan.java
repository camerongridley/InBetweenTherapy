package com.cggcoding.controllers.therpist.tools;

import com.cggcoding.models.TreatmentPlan;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by cgrid_000 on 8/12/2015.
 */
@WebServlet("/CreateTreatmentPlan")
public class CreateTreatmentPlan extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forwardTo = "index.jsp";
        String creationStep = request.getParameter("treatmentPlanCreationStep");

        switch (creationStep){
            case "planNameAndIssue":
                String planName = request.getParameter("planName");
                String planDescription = request.getParameter("planDescription");
                int txIssueID = getTreatmentPlanID(request);
                TreatmentPlan newPlan = new TreatmentPlan(planName, planDescription, txIssueID);
                request.setAttribute("newPlan", newPlan);
                forwardTo = "/therapisttools/createtxplan-stages.jsp";
                break;
            default:
        }

        request.getRequestDispatcher(forwardTo).forward(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

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
