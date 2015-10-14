package com.cggcoding.utils;

import javax.servlet.http.HttpServletRequest;

import com.cggcoding.exceptions.DatabaseException;
import com.cggcoding.exceptions.ValidationException;
import com.cggcoding.helpers.DefaultDatabaseCalls;
import com.cggcoding.models.TreatmentIssue;

public class CommonServletFunctions {

	public CommonServletFunctions() {

	}

	public static void createDefaultTreatmentIssue(HttpServletRequest request, int userID) throws ValidationException, DatabaseException{
		String newIssueName = request.getParameter("newDefaultTreatmentIssue");
		TreatmentIssue issue = new TreatmentIssue(newIssueName, userID);
		issue.saveNew();
		
		request.setAttribute("defaultTreatmentIssues", DefaultDatabaseCalls.getDefaultTreatmentIssues());
	}
	
}
