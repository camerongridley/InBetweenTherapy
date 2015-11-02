<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />
	
	
	<h1>Treatment Issue: ${treatmentPlan.title }</h1>
	
	<c:import url="/jsp/message-modal.jsp"/>
	
	<div class="row">
		<div class="col-md-12">
			
				<div class="progress">
				<c:set var="separatorWidth" value=".2"></c:set>
					<!-- depending on the accessibility of the stage, set the proper css class -->
				<c:forEach var="stage" items="${treatmentPlan.stages }" varStatus="stageStatus">
					<!---------------------------------------------------------
					For stage that is currently being viewed (enabled-active)
					---------------------------------------------------------->
					<c:if test="${stage.stageOrder == treatmentPlan.activeViewStageIndex }">
						<div class="progress-bar progress-bar-primary progress-stage-enabled-active" style="width: ${(100-(treatmentPlan.numberOfStages-1)*separatorWidth)/treatmentPlan.numberOfStages}%">
							<c:if test="${stage.stageOrder <= treatmentPlan.currentStageIndex }"><form action="./ChangeStage" method="POST"></c:if>
							${stage.title }
								<a href="#" type="button" data-toggle="modal" data-target="#stageInfoModal">
									<span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
								</a>
								<input type="hidden" name="stageIndex" value=${stage.stageOrder } /><input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" />
							<c:if test="${stage.stageOrder <= treatmentPlan.currentStageIndex  }"></form></c:if>
						</div>
						<!-- Modal popup for information about stage currently being viewed -->
						<div class="modal fade" id="stageInfoModal" tabindex="-1" role="dialog" aria-labelledby="stageInfoModalLabel">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
										<h4 class="modal-title" id="stageInfoModalLabel">${stage.title} Overview and Goals</h4>
									</div>
									<div class="modal-body">
										<p>${stage.description}</p>
										<div class="well well-sm">
											Goals:
											<c:forEach items="${stage.goals}" var="goal">
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
						</div><!-- End modal -->
					</c:if>
					<!---------------------------------------------------------------------
					For stage that is accessible but NOT the active view (enabled-inactive)
					---------------------------------------------------------------------->
					<c:if test="${stage.stageOrder != treatmentPlan.activeViewStageIndex && stage.stageOrder <= treatmentPlan.currentStageIndex}">
						<div class="progress-bar progress-bar-info progress-stage-enabled-inactive" style="width: ${(100-(treatmentPlan.numberOfStages-1)*separatorWidth)/treatmentPlan.numberOfStages}%">
							<c:if test="${stage.stageOrder <= treatmentPlan.currentStageIndex  }"><form action="./ChangeStage" method="POST"><a href='#' onclick='this.parentNode.submit(); return false;'></c:if>
							${stage.title }<input type="hidden" name="stageIndex" value=${stage.stageOrder } /><input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" />
							<c:if test="${stage.stageOrder <= treatmentPlan.currentStageIndex  }"></a></form></c:if>
						</div>
					</c:if>
					<!----------------------------------------------------
					For stage that is inaccssible at this time (disabled)
					----------------------------------------------------->
					<c:if test="${stage.stageOrder != treatmentPlan.activeViewStageIndex && stage.stageOrder > treatmentPlan.currentStageIndex}">
						<div class="progress-bar progress-bar-info progress-stage-disabled" style="width: ${(100-(treatmentPlan.numberOfStages-1)*separatorWidth)/treatmentPlan.numberOfStages}%">
								${stage.title }<input type="hidden" name="stageIndex" value=${stage.stageOrder } />
						</div>
					</c:if>
					<c:if test="${!stageStatus.last }">
						<div class="progress-bar progress-bar-separator" style="width: ${separatorWidth}%"></div>
					</c:if>
				</c:forEach>
				  
				</div>

		</div>
	</div>
	
	<!--  NESTED PROGRESS BARS
	<div class="row">
		<div class="col-md-12">
			<div class="progress">
			  <div class="progress-bar progress-bar-success" style="width: 35%">
			  		<div class="progress" style="; background-color: #337ab7;">
					  <div class="progress-bar progress-bar-striped" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 30%;">
					  </div>
					</div>
			  </div>
			  <div class="progress-bar progress-bar-warning" style="width: 20%">
			  </div>
			  <div class="progress-bar progress-bar-danger" style="width: 10%">
			  </div>
			</div>
		</div>
	</div>
	
	-->
	
	<div class="row">
		<div class="col-md-12">

			<form action="./UpdateTaskCompletion" method="post" class="form-inline">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" />
			<strong>Stage: <c:out value="${treatmentPlan.activeViewStage.title }" /> - ${treatmentPlan.activeViewStage.percentComplete }% Complete</strong>
			<div class="progress">
			  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${treatmentPlan.activeViewStage.percentComplete }%;">
			    <strong>${treatmentPlan.activeViewStage.percentComplete }%</strong>
			  </div>
			</div>
				<!---------------------------------------------------------
				 INCOMPLETE PRIMARY TASKS
				 ---------------------------------------------------------->
				<c:forEach var="task" items="${treatmentPlan.activeViewStage.incompleteTasks }" varStatus="taskStatus">
						<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
						  <div class="panel-heading panel-heading-task">
						  	<input type="hidden" name="allTaskIDs" value="${task.taskID}"/>
							  <input type="hidden" name="taskTypeName${task.taskID}" value="${task.taskTypeName}"/>
						  	<input type="checkbox" id="${task.taskID }" value="${task.taskID }" name="taskChkBx[]" aria-label="Task: ${task.title }">
							<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
					          ${task.title } - Task Type: ${task.taskTypeName }
					        </a>
							  <!-- TODO task set/repetition info goes here -->
						  </div>
							<!---------------------------------------------------------
							 Generic Task Detail
							 ---------------------------------------------------------->
							  <c:if test="${task.taskTypeName == 'TaskGeneric' }">
								  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
									  <div class="panel-body panel-body-task">
										${task.instructions }
									  </div>
								  </div>
							  </c:if>
							<!---------------------------------------------------------
							 TwoTextBoxes Task Detail
							 ---------------------------------------------------------->
							  <c:if test="${task.taskTypeName == 'TaskTwoTextBoxes' }">
								  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
									  <div class="panel-body panel-body-task">
										${task.instructions }
									  </div>
									  <div class="panel-body panel-body-task">
										${task.extraTextLabel1 }<input type="text" class="form-control" placeholder="${task.extraTextLabel1 }" name="extraTextLabel1${task.taskID }" value="${task.extraTextValue1 }">
									  </div>
									  <div class="panel-body panel-body-task">
										${task.extraTextLabel2 }<input type="text" class="form-control" placeholder="${task.extraTextLabel2 }" name="extraTextLabel2${task.taskID }" value="${task.extraTextValue2 }">
									  </div>
								  </div>
							  </c:if>
							
						</div>
				</c:forEach>


				<!---------------------------------------------------------
				 COMPLETED PRIMARY TASKS
				 ---------------------------------------------------------->
				<c:forEach var="task" items="${treatmentPlan.activeViewStage.completedTasks }" varStatus="taskStatus">
						<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
						  <div class="panel-heading panel-heading-task">
						  	<input type="hidden" name="allTaskIDs" value="${task.taskID}"/>
						  	<input type="hidden" name="taskTypeName${task.taskID}" value="${task.taskTypeName}"/>
						  	<input type="checkbox" id="${task.taskID }" aria-label="Task: ${task.title }" value="${task.taskID }" name="taskChkBx[]" checked>
							<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
					          <span class="task-completed">${task.title }</span> - Completed ${task.dateCompleted }
					        </a>
						  </div>
						  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
							  <div class="panel-body panel-body-task">
							    ${task.instructions }
							  </div>
							  <c:if test="${task.taskTypeName == 'TaskTwoTextBoxes'}">
								  <div class="panel-body panel-body-task">
										${task.extraTextLabel1 }<input type="text" class="form-control" placeholder="${task.extraTextLabel1 }" name="extraTextLabel1${task.taskID }" value="${task.extraTextValue1 }">
								  </div>
								  <div class="panel-body panel-body-task">
									${task.extraTextLabel2 }<input type="text" class="form-control" placeholder="${task.extraTextLabel2 }" name="extraTextLabel2${task.taskID }" value="${task.extraTextValue2 }">
								  </div>
							  </c:if>
						  </div>
						</div>
				</c:forEach>
				
				<hr>

				<!---------------------------------------------------------
				  INCOMPLETE EXTRA TASKS
				 ---------------------------------------------------------->
				<strong>Extra Tasks (Placeholder text - Functionality not yet implemented)</strong>
				<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
				  <div class="panel-heading panel-heading-task">
				  	<input type="checkbox" id="0" aria-label="Task: Temp Extra Task" value="0" name="taskChkBx[]">
					<a role="button" data-toggle="collapse" href="#collapse121212" aria-expanded="true" aria-controls="collapse121212">
			          Sample Extra Task
			        </a>
				  </div>
				  <div id="collapse121212" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading121212">
					  <div class="panel-body panel-body-task">
					    Extra task instructions would go here.
					  </div>
				  </div>
				</div>

				<!---------------------------------------------------------
				 COMPLETED EXTRA TASKS
				 ---------------------------------------------------------->
			  
				<button type="submit" class="btn btn-primary">Save</button>
				
			</form>
			
		
		</div>
	</div>
		
<c:import url="/jsp/footer.jsp" />