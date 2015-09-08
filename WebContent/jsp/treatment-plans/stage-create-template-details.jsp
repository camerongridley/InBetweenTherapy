
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

<div class="page-header">
    <h1>Create Stage Template</h1>
    <h2>Add goals and tasks to the stage</h2>
</div>

<c:import url="/jsp/message-modal.jsp"/>

<form class="form-horizontal" action="./CreateStageTemplate" method="POST">
	<input type="hidden" name="chosenAction" value="stageGoalsTasks">
	<input type="hidden" name="stageID" value="stage.stageID">
	<h2>Define goals for the stage: ${stage.name }</h2>

	<div class="well well-sm">
	    <div class="form-group">
	        <div class="col-sm-12">
	            <input type="text" class="form-control" id="newStageGoal" name="newStageGoal" value="${newStageGoal }" placeholder="Enter goal description here.">
	        	<button type="submit" name="addStageGoal" class="btn btn-default btn-xs" title="Click to save a new goal."><span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span></button>
	        </div>
	    </div>
	    
		<!-- List existing goals -->
	    <div class="form-group">
	        <div class="col-sm-10">
	            <input type="text" class="form-control disabled" id="stageGoal" name="stageGoal" value="${stageGoal }" >
	        </div>
	    </div>
    </div>   

      <!--
      	<div class="form-group col-md-12">
              <h4>Select the tasks for the stage</h4>
       </div>
           TODO change this to list all the tasks associated with this stage
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
	</form>
       
   <form class="form-horizontal" action="./CreateStageTemplate" method="POST">
	<input type="hidden" name="chosenAction" value="stageGoalsTasks">
	<input type="hidden" name="addingGoal" value="false">
	<input type="hidden" name="stageID" value="stage.stageID">  
       <div class="form-group">
           <div class="col-sm-offset-2 col-sm-10">
               <button type="submit" class="btn btn-default">Continue</button>
           </div>
       </div>
   </form>
	
<c:import url="/jsp/footer.jsp" />