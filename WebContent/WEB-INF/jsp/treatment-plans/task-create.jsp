
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h2>Create A Task<c:if test='${path=="templateTask" }'> Template</c:if></h2>
	<c:import url="/WEB-INF/jsp/includes/breadcrumbs.jsp" />
</div>
  
<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
	<c:if test='${path!="templateTask" }'>
		<div class="well well-sm">
			<form class="form-horizontal" action="/secure/CreateTask" method="POST">
				<input type="hidden" name="requestedAction" value="task-add-template">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="stageID" value="${stage.stageID }">
				<input type="hidden" name="isTemplate" value="${task.template }">
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
				
				<div>
					<h4>Add a Core Task</h4>
				</div>
				
				<div class="form-horizontal">
			        <div class="form-group">
			            <label for="defaultTaskList" class="col-sm-2 control-label">Core Task</label>
			            <div class="col-sm-10">
			                <select class="form-control" id="defaultTaskList" name="taskID">
			                    <option  value="">Select a core task.</option>
			                    <c:forEach items="${coreTasks}" var="defaultTask">
			                        <option value="${defaultTask.taskID}">${fn:escapeXml(defaultTask.title)}</option>
			                    </c:forEach>
			                </select>
			            </div>
			        </div>
			        
			        <div class="form-group">
				        <div class="col-sm-offset-2 col-sm-10">
				        	<p>Preview of selected task goes here.</p>
				        </div>
			        </div>
					
				<c:if test='${path.equals("manageClients")}'>
					<div class="form-group">
				        <div class="col-sm-offset-2 col-sm-10">
					  Repetitions: <select class="task-repetition-dropdown" title="Number of repetitions." id="clientRepetitions" name="clientRepetitions">
	                    <option  value="1" >1</option>
						<option  value="2" >2</option>
						<option  value="3" >3</option>
						<option  value="4" >4</option>
						<option  value="5" >5</option>
						<option  value="6" >6</option>
						<option  value="7" >7</option>
						<option  value="8" >8</option>
						<option  value="9" >9</option>
						<option  value="10" >10</option>
	                </select>
	
	                </div>
	                </div>
	            </c:if>

					
			        <div class="form-group">
			            <div class="col-sm-offset-2 col-sm-10 save-button">
			                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save & Continue</button>
			                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
			            </div>
			        </div>
			    </div>
		    </form>
		</div>
		
		<h3>Or</h3>
	</c:if>
	
	
	<div class="well well-sm">
		<form class="form-horizontal" action="/secure/CreateTask" method="POST">
			<input type="hidden" name="requestedAction" value="task-type-select">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="stageID" value="${stage.stageID }">
			<input type="hidden" name="isTemplate" value="${task.template }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
				
			<div>
				<h4>Create a New Task</h4>
			</div>
				
			<div class="form-group">
	            <label for="taskTypeID" class="col-sm-2 control-label">Task Type</label>
	            <div class="col-sm-10">
	            <a name="taskTypeSelection" id="taskTypeSelection"></a>
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
			<input type="hidden" name="requestedAction" value="task-create-new">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="stageID" value="${stage.stageID }">
			<input type="hidden" name="taskTypeID" value="${task.taskTypeID }">
			<input type="hidden" name="parentTaskID" value="${task.parentTaskID }">
			<input type="hidden" name="isTemplate" value="${task.template }">
			<input type="hidden" name="isExtraTask" value="${task.extraTask }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
			    
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
			
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10 save-button">
	                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save & Continue</button>
	                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
	            </div>
	        </div>

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