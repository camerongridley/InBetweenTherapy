
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h1>Stage Complete!</h1>
	<h2>Congratulations, you have finished the stage: ${stage.title}</h2>
</div>
  
<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	

<form action="/secure/ChangeStage" method="post" class="form-inline">
	<input type="hidden" name="requestedAction" value="stage-complete-continue">
	<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" />
	<input type="hidden" name="clientID" value=${client.userID } >
	<input type="hidden" name="path" value="${path }">
	
	<input type="hidden" name="stageIndex" value="${treatmentPlan.activeViewStageIndex }">
	<div class="form-group">
	    <div class="col-sm-12 save-button">
	        <button type="submit" name="submitButton" value="save" class="btn btn-default">Continue</button>
	        
	    </div>
	</div>
	
</form>
<form action="/secure/ChangeStage" method="post" class="form-inline">
	<input type="hidden" name="requestedAction" value="stage-complete-back">
	<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" />
	<input type="hidden" name="clientID" value=${client.userID } >
	<input type="hidden" name="path" value="${path }">
	
	<input type="hidden" name="stageIndex" value="${treatmentPlan.activeViewStageIndex -1}">
	
	<div class="form-group">
	    <div class="col-sm-12 save-button">
	        <button type="submit" name="submitButton"  value="back" class="btn btn-default">Go Back</button>
	    </div>
	</div>
	
</form>

<c:import url="/WEB-INF/jsp/footer.jsp" />