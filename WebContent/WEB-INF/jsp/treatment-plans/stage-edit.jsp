
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h2>Edit Stage</h2>
	<c:import url="/WEB-INF/jsp/includes/breadcrumbs.jsp" />
</div>
  
<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
	<form class="form-horizontal" action="/secure/treatment-components/EditStage" method="POST">
		<input type="hidden" name="requestedAction" value="select-stage">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
		<input type="hidden" name="clientUUID" value="${clientUUID }" >	
		
		<c:if test="${path=='templateStage'}">
			<div class="well well-sm">
				<div class="form-group">
					<label for="selectedCoreStageID" class="col-sm-2 control-label">Select Default Stage</label>
			        <div class="col-sm-5">
			        <a id="selectStage"></a>
			            <select class="form-control" id="selectedCoreStageID" name="stageID">
			                <option  value="">Select a stage to edit.</option>
			                <c:forEach var="coreStage" items="${coreStagesList }">
			                    <option value="${coreStage.stageID}" <c:if test="${coreStage.stageID == stage.stageID }">selected</c:if> >${coreStage.title}</option>
			                </c:forEach>
			            </select>
			        </div>
				</div>
			</div>
		</c:if>
	</form>
	
	<form class="form-horizontal" action="/secure/treatment-components/EditStage" method="POST">
		<input type="hidden" name="requestedAction" value="stage-edit-update">
		<input type="hidden" name="path" value="${path }">	
		<input type="hidden" name="stageID" value="${stage.stageID }" >
		<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
		<input type="hidden" name="clientUUID" value="${clientUUID }" >	
		
        <div class="form-group">
            <label for="stageTitle" class="col-sm-2 control-label">Stage Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageTitle" name="stageTitle" value="${stage.title }" <c:if test="${stage==null }">disabled="disabled"</c:if>
                placeholder="Enter a stage name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageDescription" name="stageDescription" <c:if test="${stage==null }">disabled="disabled"</c:if> 
                value="${stage.description }" placeholder="Describe the stage.">
            </div>
        </div>

		<a id="goalListTop"></a>
		<div class="form-group">
			<div class="col-sm-12">
				<label for="stageGoalList" class="control-label">Stage Goals
				<c:if test="${stage != null }">
					<button type="button" class="btn btn-default btn-xs" aria-label="Left Align" data-toggle="modal" data-target="#newStageGoalModal" title="Add a goal to this stage.">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
				</c:if>	
				</label>
			</div>
				
			<c:forEach items="${stage.goals }" var="goal">
			<div class="form-horizontal">
					<div class="col-sm-11">
		                <input type="text" class="form-control" id="stageGoalDescription${goal.stageGoalID}" name="stageGoalDescription${goal.stageGoalID}" value="${goal.description }" placeholder="Describe the goal.">
		            </div>
		            <div class="col-sm-1">    
		                <a role="button" data-toggle="modal" data-target="#delete_goal_modal${goal.stageGoalID }" class="btn btn-default btn-xs pull-left" title="Delete goal from this stage.">
						  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
						</a>
		            </div>
			</div>
	            
			</c:forEach>
				
		</div>
		
		<a id="taskListTop"></a>
		<label for="taskList" class="control-label">Tasks
			<c:if test="${stage != null }">
       			<a role="button" href="/secure/treatment-components/CreateTask?requestedAction=create-task-start&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}&clientUUID=${clientUUID}" class="btn btn-default btn-xs" title="Add a task to this stage.">
				  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
				</a>
			</c:if>
		</label>
			
			<c:forEach items="${stage.tasks }" var="task">
			<c:set var="mappedStageTaskInfo" value="${stage.getMappedTaskTemplateByTaskID(task.taskID)}" />
			<input type="hidden" name="allTaskIDs" value="listItem${task.taskID }">
			<a id="task${task.taskID }"></a>
			<div class="row">
			<div class="col-sm-10">
				<div class="panel panel-default panel-task" id="taskList" title="Click the task title to expand and see details.">
					  <div class="panel-heading">
					  	  
						  <div class="row">
								<div class="col-sm-11">
								  	<input type="hidden" name="taskID" value="${task.taskID}"/>
								  	<input type="hidden" name="taskTitle" value="${task.title}"/>
								  	<c:choose>
							          	<c:when test='${task.template }'>
							          		<c:set var="taskOrder" value="${mappedStageTaskInfo.templateTaskOrder }"/>
							          	</c:when>
							          	<c:otherwise>
							          		<c:set var="taskOrder" value="${task.clientTaskOrder }"/>
							          	</c:otherwise>
							         </c:choose>

								  	
								  	<a href="/secure/treatment-components/EditStage?requestedAction=increase-task-order&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}&taskID=${task.taskID}&taskOrder=${taskOrder}&clientUUID=${clientUUID}" title="Move task up."><span class="glyphicon glyphicon-chevron-up"></span></a>
								  	&nbsp;<a href="/secure/treatment-components/EditStage?requestedAction=decrease-task-order&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}&taskID=${task.taskID}&taskOrder=${taskOrder}&clientUUID=${clientUUID}" title="Move task down."><span class="glyphicon glyphicon-chevron-down"></span></a>&nbsp;
									<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
							          ${taskOrder+1} - <span class="">${task.title }</span>
							        </a>   
								</div>
								<div class="col-sm-1">
							        <a role="button" href="/secure/treatment-components/EditTask?requestedAction=edit-task-select-task&path=${path}&treatmentPlanID=${treatmentPlan.treatmentPlanID}&stageID=${stage.stageID}&taskID=${task.taskID}&clientUUID=${clientUUID}" class="btn btn-default btn-xs pull-left" title="Edit task: ${task.title }">
									  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
									</a>
									<a role="button" data-toggle="modal" data-target="#delete_task_modal${task.taskID }" class="btn btn-default btn-xs pull-left" title="Delete task (${task.title }) from this stage.">
									  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
									</a>
								</div>
						  </div><!-- end row -->
					  </div><!-- end panel-heading -->
					  
					  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID}">
						  <div class="panel-body">
						    ${task.instructions } 
						  </div>
					  </div>
				</div>
			</div>
			<div class="col-sm-2">
				<c:if test='${path.equals("templateTreatmentPlan") || path.equals("templateStage")}'>
					<div class="panel panel-primary panel-task" id="taskReps" title="Number of repetitions.">
					<div class="panel-heading">
					  Repetitions: <select class="task-repetition-dropdown" title="Number of repetitions." id="taskTemplateRepetitions${task.taskID }" name="taskTemplateRepetitions${task.taskID }">
	                    <option  value="1" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==1 }">selected</c:if>>1</option>
						<option  value="2" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==2 }">selected</c:if>>2</option>
						<option  value="3" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==3}">selected</c:if>>3</option>
						<option  value="4" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==4 }">selected</c:if>>4</option>
						<option  value="5" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==5 }">selected</c:if>>5</option>
						<option  value="6" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==6 }">selected</c:if>>6</option>
						<option  value="7" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==7 }">selected</c:if>>7</option>
						<option  value="8" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==8 }">selected</c:if>>8</option>
						<option  value="9" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==9 }">selected</c:if>>9</option>
						<option  value="10" <c:if test="${mappedStageTaskInfo.templateTaskRepetitions==10 }">selected</c:if>>10</option>
	                </select>
	
	                </div>
	                </div>
	            </c:if>
			</div>
			
			</div><!-- end row -->
				
			</c:forEach>
        <div class="form-group">
            <div class="col-sm-12 save-button">
                <button type="submit" name="submitButton" value="save" class="btn btn-default" <c:if test="${stage==null }">disabled="disabled"</c:if>>Save</button>
                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
            </div>
        </div>
    </form>
	
	<!-- New Stage Goal Modal -->
	<div class="modal fade" id="newStageGoalModal" tabindex="-1" role="dialog" aria-labelledby="newStageGoalModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
		    <form class="form-horizontal" action="/secure/treatment-components/EditStage" method="POST">
			    <input type="hidden" name="requestedAction" value="stage-edit-add-goal">
			    <input type="hidden" name="path" value="${path }" >
			    <input type="hidden" name="stageID" value="${stage.stageID}" >
			    <input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
			    <input type="hidden" name="clientUUID" value="${clientUUID }" >
		    
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="newStageGoalModalLabel">Enter a new stage goal.</h4>
		      </div>
		      <div class="modal-body">
		        <input type="text" class="form-control" id="newStageGoalDescription" name="newStageGoalDescription" value="" placeholder="Enter a new stage goal.">
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary">Save</button>
		      </div>
		    </form>  
	    </div>
	  </div>
	</div>
	<!-- End New Stage Goal Modal -->
	
	<!-- Delete Task Modal -->
	<c:forEach items="${stage.tasks }" var="task">
		<div class="modal" id="delete_task_modal${task.taskID }">
		  <div class="modal-dialog">
		    <div class="modal-content">
			    <form class="form-horizontal" action="/secure/treatment-components/EditStage" method="POST">
				    <input type="hidden" name="requestedAction" value="delete-task">
				    <input type="hidden" name="path" value="${path }" >
				    <input type="hidden" name="taskID" value="${task.taskID}">
				    <input type="hidden" name="stageID" value="${stage.stageID}" >
				    <input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
				    <input type="hidden" name="clientUUID" value="${clientUUID }" >
				    
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			          <h4 class="modal-title">Delete Task</h4>
			      </div>
			      <div class="modal-body">
			        <p>Are you sure you want to delete <strong>${task.title}</strong> from ${stage.title}? <span class="warning-message" >You cannot undo this.</span></p>
			        
			      </div>
			      <div class="modal-footer">
			      	<button type="submit" class="btn btn-default" title="Delete task (${task.title }) from this stage.">
					  OK
					</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			      </div>
		      
		      </form>
		    </div>
		  </div>
		</div>
	</c:forEach>
	<!-- End Delete Modal -->
	
	<!-- Delete Stage Goal Modal -->
	<c:forEach items="${stage.goals }" var="goal">
		<div class="modal" id="delete_goal_modal${goal.stageGoalID }">
		  <div class="modal-dialog">
		    <div class="modal-content">
		    	<form class="form-horizontal" action="/secure/treatment-components/EditStage" method="POST">
				    <input type="hidden" name="requestedAction" value="delete-goal">
				    <input type="hidden" name="path" value="${path }" >
				    <input type="hidden" name="stageID" value="${stage.stageID}" >
				    <input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
				    <input type="hidden" name="clientUUID" value="${clientUUID }" >
		    
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			          <h4 class="modal-title">Delete Stage Goal</h4>
			      </div>
			      <div class="modal-body">
			        <p>Are you sure you want to delete the goal: <strong>${goal.description}</strong> from ${stage.title}? <span class="warning-message" >You cannot undo this.</span></p>
			        
			      </div>
			      <div class="modal-footer">
			      	<button type="submit" class="btn btn-default btn-sm" title="Delete goal:${goal.description }">
					  OK
					</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			      </div>
			      
			      </form>
		    </div>
		  </div>
		</div>
	</c:forEach>
	<!-- End Delete Modal -->
	
	<script>
		$(function() {
		    $('#selectedCoreStageID').change(function() {
		    	this.form.submit();
		    });
		});
		
	</script>
<c:import url="/WEB-INF/jsp/footer.jsp" />