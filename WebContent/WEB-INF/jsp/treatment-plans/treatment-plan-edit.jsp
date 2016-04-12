<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h2>Edit Treatment Plan</h2>
	<c:import url="/WEB-INF/jsp/includes/breadcrumbs.jsp" />
</div>

<c:import url="/WEB-INF/jsp/message-modal.jsp" />


<c:if test="${path=='templateTreatmentPlan' }">
<div class="row">
	<div class="form-group">
		<form class="form-horizontal" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
			<input type="hidden" name="requestedAction" value="plan-edit-load-plan"> 
			<input type="hidden" name="path" value="${path }">
			
				<label for="selectedCoreTreatmentPlanID"
					class="col-sm-2 control-label">Select a Treatment Plan</label>
				<div class="col-sm-8">
				<a id="selectTreatmentPlan"></a>
					<select class="form-control" id="selectedCoreTreatmentPlanID" name="treatmentPlanID">
						<option value="">Select a treatment plan to edit.</option>
						<c:forEach var="corePlan" items="${coreTreatmentPlansList }">
							<option value="${corePlan.treatmentPlanID}"
								<c:if test="${corePlan.treatmentPlanID == treatmentPlan.treatmentPlanID }">selected</c:if>>${corePlan.title}</option>
						</c:forEach>
					</select>
		
				</div>
			
		</form>	
		<div class="col-sm-2">
			<form class="form-horizontal form-inline-controls" action="/secure/treatment-components/CreateTreatmentPlan" method="POST">
				<input type="hidden" name="requestedAction" value="plan-create-start"> 
				<input type="hidden" name="path" value="templateTreatmentPlan">
				<button type="submit" class="btn btn-default glyphicon glyphicon-plus" aria-hidden="true" title="Add a new treatment plan."></button>
			</form>
	
			<button type="button" class="btn btn-default glyphicon glyphicon-remove" data-toggle="modal" data-target="#delete_plan_modal" aria-hidden="true" title="Delete this treatment plan." <c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>></button>
		</div>
	</div>
</div>

<hr>
</c:if>


