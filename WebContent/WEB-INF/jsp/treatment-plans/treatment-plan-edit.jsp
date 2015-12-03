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
	<h1>Update Treatment Plan</h1>
	<h2>Add and update detail to the treatment plan</h2>
</div>

<c:import url="/WEB-INF/jsp/message-modal.jsp" />


<c:if test="${path=='treatmentPlanTemplate' }">
<div class="row">
	<div class="form-group">
		<form class="form-horizontal" action="/secure/EditTreatmentPlan" method="POST">
			<input type="hidden" name="requestedAction" value="plan-edit-select-plan"> 
			<input type="hidden" name="path" value="${path }">
			
				<label for="selectedDefaultTreatmentPlanID"
					class="col-sm-2 control-label">Select a Treatment Plan</label>
				<div class="col-sm-8">
					<select class="form-control" id="selectedDefaultTreatmentPlanID"
						name="selectedDefaultTreatmentPlanID">
						<option value="">Select a treatment plan to edit.</option>
						<c:forEach var="defaultPlan" items="${defaultTreatmentPlanList }">
							<option value="${defaultPlan.treatmentPlanID}"
								<c:if test="${defaultPlan.treatmentPlanID == treatmentPlan.treatmentPlanID }">selected</c:if>>${defaultPlan.title}</option>
						</c:forEach>
					</select>
		
				</div>
			
		</form>	
		<div class="col-xs-1">
			<form class="form-horizontal" action="/secure/CreateTreatmentPlan" method="POST">
				<input type="hidden" name="requestedAction" value="plan-create-start"> 
				<input type="hidden" name="path" value="treatmentPlanTemplate">
				<button type="submit" class="btn btn-default glyphicon glyphicon-plus" aria-hidden="true" title="Add a new treatment plan."></button>
			</form>
		</div>	
		<div class="col-xs-1">
			<form class="form-horizontal" action="/secure/EditTreatmentPlan" method="POST">
				<input type="hidden" name="requestedAction" value="delete-plan"> 
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
				<button type="submit" class="btn btn-default glyphicon glyphicon-remove" aria-hidden="true" title="Delete this treatment plan."></button>
			</form>
		</div>
	</div>
</div>

<hr>
</c:if>


<form class="form-horizontal" action="/secure/EditTreatmentPlan" method="POST">
	<input type="hidden" name="requestedAction" value="plan-edit-update">
	<input type="hidden" name="path" value="${path }"> 
	<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
	
	<div class="row form-group">
		<label for="planName" class="col-sm-2 control-label">Plan Name</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="planTitle"
				name="planTitle" value="${fn:escapeXml(treatmentPlan.title) }"
				placeholder="Enter a treatment plan name here.">
		</div>
	</div>
	<div class="row form-group">
		<label for="planDescription" class="col-sm-2 control-label">Plan
			Description</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="planDescription"
				name="planDescription" value="${fn:escapeXml(treatmentPlan.description) }"
				placeholder="Describe the treatment plan.">
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
			<label for="defaultTreatmentIssue" class="col-sm-2 control-label">Default
				Tx Issues</label>
			<div class="col-sm-9">
				<select class="form-control" id="defaultTreatmentIssue"
					name="defaultTreatmentIssue">
					<option value="">Select a default treatment issue.</option>
					<c:forEach items="${defaultTreatmentIssues}" var="defaultIssue">
						<option value="${defaultIssue.treatmentIssueID}"
							<c:if test="${defaultIssue.treatmentIssueID == treatmentPlan.treatmentIssueID}">selected</c:if>>${defaultIssue.treatmentIssueName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-sm-1">
				<button type="button" class="btn btn-default" title="Add a new default treatment issue."
					aria-label="Left Align" data-toggle="modal"
					data-target="#newDefaultTreatmentIssueModal">
					<span class="glyphicon glyphicon-plus" aria-hidden="true" ></span>
				</button>
			</div>
		</div>

		<c:if test="${customTreatmentIssues != null }">
			<div class="row form-group">
				<label for="customTreatmentIssue" class="col-sm-2 control-label">Existing
					Custom Tx Issues</label>
				<div class="col-sm-10">
					<select class="form-control" id="customTreatmentIssue"
						name="customTreatmentIssue">
						<option value="">Or select an issue you've previously
							created.</option>
						<c:forEach items="${customTreatmentIssues}" var="customIssue">
							<option value="${customIssue.treatmentIssueID}">${fn:escapeXml(customIssue.treatmentIssueName)}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-1">
					<button type="button" class="btn btn-default"
						aria-label="Left Align" data-toggle="modal"
						data-target="#newCustomTreatmentIssueModal">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
				</div>
			</div>

		</c:if>
	</div>



	<label for="stageList" class="control-label">Stages <a
		role="button"
		href="/secure/CreateStage?requestedAction=add-stage-to-treatment-plan&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}"
		class="btn btn-default btn-xs"
		title="Add a stage to this treatment plan."> <span
			class="glyphicon glyphicon-plus" aria-hidden="true"></span>
	</a>

	</label>
	<c:forEach items="${treatmentPlan.stages }" var="stage">
		<div class="panel panel-default panel-task" id="stageList"
			title="Click the stage title to expand and see the stage details.">
			<div class="panel-heading">
				<input type="hidden" name="stageID" value="${stage.stageID}" /> <input
					type="hidden" name="stageTitle${stage.stageID}"
					value="${stage.title}" /> <a role="button" data-toggle="collapse"
					href="#collapse${stage.stageID }" aria-expanded="true"
					aria-controls="collapse${stage.stageID }">
					${stage.stageOrderForUserDisplay } - <span class="">${stage.title }</span>
				</a> <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
				<a role="button"
					href="/secure/EditTreatmentPlan?requestedAction=stage-delete&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}"
					class="btn btn-default btn-xs pull-right"
					title="Delete this stage."> <span
					class="glyphicon glyphicon-remove" aria-hidden="true"></span>
				</a> <a role="button"
					href="/secure/EditStage?requestedAction=stage-edit&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}"
					class="btn btn-default btn-xs pull-right" title="Edit this stage.">
					<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
				</a>

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
								<tr>
									<!-- <th scope="row">${task.taskOrder}</th>-->
									<td>${task.title} (${task.repetitions }) <span
										class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
										<!-- <a role="button"
										href="/secure/EditTask?requestedAction=edit-task-select-task&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}&taskID=${task.taskID}"
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

	<div class="row form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-default">Save</button>
		</div>
	</div>
</form>

<!-- New Default Treatment Issue Modal -->
<div class="modal fade" id="newDefaultTreatmentIssueModal" tabindex="-1"
	role="dialog" aria-labelledby="newDefaultTreatmentIssueModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form class="form-horizontal" action="/secure/EditTreatmentPlan"
				method="POST">
				<input type="hidden" name="requestedAction"
					value="create-default-treatment-issue"> <input
					type="hidden" name="path" value="${path }"> <input
					type="hidden" name="treatmentPlanID"
					value="${treatmentPlan.treatmentPlanID}">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="newDefaultTreatmentIssueModalLabel">Enter
						a new default Treatment Issue</h4>
				</div>
				<div class="modal-body">
					<input type="text" class="form-control"
						id="newDefaultTreatmentIssue" name="newDefaultTreatmentIssue"
						value="${fn:escapeXml(newDefaultTreatmentIssue) }"
						placeholder="Enter a new default treatment issue.">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary">Save</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script>
	$(function() {
		$('#selectedDefaultTreatmentPlanID').change(function() {
			this.form.submit();
		});
	});
</script>
<c:import url="/WEB-INF/jsp/footer.jsp" />