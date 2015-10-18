
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
		<input type="hidden" name="path" value="${path }">
		
		<c:if test="${path=='editingStageTemplate'}">
			<div class="form-group">
				<label for="selectedDefaultStageID" class="col-sm-2 control-label">Select Default Stage to Edit</label>
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
		
		
	</form>
	
	<hr>
	
	<form class="form-horizontal" action="./EditStage" method="POST">
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
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Save</button>
            </div>
        </div>
    </form>
	
	<script>
		$(function() {
		    $('#selectedDefaultStageID').change(function() {
		    	this.form.submit();
		    });
		});
	</script>
<c:import url="/jsp/footer.jsp" />