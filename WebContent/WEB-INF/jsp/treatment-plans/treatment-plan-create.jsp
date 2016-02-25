<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<c:import url="/WEB-INF/jsp/header.jsp" />

    <div class="page-header">
        <h2>Create a Treatment Plan<c:if test='${path=="templateTreatmentPlan" }'> Template</c:if></h2>
        <c:import url="/WEB-INF/jsp/includes/breadcrumbs.jsp" />
    </div>
    
	<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
    
    <form class="form-horizontal" action="/secure/treatment-components/CreateTreatmentPlan" method="POST">
        <input type="hidden" name="requestedAction" value="plan-create-new" >
        <input type="hidden" name="path" value="${path }" >
        <div class="form-group">
            <label for="planTitle" class="col-sm-2 control-label">Plan Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="planTitle" name="planTitle" value="${fn:escapeXml(planTitle) }" placeholder="Enter a treatment plan name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="planDescription" class="col-sm-2 control-label">Plan Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="planDescription" name="planDescription" value="${fn:escapeXml(planDescription) }" placeholder="Describe the treatment plan.">
            </div>
        </div>
		
		<div class="well well-sm">
            <div class="form-group col-md-12">
            <c:choose>
	            <c:when test="${customTreatmentIssues != null }">
	                <h4>Select <strong>one</strong> of the following options for designating the issue this plan will treat.</h4>
	            </c:when>
	            <c:otherwise>
	            	<h4>Select the issue this plan will treat.</h4>
	            </c:otherwise>
            </c:choose>
            
            </div>
            
            <div class="form-group">
                <label for="coreTreatmentIssueID" class="col-sm-2 control-label">Core Treatment Issues</label>
                <div class="col-sm-9">
                    <select class="form-control" id="coreTreatmentIssueID" name="coreTreatmentIssueID">
                        <option  value="">Select a core treatment issue.</option>
                        <c:forEach items="${coreTreatmentIssues}" var="coreIssue">
                            <option value="${coreIssue.treatmentIssueID}" <c:if test="${coreIssue.treatmentIssueID == selectedCoreIssueID}">selected</c:if>>${coreIssue.treatmentIssueName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-sm-1">
                	<button type="button" class="btn btn-default" aria-label="Left Align" data-toggle="modal" data-target="#newCoreTreatmentIssueModal">
					  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
                </div>
            </div>

            <c:if test="${customTreatmentIssues != null }">
            <div class="form-group">
                <label for="customTreatmentIssueID" class="col-sm-2 control-label">Existing Custom Tx Issues</label>
                <div class="col-sm-10">
                    <select class="form-control" id="customTreatmentIssueID" name="customTreatmentIssueID">
                        <option value="">Or select an issue you've previously created.</option>
                        <c:forEach items="${customTreatmentIssues}" var="customIssue">
                            <option value="${customIssue.treatmentIssueID}"<c:if test="${customIssue.treatmentIssueID == selectedCustomIssueID}">selected</c:if>>${customIssue.treatmentIssueName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-sm-1">
                	<button type="button" class="btn btn-default" aria-label="Left Align" data-toggle="modal" data-target="#newCustomTreatmentIssueModal">
					  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
                </div>
            </div>
            
            </c:if>
        </div>
		
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10 save-button">
                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save & Continue</button>
                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
            </div>
        </div>
    </form>

	<!-- New Core Treatment Issue Modal -->
	<div class="modal fade" id="newCoreTreatmentIssueModal" tabindex="-1" role="dialog" aria-labelledby="newCoreTreatmentIssueModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
		    <form class="form-horizontal" action="/secure/treatment-components/CreateTreatmentPlan" method="POST">
		    <input type="hidden" name="requestedAction" value="create-new-treatment-issue">
		    <input type="hidden" name="path" value="${path }" >
		    <input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" >
		    <input type="hidden" name="planTitle" value="${planTitle }">
		    <input type="hidden" name="planDescription" value="${planDescription }">
		    <input type="hidden" name="selectedCoreIssueID" value="${selectedCoreIssueID }">
		    <input type="hidden" name="selectedCustomIssueID" value="${selectedCustomIssueID }">
		    
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="newCoreTreatmentIssueModalLabel">Enter a new core Treatment Issue</h4>
		      </div>
		      <div class="modal-body">
		        <input type="text" class="form-control" id="newCoreTreatmentIssue" name="newCoreTreatmentIssue" value="${fn:escapeXml(newCoreTreatmentIssue) }" placeholder="Enter a new core treatment issue.">
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary">Save</button>
		      </div>
		    </form>  
	    </div>
	  </div>
	</div>

<c:import url="/WEB-INF/jsp/footer.jsp" />