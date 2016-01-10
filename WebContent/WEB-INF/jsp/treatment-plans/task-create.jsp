
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<c:if test="${path=='taskTemplate' }"><h1>Create A Task Template</h1></c:if>
	<c:if test="${path!='taskTemplate' }"><h1>Create a Task for the Stage: ${stage.title}</h1></c:if>
</div>
  
<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
	<c:if test="${path!='taskTemplate' }">
		<div class="well well-sm">
			<form class="form-horizontal" action="/secure/CreateTask" method="POST">
				<input type="hidden" name="requestedAction" value="task-add-default-template">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="stageID" value="${stage.stageID }">
				<input type="hidden" name="isTemplate" value="${task.template }">
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlanID }">
				
				<div>
					<h3>Add a Predefined Task</h3>
				</div>
				
				<div class="form-horizontal">
			        <div class="form-group">
			            <label for="defaultStage" class="col-sm-2 control-label">Default Task</label>
			            <div class="col-sm-10">
			                <select class="form-control" id="defaultTaskList" name="taskID">
			                    <option  value="">Select a default task.</option>
			                    <c:forEach items="${defaultTasks}" var="defaultTask">
			                        <option value="${defaultTask.taskID}">${fn:escapeXml(defaultTask.title)}</option>
			                    </c:forEach>
			                </select>
			            </div>
			        </div>
			        <div class="form-group">
				        <div class="col-sm-offset-2 col-sm-2">
				        	<div class="panel panel-primary panel-task" id="taskReps" title="Number of repetitions.">
								<div class="panel-heading">
								  Repetitions: <select class="task-repetition-dropdown" title="Number of repetitions." id="taskReps" name="taskReps">
				                    <option  value="1" <c:if test="${mappedStageTaskInfo.templateRepetitions==1 }">selected</c:if>>1</option>
									<option  value="2" <c:if test="${mappedStageTaskInfo.templateRepetitions==2 }">selected</c:if>>2</option>
									<option  value="3" <c:if test="${mappedStageTaskInfo.templateRepetitions==3}">selected</c:if>>3</option>
									<option  value="4" <c:if test="${mappedStageTaskInfo.templateRepetitions==4 }">selected</c:if>>4</option>
									<option  value="5" <c:if test="${mappedStageTaskInfo.templateRepetitions==5 }">selected</c:if>>5</option>
									<option  value="6" <c:if test="${mappedStageTaskInfo.templateRepetitions==6 }">selected</c:if>>6</option>
									<option  value="7" <c:if test="${mappedStageTaskInfo.templateRepetitions==7 }">selected</c:if>>7</option>
									<option  value="8" <c:if test="${mappedStageTaskInfo.templateRepetitions==8 }">selected</c:if>>8</option>
									<option  value="9" <c:if test="${mappedStageTaskInfo.templateRepetitions==9 }">selected</c:if>>9</option>
									<option  value="10" <c:if test="${mappedStageTaskInfo.templateRepetitions==10 }">selected</c:if>>10</option>
				                </select>
				
				                </div>
				            </div>
				        </div>
			        </div>
			        
			        <div class="form-group">
				        <div class="col-sm-offset-2 col-sm-10">
				        	<p>Preview of selected task goes here.</p>
				        </div>
			        </div>
			
			        <div class="form-group">
			            <div class="col-sm-offset-2 col-sm-10">
			                <button type="submit" class="btn btn-default">Save</button>
			            </div>
			        </div>
			    </div>
		    </form>
		</div>
		
		<h2>Or</h2>
	</c:if>
	
	
	<div class="well well-sm">
		<form class="form-horizontal" action="/secure/CreateTask" method="POST">
			<input type="hidden" name="requestedAction" value="task-type-select">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="stageID" value="${stage.stageID }">
			<input type="hidden" name="isTemplate" value="${task.template }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlanID }">
				
			<div class="form-group">
	            <label for="taskTypeID" class="col-sm-2 control-label">Task Type</label>
	            <div class="col-sm-10">
	                <select class="form-control" id="taskTypeID" name="taskTypeID">
	                    <option  value="">Select a task type.</option>
	                    <c:forEach items="${taskTypeMap}" var="taskType">
	                        <option value="${taskType.key}" <c:if test="${taskType.key == task.taskTypeID}">selected</c:if> >${fn:escapeXml(taskType.value)}</option>
	                    </c:forEach>
	                </select>
	            </div>
	        </div>	
	    </form>
	        
	    <form class="form-horizontal" action="/secure/CreateTask" method="POST">
			<input type="hidden" name="requestedAction" value="create-new-task">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="stageID" value="${stage.stageID }">
			<input type="hidden" name="taskTypeID" value="${task.taskTypeID }">
			<input type="hidden" name="parentTaskID" value="${task.parentTaskID }">
			<input type="hidden" name="isTemplate" value="${task.template }">
			<input type="hidden" name="isExtraTask" value="${task.extraTask }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlanID }">
			    
			<c:if test="${task.taskTypeID!=0 }">	
	        <div class="form-group">
	            <label for="taskTitle" class="col-sm-2 control-label">Task Name</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="taskTitle" name="taskTitle" value="<c:out value="${fn:escapeXml(task.title) }"/>" placeholder="Enter a task name here.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="taskInstructions" class="col-sm-2 control-label">Instructions</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="taskInstructions" name="taskInstructions" value="<c:out value="${fn:escapeXml(task.instructions) }"/>" placeholder="Describe the task">
	            </div>
	        </div>
			<div class="form-group">
	            <label for="resourceLink" class="col-sm-2 control-label">Resource Link</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="resourceLink" name="resourceLink" value="<c:out value="${fn:escapeXml(task.resourceLink) }"/>" placeholder="Add a link to related resources for this task.">
	            </div>
	        </div>
	        
			<div class="form-group">
	            <label for="repetitions" class="col-sm-2 control-label">Repetitions</label>
	            <div class="col-sm-2">
	            	<select class="form-control" id="repetitions" name="repetitions">
	                    <option  value="1">1</option>
						<option  value="2">2</option>
						<option  value="3">3</option>
						<option  value="4">4</option>
						<option  value="5">5</option>
						<option  value="6">6</option>
						<option  value="7">7</option>
						<option  value="8">8</option>
						<option  value="9">9</option>
						<option  value="10">10</option>
	                </select>
	              
	            </div>
	        </div>
	        </c:if>
	        
			<c:if test="${task.taskTypeID==2 }">
				<div class="form-group">
		            <label for="extraTextLabel1" class="col-sm-2 control-label">Extra TextBox 1 Label</label>
		            <div class="col-sm-10">
		                <input type="text" class="form-control" id="extraTextLabel1" name="extraTextLabel1" value="<c:out value="${fn:escapeXml(task.extraTextLabel1) }"/>" placeholder="Enter a name for the label of the first extra textbox.">
		            </div>
		        </div>
		        <div class="form-group">
		            <label for="extraTextValue1" class="col-sm-2 control-label">Extra TextBox 1 Value</label>
		            <div class="col-sm-10">
		                <input type="text" class="form-control" id="extraTextValue1" name="extraTextValue1" value="<c:out value="${fn:escapeXml(task.extraTextValue1) }"/>" placeholder="Enter a value for the first extra textbox. Leave empty if this for a client entry.">
		            </div>
		        </div>
		        <div class="form-group">
		            <label for="extraTextLabel2" class="col-sm-2 control-label">Extra TextBox 2 Label</label>
		            <div class="col-sm-10">
		                <input type="text" class="form-control" id="extraTextLabel2" name="extraTextLabel2" value="<c:out value="${fn:escapeXml(task.extraTextLabel2) }"/>" placeholder="Enter a name for the label of the first extra textbox.">
		            </div>
		        </div>
		        <div class="form-group">
		            <label for="extraTextValue2" class="col-sm-2 control-label">Extra TextBox 2 Value</label>
		            <div class="col-sm-10">
		                <input type="text" class="form-control" id="extraTextValue2" name="extraTextValue2" value="<c:out value="${fn:escapeXml(task.extraTextValue2) }"/>" placeholder="Enter a value for the second extra textbox. Leave empty if this for a client entry.">
		            </div>
		        </div>
			</c:if>
			
			<c:if test="${path='clientTask' }">
				<div class="form-group">
					<div class="checkbox col-sm-offset-2">
					  <label>
					    <input type="checkbox" value="checked" name="copyAsTemplate" id="copyAsTemplate">
					    Make a copy for use as a default Task?
					  </label>
					</div>
		        </div>
			</c:if>
	        <button type="submit" class="btn btn-default col-sm-offset-2">Submit</button>

	    </form>
    </div>
	
	<script>
		$(function() {
		    $('#taskTypeID').change(function() {
		    	this.form.submit();
		    });
		});
	</script>

<c:import url="/WEB-INF/jsp/footer.jsp" />