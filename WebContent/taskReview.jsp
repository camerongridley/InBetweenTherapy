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
						<c:if test="${stage.completed }">*</c:if>
						${stage.name }
					</div>
				</c:if>
				<c:if test="${stage.stageID != currentStage.stageID }">
					<div class="progress-bar progress-bar-info progress-stage-inactive" style="width: ${(100-(txIssue.numberOfStages-1)*separatorWidth)/txIssue.numberOfStages}%">
						<c:if test="${stage.completed }">*</c:if>
						${stage.name }
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
				<c:forEach var="task" items="${currentStage.tasks }" varStatus="taskStatus">
					<c:if test="${task.completed == false }">
						<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
						  <div class="panel-heading panel-heading-task">
						  	<input type="checkbox" id="${task.id }" value="${task.id }" name="taskChkBx[]" aria-label="Task: ${task.name }">
							<a role="button" data-toggle="collapse" href="#collapse${task.id }" aria-expanded="true" aria-controls="collapse${task.id }">
					          ${task.name }
					        </a>
						  </div>
						  <div id="collapse${task.id }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.id }">
							  <div class="panel-body panel-body-task">
							    ${task.description }
							  </div>
						  </div>
						</div>
					</c:if>
				</c:forEach>
				
				<c:forEach var="task" items="${currentStage.tasks }" varStatus="taskStatus">
					<c:if test="${task.completed == true }">
						<div class="panel panel-default panel-task" title="Click the task title to expand and see task details.">
						  <div class="panel-heading panel-heading-task">
						  	<input type="checkbox" id="${task.id }" aria-label="Task: ${task.name }" value="${task.id }" name="taskChkBx[]" checked>
							<a role="button" data-toggle="collapse" href="#collapse${task.id }" aria-expanded="true" aria-controls="collapse${task.id }">
					          <span class="task-completed">${task.name }</span> - Completed ${task.dateCompleted }
					        </a>
						  </div>
						  <div id="collapse${task.id }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.id }">
							  <div class="panel-body panel-body-task">
							    ${task.description }
							  </div>
						  </div>
						</div>
					</c:if>
				</c:forEach>
				
				<hr>
				
				<strong>Extra Tasks</strong>
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
				
			  
				<button type="submit" class="btn btn-primary">Save</button>
				
			</form>
			
		
		</div>
	</div>
</div>
		
<c:import url="footer.jsp" />