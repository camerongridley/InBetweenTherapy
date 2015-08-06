<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="header.jsp" />
	
	
	
<div class="container">
	<h1>Treatment Issue: ${txIssue.name }</h1>
	<div class="row">
		<div class="col-md-12">
			
				<div class="progress">
				<c:set var="separatorWidth" value=".2"></c:set>
				<c:forEach var="stage" items="${txIssue.stages }" varStatus="stageStatus">
					<c:if test="${stage.stageID == currentStage.stageID }">
						<div class="progress-bar progress-bar-primary" style="width: ${(100-(txIssue.numberOfStages-1)*separatorWidth)/txIssue.numberOfStages}%">
							<c:if test="${stage.completed }"><form action="./ChangeStage" method="POST"><a href='#' onclick='this.parentNode.submit(); return false;'></c:if>
							${stage.name }<input type="hidden" name="stageID" value=${stage.stageID } />
							<c:if test="${stage.completed }"></a></form></c:if>
						</div>
					</c:if>
					<c:if test="${stage.stageID != currentStage.stageID }">
						<div class="progress-bar progress-bar-info progress-stage-inactive" style="width: ${(100-(txIssue.numberOfStages-1)*separatorWidth)/txIssue.numberOfStages}%">
							<c:if test="${stage.completed }"><form action="./ChangeStage" method="POST"><a href='#' onclick='this.parentNode.submit(); return false;'></c:if>
							${stage.name }<input type="hidden" name="stageID" value=${stage.stageID } />
							<c:if test="${stage.completed }"></a></form></c:if>
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
			
			<strong>Stage: <c:out value="${currentStage.name }" /> - ${currentStage.percentComplete }% Complete</strong>
			<div class="progress">
			  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${currentStage.percentComplete }%;">
			    <strong>${currentStage.percentComplete }%</strong>
			  </div>
			</div>
				<!---------------------------------------------------------
				 INCOMPLETE PRIMARY TASKS
				 ---------------------------------------------------------->
				<c:forEach var="task" items="${currentStage.tasks }" varStatus="taskStatus">
					<c:if test="${task.completed == false }">
						<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
						  <div class="panel-heading panel-heading-task">
						  	<input type="checkbox" id="${task.taskID }" value="${task.taskID }" name="taskChkBx[]" aria-label="Task: ${task.name }">
							<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
					          ${task.name } - Task Type: ${task.taskTypeName }
					        </a>
						  </div>
							<!---------------------------------------------------------
							 PsychEd Tasks
							 ---------------------------------------------------------->
							  <c:if test="${task.taskTypeName == 'PsychEdTask' }">
								  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
									  <div class="panel-body panel-body-task">
										${task.description }
									  </div>
								  </div>
							  </c:if>
							<!---------------------------------------------------------
							 Relaxation Tasks
							 ---------------------------------------------------------->
							  <c:if test="${task.taskTypeName == 'RelaxationTask' }">
								  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
									  <div class="panel-body panel-body-task">
										${task.description }
									  </div>
									  <div class="panel-body panel-body-task">
										Duration: ${task.durationInMinutes }
									  </div>
								  </div>
							  </c:if>
							<!---------------------------------------------------------
							 Cognitive Tasks
							 ---------------------------------------------------------->
							  <c:if test="${task.taskTypeName == 'CognitiveTask' }">
								  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
									  <div class="panel-body panel-body-task">
										${task.description }
									  </div>
									  <div class="panel-body panel-body-task">
										<input type="text" class="form-control" placeholder="Enter your automatic thought." name="automaticThought${task.taskID }" value="${task.automaticThought }">
									  </div>
									  <div class="panel-body panel-body-task">
										 <input type="text" class="form-control" placeholder="Enter your balanced or alternative thought." name="alternativeThought${task.taskID }" value="${task.alternativeThought }">
									  </div>
								  </div>
							  </c:if>
						</div>
					</c:if>
				</c:forEach>


				<!---------------------------------------------------------
				 COMPLETED PRIMARY TASKS
				 ---------------------------------------------------------->
				<c:forEach var="task" items="${currentStage.tasks }" varStatus="taskStatus">
					<c:if test="${task.completed == true }">
						<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
						  <div class="panel-heading panel-heading-task">
						  	<input type="checkbox" id="${task.taskID }" aria-label="Task: ${task.name }" value="${task.taskID }" name="taskChkBx[]" checked>
							<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
					          <span class="task-completed">${task.name }</span> - Completed ${task.dateCompleted }
					        </a>
						  </div>
						  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID }">
							  <div class="panel-body panel-body-task">
							    ${task.description }
							  </div>
							  <div class="panel-body panel-body-task">
								  <input type="text" class="form-control" placeholder="Enter your automatic thought." name="automaticThought${task.taskID }" value="${task.automaticThought }">
							  </div>
							  <div class="panel-body panel-body-task">
								  <input type="text" class="form-control" placeholder="Enter your balanced or alternative thought." name="alternativeThought${task.taskID }" value="${task.alternativeThought }">
							  </div>
						  </div>
						</div>
					</c:if>
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
					    Extra task description would go here.
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
</div>
		
<c:import url="footer.jsp" />