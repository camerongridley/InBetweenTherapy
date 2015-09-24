
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

<div class="page-header">
	<h1>Edit a Stage</h1>
	<h2>Name and Description</h2>
</div>
  
<c:import url="/jsp/message-modal.jsp"/>
	
	<form class="form-horizontal" action="./EditStage" method="POST">
		<input type="hidden" name="requestedAction" value="stage-edit-select-stage">
		
		<div class="form-group">
			<label for="defaultTreatmentIssue" class="col-sm-2 control-label">Select Default Stage to Edit</label>
	        <div class="col-sm-5">
	            <select class="form-control" id="selectedDefaultStageID" name="selectedDefaultStageID">
	                <option  value="">Select a stage to edit.</option>
	                <c:forEach var="defaultStage" items="${defaultStageList }">
	                    <option value="${defaultStage.stageID}" <c:if test="${defaultStage.stageID == selectedDefaultStage.stageID }">selected</c:if> >${defaultStage.title}</option>
	                </c:forEach>
	            </select>
	             
	        </div>
	        <div class="col-sm-5">
	        	<button type="submit" class="btn btn-primary btn-md">Load</button>
	        </div>
		</div>
	</form>
	
	<hr>
	
	<form class="form-horizontal" action="./EditStage" method="POST">
		<input type="hidden" name="requestedAction" value="stage-edit-name">	
		<input type="hidden" name="stageID" value="${selectedDefaultStage.stageID }" >	
        <div class="form-group">
            <label for="stageTitle" class="col-sm-2 control-label">Stage Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageTitle" name="stageTitle" value="${selectedDefaultStage.title }" placeholder="Enter a stage name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageDescription" name="stageDescription" value="${selectedDefaultStage.description }" placeholder="Describe the stage.">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Add Goals >></button>
            </div>
        </div>
    </form>
	

<c:import url="/jsp/footer.jsp" />