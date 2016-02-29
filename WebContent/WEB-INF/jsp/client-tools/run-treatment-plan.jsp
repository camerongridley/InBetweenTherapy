<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:import url="/WEB-INF/jsp/header.jsp" />


<h1>Treatment Plan: ${treatmentPlan.title }
<c:if test='${user.role.equals("therapist") }'>
<a href="/secure/treatment-components/EditTreatmentPlan?requestedAction=plan-edit-load-plan&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&clientUserID=${client.userID}" 
type="button" class="btn btn-default run-plan-edit-button" aria-label="Left Align" title="Edit the Treatment Plan"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a></c:if>
</h1>
<c:import url="/WEB-INF/jsp/message-modal.jsp" />

<div class="form-horizontal">



	<!-- depending on the accessibility of the stage, set the proper css class -->
	<c:forEach var="stage" items="${treatmentPlan.stages }" varStatus="status">
		<form id="stageNode${stage.stageID }" action="/secure/ChangeStage" method="POST">

			<input type="hidden" name="stageIndex" value=${stage.clientStageOrder } >
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" >
			<input type="hidden" name="clientUserID" value=${client.userID } >
			<input type="hidden" name="requestedAction" value="change-stage">
			<input type="hidden" name="path" value="${path }">
			
			<!--For stage that is currently being viewed (enabled-active)-->
	
			<c:if test="${stage.clientStageOrder == treatmentPlan.activeViewStageIndex && stage.inProgress}">
				<div class="progress-stage progress-stage-enabled-active col-sm-${treatmentPlan.stageNodeDisplayColWidthForBootstrap()}">
					${stage.title } <a href="" type="button" data-toggle="modal" data-target="#stageInfoModal"> <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
				</div>
			</c:if>
			
			<!--For stage that is accessible but NOT the active view (enabled-inactive)  removed this condition:  && stage.clientStageOrder <= treatmentPlan.currentStageIndex-->
			<c:if test="${stage.clientStageOrder != treatmentPlan.activeViewStageIndex  && stage.inProgress}">
				<div class="progress-stage progress-stage-enabled-inactive col-sm-${treatmentPlan.stageNodeDisplayColWidthForBootstrap()}">
					<a href='' onclick='this.parentNode.parentNode.submit(); return false;'>${stage.title }</a>
				</div>
			</c:if>
			
			<!--For stage that is disabled at this time and active as current view (disabled-active)-->
			<c:if test="${stage.clientStageOrder == treatmentPlan.activeViewStageIndex && !stage.inProgress  && !stage.completed}">
				<div class="progress-stage progress-stage-disabled-active col-sm-${treatmentPlan.stageNodeDisplayColWidthForBootstrap()}">
					${stage.title } <a href="" type="button" data-toggle="modal" data-target="#stageInfoModal"> <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
				</div>
			</c:if>
			
			<!--For stage that is disabled at this time and inactive as current view (disabled-inactive)-->
			<c:if test="${stage.clientStageOrder != treatmentPlan.activeViewStageIndex && !stage.inProgress && !stage.completed}">
				<div class="progress-stage progress-stage-disabled-inactive col-sm-${treatmentPlan.stageNodeDisplayColWidthForBootstrap()}">
					<a href='' onclick='this.parentNode.parentNode.submit(); return false;'>${stage.title }</a>
				</div>
			</c:if>
		
		</form>
	</c:forEach>




</div>

<!-- Modal popup for information about stage currently being viewed -->
<div class="modal fade" id="stageInfoModal" tabindex="-1" role="dialog" aria-labelledby="stageInfoModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="stageInfoModalLabel">${activeStage.title} Overview and Goals</h4>
			</div>
			<div class="modal-body">
				<p>${activeStage.description}</p>
				<div class="well well-sm">
					Goals:
					<c:forEach items="${activeStage.goals}" var="goal">
						<ul>
							<li>
								${goal.description}
							</li>
						</ul>
					</c:forEach>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- End modal -->

