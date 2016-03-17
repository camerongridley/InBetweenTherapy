
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
			<form class="form-horizontal" action="/secure/treatment-components/CreateStage" method="POST">
				<input type="hidden" name="requestedAction" value="stage-add-template">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
				
				<div>
					<h3>Add a Core Stage</h3>
				</div>
				
		        <div class="form-group">
		            <label for="coreStage" class="col-sm-2 control-label">Core Stages</label>
		            <div class="col-sm-10">
		                <select class="form-control" id="coreStageID" name="coreStageID">
		                    <option  value="">Select a core stage.</option>
		                    <c:forEach items="${coreStages}" var="coreStage">
		                        <option value="${coreStage.stageID}" <c:if test="${coreStage.stageID == coreStageID}">selected</c:if>>${coreStage.title}</option>
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
	            <div class="col-sm-offset-2 col-sm-10 save-button">
	                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save & Continue</button>
	                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
	            </div>
	        </div>
		    </form>
		</div>
		
		<h2>Or</h2>	
	</c:if>
	
	
	<div class="well well-sm">
		<form class="form-horizontal" action="/secure/treatment-components/CreateStage" method="POST">
			<input type="hidden" name="requestedAction" value="stage-create-new">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
			
			<div>
				<h3>Create a New Stage</h3>
			</div>
			
	        <div class="form-group">
	            <label for="stageTitle" class="col-sm-2 control-label">Stage Name</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="stageTitle" name="stageTitle" value='<c:out value="${stageTitle}"/>' placeholder="Enter a stage name here.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="stageDescription" name="stageDescription" value='<c:out value="${stageDescription}"/>' placeholder="Describe the stage.">
	            </div>
	        </div>
	        
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10 save-button">
	                <button type="submit" name="submitButton" value="save" class="btn btn-default">Save & Continue</button>
	                <button type="submit" name="submitButton"  value="cancel" class="btn btn-default">Cancel</button>
	            </div>
	        </div>

	    </form>
	</div>

<c:import url="/WEB-INF/jsp/footer.jsp" />