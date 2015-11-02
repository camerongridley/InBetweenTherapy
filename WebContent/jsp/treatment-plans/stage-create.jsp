
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

    <div class="page-header">
        <h1>Manage Stages</h1>
        <h2>Treatment Plan: ${treatmentPlan.title} (${treatmentPlan.treatmentPlanID })</h2>
    </div>
    
	<c:import url="/jsp/message-modal.jsp"/>

	<c:if test="${path != 'creatingStageTemplate' }">
		<div class="well well-sm">
			<form class="form-horizontal" action="./CreateStage" method="POST">
				<input type="hidden" name="requestedAction" value="stage-add-default">
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
		<form class="form-horizontal" action="./CreateStage" method="POST">
			<input type="hidden" name="requestedAction" value="stage-create-title">
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID }">
			
			<div>
				<h3>Create a New Stage</h3>
			</div>
			
	        <div class="form-group">
	            <label for="stageTitle" class="col-sm-2 control-label">Stage Name</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="stageTitle" name="stageTitle" value="${stageTitle }" placeholder="Enter a stage name here.">
	            </div>
	        </div>
	        <div class="form-group">
	            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
	            <div class="col-sm-10">
	                <input type="text" class="form-control" id="stageDescription" name="stageDescription" value="${stageDescription }" placeholder="Describe the stage.">
	            </div>
	        </div>
	        
	        
	       <!--  <div class="form-group col-md-12">
	                <h4>Define the goals for the stage</h4>
	        </div>
	
	       
	       	<div class="form-group col-md-12">
	               <h4>Select the tasks for the stage</h4>
	        </div> -->
	           <!-- TODO change this to list all the tasks associated with this stage
	           <div class="form-group">
	               <label for="existingCustomTreatmentIssue" class="col-sm-2 control-label">Existing Custom Tx Issues</label>
	               <div class="col-sm-10">
	                   <select class="form-control" id="existingCustomTreatmentIssue" name="existingCustomTreatmentIssue">
	                       <option value="">Or select an issue you've previously created.</option>
	                       <c:forEach items="${customTreatmentIssues}" var="customIssue">
	                           <option value="${customIssue.treatmentIssueID}">${customIssue.treatmentIssueName}</option>
	                       </c:forEach>
	                   </select>
	               </div>
	           </div>
	            -->
	
	        
	        
	        <div class="form-group">
	            <div class="col-sm-offset-2 col-sm-10">
	                <button type="submit" class="btn btn-default">Save</button>
	            </div>
	        </div>
	    </form>
	</div>

<c:import url="/jsp/footer.jsp" />