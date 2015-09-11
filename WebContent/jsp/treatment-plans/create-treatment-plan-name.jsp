<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

    <div class="page-header">
        <h1>Create a Treatment Plan</h1>
        <h2>Name the plan and identify what issue it is treating</h2>
    </div>
    
	<c:import url="/jsp/message-modal.jsp"/>
    
    <form class="form-horizontal" action="./CreateTreatmentPlan" method="POST">
        <input type="hidden" name="treatmentPlanCreationStep" value="planNameAndIssue" />
        <div class="form-group">
            <label for="planName" class="col-sm-2 control-label">Plan Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="planName" name="planName" value="${planName }" placeholder="Enter a treatment plan name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="planDescription" class="col-sm-2 control-label">Plan Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="planDescription" name="planDescription" value="${planDescription }" placeholder="Describe the treatment plan.">
            </div>
        </div>
        <div class="well well-sm">
            <div class="form-group col-md-12">
                <h4>Select <strong>one</strong> of the following options for designating the issue this plan will treat.</h4>
            </div>
            

            <div class="form-group">
                <label for="defaultTreatmentIssue" class="col-sm-2 control-label">Default Tx Issues</label>
                <div class="col-sm-10">
                    <select class="form-control" id="defaultTreatmentIssue" name="defaultTreatmentIssue">
                        <option  value="">Select a default treatment issue.</option>
                        <c:forEach items="${defaultTreatmentIssues}" var="defaultIssue">
                            <option value="${defaultIssue.treatmentIssueID}" <c:if test="${defaultIssue.treatmentIssueID == defaultTreatmentIssue}">selected</c:if>>${defaultIssue.treatmentIssueName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            
            <div class="form-group">
                <label for="newDefaultTreatmentIssue" class="col-sm-2 control-label">New Default Tx Issue</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="newDefaultTreatmentIssue" name="newDefaultTreatmentIssue" value="${newDefaultTreatmentIssue }" placeholder="Or enter a new default treatment issue.">
                </div>
            </div>

            
            <c:if test="${customTreatmentIssues != null }">
            <div class="form-group">
                <label for="existingCustomTreatmentIssue" class="col-sm-2 control-label">Existing Custom Tx Issues</label>
                <div class="col-sm-10">
                    <select class="form-control" id="existingCustomTreatmentIssue" name="existingCustomTreatmentIssue">
                        <option value="">Or select an issue you've previously created.</option>
                        <c:forEach items="${customTreatmentIssues}" var="customIssue">
                            <option value="${customIssue.treatmentIssueID}">${customIssue.treatmentIssueName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            
            
            <div class="form-group">
                <label for="newCustomTreatmentIssue" class="col-sm-2 control-label">New Custom Tx Issue</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="newCustomTreatmentIssue" name="newCustomTreatmentIssue" value="${newCustomTreatmentIssue }" placeholder="Or enter a new custom treatment issue.">
                </div>
            </div>
            </c:if>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Next Step >></button>
            </div>
        </div>
    </form>



<c:import url="/jsp/footer.jsp" />