<form id="form-update-plan" class="form-horizontal" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
	<input type="hidden" name="requestedAction" value="plan-edit-update">
	<input type="hidden" name="path" value="${path }"> 
	<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
	
	<div class="row form-group">
		<label for="planName" class="col-sm-2 control-label">Plan Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="planTitle"
				name="planTitle" value="<c:out value="${treatmentPlan.title }"/>"
				placeholder="Enter a treatment plan name here." <c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>>
		</div>
	</div>
	<div class="row form-group">
		<label for="planDescription" class="col-sm-2 control-label">Plan
			Description</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="planDescription"
				name="planDescription" value="<c:out value="${treatmentPlan.description }"/>"
				placeholder="Describe the treatment plan." <c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>>
		</div>
	</div>
	<div class="well well-sm">
		<div class="row form-group">
			<div class="col-md-12">
				<c:choose>
					<c:when test="${customTreatmentIssues != null }">
						<h4>
							Select <strong>one</strong> of the following options for
							designating the issue this plan will treat.
						</h4>
					</c:when>
					<c:otherwise>
						<h4>Select the issue this plan will treat.</h4>
					</c:otherwise>
				</c:choose>
			</div>
		</div>


		<div class="row form-group">
			<label for="coreTreatmentIssueID" class="col-sm-2 control-label">Core Treatment Issues</label>
			<div class="col-sm-9">
				<select class="form-control" id="coreTreatmentIssueID" name="coreTreatmentIssueID" <c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>>
					<option value="" >Select a default treatment issue.</option>
					<c:forEach items="${coreTreatmentIssues}" var="coreIssue">
						<option value="${coreIssue.treatmentIssueID}"
							<c:if test="${coreIssue.treatmentIssueID == treatmentPlan.treatmentIssueID}">selected</c:if>>${coreIssue.treatmentIssueName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-sm-1">
				<c:if test='${user.role.equals("admin") }'>
					<button type="button" class="btn btn-default" title="Add a new default treatment issue."
						aria-label="Left Align" data-toggle="modal"
						data-target="#newCoreTreatmentIssueModal"
						title="Add a new core treatment issue" 
						<c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>>
						<span class="glyphicon glyphicon-plus" aria-hidden="true" ></span>
					</button>
				</c:if>	
			</div>
		</div>

		<c:if test="${customTreatmentIssues != null }">
			<div class="row form-group">
				<label for="customTreatmentIssueID" class="col-sm-2 control-label">Existing
					Custom Tx Issues</label>
				<div class="col-sm-10">
					<select class="form-control" id="customTreatmentIssueID"
						name="customTreatmentIssueID">
						<option value="">Or select an issue you've previously
							created.</option>
						<c:forEach items="${customTreatmentIssues}" var="customIssue">
							<option value="${customIssue.treatmentIssueID}"><c:out value="${customIssue.treatmentIssueName }"/>"</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-1">
					<button type="button" class="btn btn-default"
						aria-label="Left Align" data-toggle="modal"
						data-target="#newCustomTreatmentIssueModal" 
						title="Add a new custom treatment issue." 
						<c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>>
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
				</div>
			</div>

		</c:if>
	</div>

</form>

	<a id="stageListTop"></a>
	<label for="stageList" class="control-label">Stages 
	<c:if test="${treatmentPlan!=null }">
		<form class="form-inline form-inline-controls" action="/secure/treatment-components/CreateStage" method="POST">
			<input type="hidden" name="requestedAction" value="add-stage-to-treatment-plan">
			<input type="hidden" name="path" value="${path }"> 
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">

			<button role="submit" class="btn btn-default btn-xs" title="Add a stage to this treatment plan."> 
				<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
			</button>
		</form>
	</c:if>
	</label>
	<c:forEach items="${treatmentPlan.stages }" var="stage">
	<c:set var="mappedPlanStageInfo" value="${treatmentPlan.getMappedStageTemplateByStageID(stage.stageID)}" />
		<div class="panel panel-default panel-task" id="stageList"
			title="Click the stage title to expand and see the stage details.">
			<div class="panel-heading">
				<input type="hidden" name="stageID" value="${stage.stageID}" /> 
				<input type="hidden" name="stageTitle${stage.stageID}" value="${stage.title}" /> 
				<c:choose>
		          <c:when test='${stage.template }'>
		          	<c:set var="stageOrder" value="${mappedPlanStageInfo.templateStageOrder }"/>
		          </c:when>
		          <c:otherwise>
		          	<c:set var="stageOrder" value="${stage.clientStageOrder }"/>
		          </c:otherwise>
		        </c:choose>
				
				<a role="button" data-toggle="collapse" href="#collapse${stage.stageID }" aria-expanded="true" aria-controls="collapse${stage.stageID }">
					${stageOrder+1 } - <span class="">${stage.title }</span>
				</a> 
				<a role="button" data-toggle="modal" data-target="#delete_stage_modal${stage.stageID }"
					class="btn btn-default btn-xs pull-right"
					title="Delete stage from this treatment plan."> 
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
				</a> 
				<form class="form-inline form-inline-controls" action="/secure/treatment-components/EditStage" method="POST">
					<input type="hidden" name="requestedAction" value="select-stage">
					<input type="hidden" name="path" value="${path }"> 
					<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
					<input type="hidden" name="stageID" value="${stage.stageID}">
					
					<button role="button" class="btn btn-default btn-xs pull-right" title="Edit this stage.">
						<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
					</button>
				</form>
			</div>
			<div id="collapse${stage.stageID }" class="panel-collapse collapse"
				role="tabpanel" aria-labelledby="heading${stage.stageID}">
				<div class="panel-body">${stage.description }</div>
				<div class="" style="padding-left: 20px">
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Tasks</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${stage.tasks }" var="task">
							<c:set var="mappedStageTaskInfo" value="${stage.getMappedTaskTemplateByTaskID(task.taskID)}" />
								<tr>
									<!-- <th scope="row">${task.clientTaskOrder}</th>-->
									<td>${task.title} <c:if test="${task.template}"><span class="badge" title="Number of repetitions.">${mappedStageTaskInfo.templateTaskRepetitions }</span></c:if>
									
										<!-- <a role="button"
										href="/secure/treatment-components/EditTask?requestedAction=edit-task-select-task&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}&taskID=${task.taskID}"
										class="btn btn-default btn-xs pull-right"
										title="Edit this task"> <span
											class="glyphicon glyphicon-edit" aria-hidden="true"></span>
										</a> -->
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>
		</div>
	</c:forEach>

	<div class="form-group">
        <div class="col-sm-12 save-button">
            <button type="submit" id="submitForUpdatePlan" value="save" class="btn btn-default" <c:if test="${treatmentPlan==null }">disabled="disabled"</c:if>>Save</button>
            <form class="form-inline form-inline-controls" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
				<input type="hidden" name="requestedAction" value="plan-edit-update-cancel">
				<input type="hidden" name="path" value="${path }"> 
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
				
            	<button type="submit" id="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
            </form>
            
        </div>
    </div>


<!-- New Core Treatment Issue Modal -->
<div class="modal fade" id="newCoreTreatmentIssueModal" tabindex="-1"
	role="dialog" aria-labelledby="newCoreTreatmentIssueModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form class="form-horizontal" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
				<input type="hidden" name="requestedAction" value="create-new-treatment-issue"> 
				<input type="hidden" name="path" value="${path }"> 
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="newCoreTreatmentIssueModalLabel">Enter
						a new core Treatment Issue</h4>
				</div>
				<div class="modal-body">
					<input type="text" class="form-control"
						id="newCoreTreatmentIssue" name="newCoreTreatmentIssue"
						value="<c:out value="${newCoreTreatmentIssue }"/>"
						placeholder="Enter a new core treatment issue.">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary" >Save</button>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- End New Default Treatment Issue Modal -->

<!-- Delete Stage Modal -->
<c:forEach items="${treatmentPlan.stages }" var="stage">
	<div class="modal" id="delete_stage_modal${stage.stageID }">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		          <h4 class="modal-title">Delete Stage</h4>
		      </div>
		      <div class="modal-body">
		        <p>Are you sure you want to delete <strong>${stage.title}</strong> from ${treatmentPlan.title}?  <span class="warning-message" >You cannot undo this.</span></p>
		        
		      </div>
		      <div class="modal-footer">
		      	<a role="button" href="/secure/treatment-components/EditTreatmentPlan?requestedAction=stage-delete&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}" class="btn btn-default" title="Delete task (${task.title }) from this stage.">
				  OK
				</a>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      </div>
		    </div>
		  </div>
		</div>
</c:forEach>
<!-- End Delete Stage Modal -->

<!-- Delete TreatmentPlan Modal -->
<div class="modal" id="delete_plan_modal">
	<form class="form-horizontal" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
		<input type="hidden" name="requestedAction" value="delete-plan"> 
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
		
		
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		          <h4 class="modal-title">Delete Treatment Plan</h4>
		      </div>
		      <div class="modal-body">
		        <p>Are you sure you want to delete <strong>${treatmentPlan.title}</strong> from ${owner.userName}'s account? <span class="warning-message" >You cannot undo this.</span></p>
		        
		      </div>
		      <div class="modal-footer">
		      	<button type="submit" class="btn btn-default" aria-hidden="true" title="Delete this treatment plan.">OK</button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      </div>
		    </div>
		  </div>
	</form>
</div>
<!-- End Delete TreatmentPlan Modal -->

<script>
	$(function() {
		$('#selectedCoreTreatmentPlanID').change(function() {
			this.form.submit();
		});
	});
	
	$(function() {
		var form = document.getElementById("form-update-plan");

		document.getElementById("submitForUpdatePlan").addEventListener("click", function () {
		  form.submit();
		});
	});
	
</script>
<c:import url="/WEB-INF/jsp/footer.jsp" />