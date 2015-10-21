
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="/jsp/header.jsp" />

<div class="page-header">
	<h1>Edit A Task</h1>
	<h2>Subheading</h2>
</div>
  
<c:import url="/jsp/message-modal.jsp"/>

<form class="form-horizontal" action="./EditTask" method="POST">
	<input type="hidden" name="requestedAction" value="edit-task-select-task">
	<input type="hidden" name="path" value="${path }">

	<c:if test="${path=='editingTaskTemplate'}">
		<div class="form-group">
			<label for="defaultTaskListID" class="col-sm-2 control-label">Select Task</label>
			<div class="col-sm-8">
				<select class="form-control" id="defaultTaskListID" name="taskID">
				    <option  value="">Select a default task.</option>
				    	<c:forEach items="${taskTemplateList}" var="taskTemplate">
							<option value="${taskTemplate.taskID}" <c:if test="${taskTemplate.taskID == task.taskID }">selected</c:if> >${fn:escapeXml(taskTemplate.title)}</option>
						</c:forEach>
				</select>
			</div>
		</div>	
	<hr>
	</c:if>
</form>

<form class="form-horizontal" action="./EditTask" method="POST">
	<input type="hidden" name="requestedAction" value="edit-task-select-task-type">
	<input type="hidden" name="path" value="${path }">
	<input type="hidden" name="taskID" value="${task.taskID }">
	
	<div class="form-group">
	    <label for="taskTypeID" class="col-sm-2 control-label">Task Type</label>
	    <div class="col-sm-10">
	        <select class="form-control" id="taskTypeID" name="taskTypeID">
	            <option  value="">Select a default treatment issue.</option>
	            <c:forEach items="${taskTypeMap}" var="taskType">
	                <option value="${taskType.key}" <c:if test="${taskType.key == task.taskTypeID}">selected</c:if> >${fn:escapeXml(taskType.value)}</option>
	            </c:forEach>
	        </select>
	    </div>
	</div>
</form>
	
<form class="form-horizontal" action="./EditTask" method="POST">
		<input type="hidden" name="requestedAction" value="edit-task-update">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="taskTypeID" value="${task.taskTypeID }">
		<input type="hidden" name="taskID" value="${task.taskID }">
		<input type="hidden" name="stageID" value="${task.stageID }">
		<input type="hidden" name="parentTaskID" value="${task.parentTaskID }">
		<input type="hidden" name="isTemplate" value="${task.template }">
		<input type="hidden" name="isExtraTask" value="${task.extraTask }">
		
		
				
        <div class="form-group">
            <label for="taskTitle" class="col-sm-2 control-label">Task Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="taskTitle" name="taskTitle" value="<c:out value="${fn:escapeXml(task.title) }"/>" placeholder="Enter a task name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="taskInstructions" class="col-sm-2 control-label">Instructions</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="taskInstructions" name="taskInstructions" value="<c:out value="${fn:escapeXml(task.instructions) }"/>" placeholder="Describe the stage.">
            </div>
        </div>
		<div class="form-group">
            <label for="resourceLink" class="col-sm-2 control-label">Resource Link</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="resourceLink" name="resourceLink" value="<c:out value="${fn:escapeXml(task.resourceLink) }"/>" placeholder="Add a link to related resources for this task.">
            </div>
        </div>
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
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Submit</button>
            </div>
        </div>
    </form>
	
	<script>
		$(function() {
		    $('#defaultTaskListID').change(function() {
		    	this.form.submit();
		    });
		    $('#taskTypeID').change(function() {
		    	//alert("Caution: Changing the task type will delete all non-generic data.")
		    	this.form.submit();
		    	
		    });
		});
	</script>

<c:import url="/jsp/footer.jsp" />