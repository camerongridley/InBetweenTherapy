
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

<div class="page-header">
	<h1>Heading</h1>
	<h2>Subheading</h2>
</div>
  
<c:import url="/jsp/message-modal.jsp"/>
	
	<form class="form-horizontal" action="./EditStageTemplate" method="POST">
		<input type=hidden name="chosenAction" value="edit-stage-select-stage">
		
		<div class="form-group">
			<label for="defaultTreatmentIssue" class="col-sm-2 control-label">Default Stages</label>
	        <div class="col-sm-5">
	            <select class="form-control" id="selectedDefaultStage" name="selectedDefaultStage">
	                <option  value="">Select a stage to edit.</option>
	                <c:forEach var="defaultStage" items="${defaultStageList }">
	                    <option value="${defaultStage.stageID}" <c:if test="${defaultStage.stageID == selectedDefaultStage.stageID }">selected</c:if> >${defaultStage.name}</option>
	                </c:forEach>
	            </select>
	             
	        </div>
	        <div class="col-sm-5">
	        	<button type="submit" class="btn btn-primary btn-md">Load</button>
	        </div>
		</div>
	</form>
	
	<hr>
	
	<form class="form-horizontal" action="./EditStageTemplate" method="POST">
		<input type=hidden name="chosenAction" value="edit-stage-name">	
		<input type="hidden" name="stageID" value="${selectedDefaultStage.stageID }" >	
        <div class="form-group">
            <label for="stageName" class="col-sm-2 control-label">Stage Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageName" name="stageName" value="${selectedDefaultStage.name }" placeholder="Enter a stage name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="stageDescription" class="col-sm-2 control-label">Stage Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="stageDescription" name="stageDescription" value="${selectedDefaultStage.description }" placeholder="Describe the stage.">
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
                <button type="submit" class="btn btn-default">Add Goals & Tasks >></button>
            </div>
        </div>
    </form>
	

<c:import url="/jsp/footer.jsp" />