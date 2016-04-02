
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h2>Edit Task <c:if test="${task.template}">Template</c:if></h2>
	<c:import url="/WEB-INF/jsp/includes/breadcrumbs.jsp" />
</div>
  
<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

<form class="form-horizontal" action="/secure/treatment-components/EditTask" method="POST">
	<input type="hidden" name="requestedAction" value="edit-task-select-task">
	<input type="hidden" name="path" value="${path }">
	<input type="hidden" name="stageID" value="${stage.stageID }" >
	<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">

	<c:if test='${path=="templateTask"}'>
		<div class="form-group">
			<label for="defaultTaskListID" class="col-sm-2 control-label">Select Task</label>
			<div class="col-sm-8">
			<a id="taskSelection"></a>
				<select class="form-control" id="defaultTaskListID" name="taskID">
				    <option  value="">Select a default task.</option>
				    	<c:forEach items="${coreTasks}" var="coreTask">
							<option value="${coreTask.taskID}" <c:if test="${coreTask.taskID == task.taskID }">selected</c:if> ><c:out value="${coreTask.title}"/></option>
						</c:forEach>
				</select>
			</div>
		</div>	
	<hr>
	</c:if>
</form>

<form class="form-horizontal" action="/secure/treatment-components/EditTask" method="POST">
	<input type="hidden" name="requestedAction" value="edit-task-select-task-type">
	<input type="hidden" name="path" value="${path }">
	<input type="hidden" name="taskID" value="${task.taskID }">
	<input type="hidden" name="stageID" value="${stage.stageID }" >
	<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
	
	<div class="form-group">
	    <label for="taskTypeID" class="col-sm-2 control-label">Task Type</label>
	    <div class="col-sm-10">
	        <select class="form-control" id="taskTypeID" name="taskTypeID">
	            <option  value="">Select a task type.</option>
	            <c:forEach items="${taskTypeMap}" var="taskType">
	                <option value="${taskType.key}" <c:if test="${taskType.key == task.taskTypeID}">selected</c:if> ><c:out value="${taskType.value}" /></option>
	            </c:forEach>
	        </select>
	    </div>
	</div>
</form>
	
<form class="form-horizontal" action="/secure/treatment-components/EditTask" method="POST">
		<input type="hidden" name="requestedAction" value="edit-task-update">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="taskTypeID" value="${task.taskTypeID }">
		<input type="hidden" name="taskID" value="${task.taskID }">
		<input type="hidden" name="stageID" value="${stage.stageID }">
		<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
		<input type="hidden" name="parentTaskID" value="${task.parentTaskID }">
		<input type="hidden" name="isTemplate" value="${task.template }">
		<input type="hidden" name="isExtraTask" value="${task.extraTask }">
		<input type="hidden" name="clientTaskOrder" value="${task.clientTaskOrder }">
		
				
        <div class="form-group">
            <label for="taskTitle" class="col-sm-2 control-label">Task Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="taskTitle" name="taskTitle" value="<c:out value="${task.title }"/>" placeholder="Enter a task name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="taskInstructions" class="col-sm-2 control-label">Instructions</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="taskInstructions" name="taskInstructions" value="<c:out value="${task.instructions }"/>" placeholder="Provide instructions for the task.">
            </div>
        </div>
		<div class="form-group">
            <label for="resourceLink" class="col-sm-2 control-label">Resource Link</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="resourceLink" name="resourceLink" value="<c:out value="${task.resourceLink }"/>" placeholder="Add a link to related resources for this task.">
            </div>
        </div>
        <c:if test="${task.template }">
        	<div class="form-group">
	            
	        </div>
        </c:if>
        <c:if test="${task.taskTypeID==2 }">
			<div class="form-group">
	            <label for="extraTextLabel1" class="col-sm-2 control-label">Extra TextBox 1 Label</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="extraTextLabel1" name="extraTextLabel1" value="<c:out value="${task.extraTextLabel1 }"/>" placeholder="Enter a name for the label of the first extra textbox.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="extraTextValue1" class="col-sm-2 control-label">Extra TextBox 1 Value</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="extraTextValue1" name="extraTextValue1" value="<c:out value="${task.extraTextValue1 }"/>" placeholder="Enter a value for the first extra textbox. Leave empty if this for a client entry.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="extraTextLabel2" class="col-sm-2 control-label">Extra TextBox 2 Label</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="extraTextLabel2" name="extraTextLabel2" value="<c:out value="${task.extraTextLabel2 }"/>" placeholder="Enter a name for the label of the first extra textbox.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="extraTextValue2" class="col-sm-2 control-label">Extra TextBox 2 Value</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="extraTextValue2" name="extraTextValue2" value="<c:out value="${task.extraTextValue2 }"/>" placeholder="Enter a value for the second extra textbox. Leave empty if this for a client entry.">
	            </div>
	        </div>
		</c:if>
		
		<div class="form-group">
            <label for="taskTitle" class="col-sm-2 control-label">Keywords</label>
            <div class="col-sm-10">
            	
                <c:forEach var="keyword" items="${coreTaskKeyords}">
                	<c:set var="keywordValue" value="${keyword.value}"></c:set>

				<!-- use label-default for core keywords and label-primary for custom keywords -->
				<span class="keyword label-default">
				      <input type="checkbox" value="${keywordValue.keywordID}" <c:if test="${task.hasKeyword(keyword.key) }"> checked</c:if>> ${keywordValue.keyword}
				</span>
				
				

				</c:forEach>
				
            </div>
        </div>
		<div class="form-group">

            <div class="col-sm-offset-2 col-sm-10 save-button">
                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save</button>
                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
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

<c:import url="/WEB-INF/jsp/footer.jsp" />