<div class="row">
	<div class="col-sm-12">

		<form action="/secure/UpdateTaskCompletion" method="post" class="form-inline">
			<input type="hidden" name="requestedAction" value="update-client-plan">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" />
			<input type="hidden" name="clientID" value=${client.userID } >
			<input type="hidden" name="path" value="${path }">
			<c:set var="activeViewStagePercentComplete" value="${treatmentPlan.activeViewStage.percentComplete * 100}"></c:set>
			<div class="progress-stage-detail">
				<strong>
					Stage: <c:out value="${treatmentPlan.activeViewStage.title }" /> - ${activeViewStagePercentComplete}% Complete
				</strong>
				<c:if test='${user.role.equals("therapist") }'>
				<a role="button"
					href="/secure/treatment-components/EditStage?requestedAction=select-stage&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${treatmentPlan.activeViewStage.stageID}" 
					class="btn btn-default btn-xs run-plan-edit-button" title="Edit this stage.">
					<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
				</a>
				</c:if>
			</div>
			<div class="progress">
				<div class="progress-bar progress-bar-success" role="progressbar"
					aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
					style="width: ${activeViewStagePercentComplete }%;">
					<strong>${activeViewStagePercentComplete }%</strong>
				</div>
			</div>
			
			<!--INCOMPLETE PRIMARY TASKS-->
			<c:forEach var="task"
				items="${treatmentPlan.activeViewStage.incompleteTasks }"
				varStatus="taskStatus">
				<div class="panel panel-default panel-task"
					title="Click the task title to expand and see task details.">
					<div class="panel-heading panel-heading-task">
						<input type="hidden" name="allTaskIDs" value="${task.taskID}" /> 
						<input type="hidden" name="taskTypeName${task.taskID}" value="${task.taskTypeName}" /> 
						<input class="responsive-checkbox" type="checkbox" id="${task.taskID }" value="${task.taskID }" name="taskChkBx[]" aria-label="Task: ${task.title }" <c:if test="${task.disabled }">disabled</c:if>/> 
						<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
							${task.title } - Task Type: ${task.taskTypeName } 
						</a>
						<c:if test='${user.role.equals("therapist") }'>
							<a role="button"
								href="/secure/treatment-components/EditTask?requestedAction=edit-task-select-task&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${treatmentPlan.activeViewStage.stageID}&taskID=${task.taskID}" 
								class="btn btn-default btn-xs run-plan-edit-button" title="Edit this task">
								<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
							</a>
						</c:if>
					</div>
					<!--Generic Task Detail-->
					<c:if test="${task.taskTypeName == 'TaskGeneric' }">
						<div id="collapse${task.taskID }" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="heading${task.taskID }">
							<div class="panel-body panel-body-task">
								${task.instructions }</div>
							<c:if test="${task.resourceLink != '' }">
								<div class="panel-body panel-body-task">
									<a href="${task.resourceLink }" target="_blank">Resource
										Link</a>
								</div>
							</c:if>
						</div>
					</c:if>
					<!--TwoTextBoxes Task Detail-->
					<c:if test="${task.taskTypeName == 'TaskTwoTextBoxes' }">
						<div id="collapse${task.taskID }" class="panel-collapse collapse"
							role="tabpanel" aria-labelledby="heading${task.taskID }">
							<div class="panel-body panel-body-task">
								${task.instructions }</div>
							<div class="panel-body panel-body-task">
								${task.extraTextLabel1 }<input type="text" class="form-control"
									placeholder="${task.extraTextLabel1 }"
									name="extraTextValue1${task.taskID }"
									value="${task.extraTextValue1 }" 
									<c:if test="${task.disabled }">disabled</c:if>>
							</div>
							<div class="panel-body panel-body-task">
								${task.extraTextLabel2 }<input type="text" class="form-control"
									placeholder="${task.extraTextLabel2 }"
									name="extraTextValue2${task.taskID }"
									value="${task.extraTextValue2 }" 
									<c:if test="${task.disabled }">disabled</c:if>>
							</div>
						</div>
					</c:if>

				</div>
			</c:forEach>


			<!--COMPLETED PRIMARY TASKS-->
			<c:forEach var="task"
				items="${treatmentPlan.activeViewStage.completedTasks }"
				varStatus="taskStatus">
				<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
					<div class="panel-heading panel-heading-task">
						<input type="hidden" name="allTaskIDs" value="${task.taskID}" /> 
						<input type="hidden" name="taskTypeName${task.taskID}" value="${task.taskTypeName}" /> 
						<input class="responsive-checkbox" type="checkbox" id="${task.taskID }" aria-label="Task: ${task.title }" value="${task.taskID }" name="taskChkBx[]" checked
							<c:if test="${task.disabled }">disabled</c:if>> 
							<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }"> 
							<span class="task-completed">${task.title }</span> -  ${task.dateCompletedFormatted }
							</a>
							<c:if test='${user.role.equals("therapist") }'>
							<a role="button"
								href="/secure/treatment-components/EditTask?requestedAction=edit-task-select-task&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${treatmentPlan.activeViewStage.stageID}&taskID=${task.taskID}" 
								class="btn btn-default btn-xs run-plan-edit-button" title="Edit this task">
								<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
							</a>
						</c:if>
					</div>
					<div id="collapse${task.taskID }" class="panel-collapse collapse"
						role="tabpanel" aria-labelledby="heading${task.taskID }">
						<div class="panel-body panel-body-task">${task.instructions }
						</div>
						<c:if test="${task.taskTypeName == 'TaskTwoTextBoxes'}">
							<div class="panel-body panel-body-task">
								${task.extraTextLabel1 }<input type="text" class="form-control"
									placeholder="${task.extraTextLabel1 }"
									name="extraTextValue1${task.taskID }"
									value="${task.extraTextValue1 }"
									<c:if test="${task.disabled }">disabled</c:if>>
							</div>
							<div class="panel-body panel-body-task">
								${task.extraTextLabel2 }<input type="text" class="form-control"
									placeholder="${task.extraTextLabel2 }"
									name="extraTextValue2${task.taskID }"
									value="${task.extraTextValue2 }"
									<c:if test="${task.disabled }">disabled</c:if>>
							</div>
						</c:if>
					</div>
				</div>
			</c:forEach>

			<hr>

			<!--INCOMPLETE EXTRA TASKS-->
			<strong>Extra Tasks (Placeholder text - Functionality not yet implemented)</strong>
			<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
				<div class="panel-heading panel-heading-task">
					<input class="responsive-checkbox" type="checkbox" id="0" aria-label="Task: Temp Extra Task"
						value="0" name="taskChkBx[]" <c:if test="${task.disabled }">disabled</c:if>> <a role="button"
						data-toggle="collapse" href="#collapse121212" aria-expanded="true"
						aria-controls="collapse121212"> Sample Extra Task </a>
						<c:if test='${user.role.equals("therapist") }'>
							<a role="button"
								href="/secure/treatment-components/EditTask?requestedAction=edit-task-select-task&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${treatmentPlan.activeViewStage.stageID}&taskID=${task.taskID}" 
								class="btn btn-default btn-xs run-plan-edit-button" title="Edit this task">
								<span class="glyphicon glyphicon-edit run-plan-edit-button" aria-hidden="true"></span>
							</a>
						</c:if>
				</div>
				<div id="collapse121212" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading121212">
					<div class="panel-body panel-body-task">Extra task
						instructions would go here.</div>
				</div>
			</div>

			<!--COMPLETED EXTRA TASKS-->
			
			<c:choose>
				<c:when test='${path.equals("manageClients") }'>
				<!--  -->
					<div class="form-group">
			            <div class="col-sm-12 save-button">
			                <button type="submit" class="btn btn-primary">Done</button>
			            </div>
			        </div>
				</c:when>
				<c:otherwise>
					<div class="form-group">
			            <div class="col-sm-12 save-button">
			                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save</button>
			                <button type="submit" name="submitButton"  value="back" class="btn btn-default">Back</button>
			            </div>
			        </div>
				</c:otherwise>
			</c:choose>

		</form>
		
		

	</div>
</div>


<c:import url="/WEB-INF/jsp/footer.jsp" />