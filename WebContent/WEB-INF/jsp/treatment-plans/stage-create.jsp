
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<c:import url="/WEB-INF/jsp/header.jsp" />

    <div class="page-header">
        <h2>Create a Stage<c:if test="${path == 'templateStage' }"> Template</c:if></h2>
        <c:import url="/WEB-INF/jsp/includes/breadcrumbs.jsp" />
    </div>
    
	<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	<c:if test="${path != 'templateStage' }">
		<div class="well well-sm">
			<form class="form-horizontal" action="/secure/CreateStage" method="POST">
				<input type="hidden" name="requestedAction" value="stage-add-default-template">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
				
				<div>
					<h3>Add a Predefined Stage</h3>
				</div>
				
		        <div class="form-group">
		            <label for="defaultStage" class="col-sm-2 control-label">Default Stages</label>
		            <div class="col-sm-10">
		                <select class="form-control" id="defaultStageID" name="defaultStageID">
		                    <option  value="">Select a default stage.</option>
		                    <c:forEach items="${defaultStages}" var="defaultStage">
		                        <option value="${defaultStage.stageID}" <c:if test="${defaultStage.stageID == defaultStageID}">selected</c:if>>${defaultStage.title}</option>
		                    </c:forEach>
		                </select>
		            </div>
		        </div>
		        <div class="form-group">
		        <div class="col-sm-offset-2 col-sm-10">
		        <p>Preview of selected stage goes here.</p>
		        </div>
		        </div>
		
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                <button type="submit" class="btn btn-default">Save</button>
		            </div>
		        </div>
		    </form>
		</div>
		
		<h2>Or</h2>	
	</c:if>
	
	
	<div class="well well-sm">
		<form class="form-horizontal" action="/secure/CreateStage" method="POST">
			<input type="hidden" name="requestedAction" value="stage-create-title">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
			
			<div>
				<h3>Create a New Stage</h3>
			</div>
			
	        <div class="form-group">
	            <label for="stageTitle" class="col-sm-2 control-label">Stage Name</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="stageTitle" name="stageTitle" value="${fn:escapeXml(stageTitle)}" placeholder="Enter a stage name here.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="stageDescription" name="stageDescription" value="${fn:escapeXml(stageDescription)}" placeholder="Describe the stage.">
	            </div>
	        </div>
	        
	        <button type="submit" class="btn btn-default col-sm-offset-2">Save</button>

	    </form>
	</div>

<c:import url="/WEB-INF/jsp/footer.jsp" />