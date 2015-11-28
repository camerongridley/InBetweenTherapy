
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

<div class="page-header">
	<h1>Edit a Stage</h1>
</div>
  
<c:import url="/jsp/message-modal.jsp"/>
	
	<form class="form-horizontal" action="/secure/EditStage" method="POST">
		<input type="hidden" name="requestedAction" value="stage-edit-select-stage">
		<input type="hidden" name="path" value="${path }">
		
		<div class="well well-sm">
			<c:if test="${path=='editingStageTemplate'}">
				<div class="form-group">
					<label for="selectedDefaultStageID" class="col-sm-2 control-label">Select Default Stage</label>
			        <div class="col-sm-5">
			            <select class="form-control" id="selectedDefaultStageID" name="selectedDefaultStageID">
			                <option  value="">Select a stage to edit.</option>
			                <c:forEach var="defaultStage" items="${defaultStageList }">
			                    <option value="${defaultStage.stageID}" <c:if test="${defaultStage.stageID == stage.stageID }">selected</c:if> >${defaultStage.title}</option>
			                </c:forEach>
			            </select>
			        </div>
				</div>
				
			</c:if>
		</div>
		
	</form>
	
	<form class="form-horizontal" action="/secure/EditStage" method="POST">
		<input type="hidden" name="requestedAction" value="stage-edit-name">
		<input type="hidden" name="path" value="${path }">	
		<input type="hidden" name="stageID" value="${stage.stageID }" >	
		
        <div class="form-group">
            <label for="stageTitle" class="col-sm-2 control-label">Stage Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageTitle" name="stageTitle" value="${stage.title }" placeholder="Enter a stage name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageDescription" name="stageDescription" value="${stage.description }" placeholder="Describe the stage.">
            </div>
        </div>

		<div class="form-group">
			<div class="col-sm-12">
				<label for="stageGoalList" class="control-label">Stage Goals
					<button type="button" class="btn btn-default btn-xs" aria-label="Left Align" data-toggle="modal" data-target="#newStageGoalModal" title="Add a goal to this stage." <c:if test="${stage.stageID == null }">disabled</c:if>>
					
					  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
					
				</label>
			</div>
				
			<c:forEach items="${stage.goals }" var="goal">
	            <div class="col-xs-11">
	                <input type="text" class="form-control" id="stageGoalDescription${goal.stageGoalID}" name="stageGoalDescription${goal.stageGoalID}" value="${goal.description }" placeholder="Describe the goal.">
	            </div>
	            <div class="col-xs-1">    
	                <a role="button" href="/secure/EditStage?requestedAction=delete-goal&path=${path}&stageID=${stage.stageID}&stageGoalID=${goal.stageGoalID}" class="btn btn-default btn-xs pull-right" title="Delete goal:${goal.description }">
					  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
					</a>
	            
	            </div>
			</c:forEach>
				
		</div>
		
		<label for="stageList" class="control-label">Tasks

       			<a role="button" href="/secure/CreateTask?requestedAction=create-task-start&path=${path}&stageID=${stage.stageID}" class="btn btn-default btn-xs" title="Add a task to this stage." <c:if test="${stage.stageID == null }">disabled</c:if>>
				  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
				</a>

		</label>
			<c:forEach items="${stage.tasks }" var="task">
				<div class="panel panel-default panel-task" id="stageList" title="Click the task title to expand and see details.">
				  <div class="panel-heading">
				  	<input type="hidden" name="taskID" value="${task.taskID}"/>
				  	<input type="hidden" name="taskTitle" value="${task.title}"/>
					<a role="button" data-toggle="collapse" href="#collapse${task.taskID }" aria-expanded="true" aria-controls="collapse${task.taskID }">
			          ${task.taskOrderForUserDisplay } - <span class="">${task.title }</span>
			        </a>
			        <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
			        <a role="button" href="/secure/EditStage?requestedAction=delete-task&path=${path}&stageID=${stage.stageID}&taskID=${task.taskID}" class="btn btn-default btn-xs pull-right" title="Delete task:${task.title }">
					  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
					</a>
					
			        <a role="button" href="/secure/EditTask?requestedAction=edit-task-select-task&path=${path}&stageID=${stage.stageID}&taskID=${task.taskID}" class="btn btn-default btn-xs pull-right" title="Edit task: ${task.title }">
					  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
					</a>
					
				  </div>
				  <div id="collapse${task.taskID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.taskID}">
					  <div class="panel-body">
					    ${task.instructions } 
					  </div>
				  </div>
				</div>
			</c:forEach>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Save</button>
            </div>
        </div>
    </form>
	
	<!-- New Stage Goal Modal -->
	<div class="modal fade" id="newStageGoalModal" tabindex="-1" role="dialog" aria-labelledby="newStageGoalModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
		    <form class="form-horizontal" action="/secure/EditStage" method="POST">
		    <input type="hidden" name="requestedAction" value="stage-edit-add-goal">
		    <input type="hidden" name="path" value="${path }" >
		    <input type="hidden" name="stageID" value="${stage.stageID}" >
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
	
	<script>
		$(function() {
		    $('#selectedDefaultStageID').change(function() {
		    	this.form.submit();
		    });
		});
	</script>
<c:import url="/jsp/footer.jsp" />