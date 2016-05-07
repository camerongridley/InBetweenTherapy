
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

	
<div class="page-header">
    <h2><span class="glyphicon glyphicon-dashboard panel-icon" aria-hidden="true"></span>Main Menu</h2>
  </div>
  <c:import url="/WEB-INF/jsp/message-modal.jsp"/>
  
  <div class="alert alert-info" role="alert">
	  <span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
	  <span class="sr-only"></span>
	  <strong>Daily Affirmation: </strong>The nature of emotions are to be impermanent.  Keep breathing and observe without judgment.
	</div>
  
  <div class="panel panel-primary">
	  <div class="panel-heading" title="Your Stats and Achievements">
	    <h3 class="panel-title">Your Stats and Achievements</h3>
	  </div>
	  <div class="panel-body">
	  		<div class="alert alert-success" role="alert">
			  <span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>
			  <span class="sr-only">Activity Streak:</span>
			  Current Activity Streak: 9  Nice! Keep it up!
			</div>
			
			<div class="alert alert-success" role="alert">
			  <span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>
			  <span class="sr-only">Login Streak:</span>
			  Current Login Streak: 13
			</div>
	  </div>
  </div>
  
  
  <c:set var="activePlan" value="${user.getActiveTreatmentPlan() }"></c:set>
  <c:if test="${activePlan != null }">
  <div class="panel panel-primary">
	  <div class="panel-heading" title="Continue Where You Left Off">
	    <h3 class="panel-title">Continue Working On Your Treatment Plan</h3>
	  </div>
	  <div class="panel-body">
	  		<div class="panel panel-default">
			  <div class="panel-heading" title="${activePlan.title}">
			    <h3 class="panel-title">${activePlan.title}</h3>
			  </div>
			  <div class="panel-body">
			    <div class="progress" title="This plan is ${activePlan.percentComplete()}% complete.">
				  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${activePlan.percentComplete()}" aria-valuemin="0" aria-valuemax="100" style="width: ${activePlan.percentComplete()}%;">
				    ${activePlan.percentComplete()}%
				  </div>
				</div>
				<p><strong>Current Stage: </strong>${activePlan.currentStage.title } (${activePlan.currentStage.percentComplete}%)</p>
				
				<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
					<input type="hidden" name="requestedAction" value="select-plan-load">
					<input type="hidden" name="path" value="${path }">
					<input type="hidden" name="initialize" value="no">
					<input type="hidden" name="clientUUID" value="${clientUUID}">
					<input type="hidden" name="treatmentPlanID" value="${activePlan.treatmentPlanID}">
					
					<button type="submit" class="btn btn-lg btn-default" aria-label="Left Align" title="Continue working on this this treatment plan.">
					  <span class="glyphicon glyphicon-play" aria-hidden="true"></span>
					</button>
				</form>
				
			  </div>
			</div>
	  </div>
  </div>
  
  	
  </c:if>
  
  <c:if test="${activePlan == null }">
  	<h3>You do not have any plans that are active.  Please visit your Manage Treatment Plans page to start a working on a plan.</h3>
  </c:if>
  
  
  
  <p>
    <form class="form-inline" action="/secure/ClientSelectPlan" method="POST">
      <div><button type="submit" class="btn btn-primary">Manage Your Treatment Plans</button></div>
      <input type="hidden" name="path" value="clientManagePlans">
      <input type="hidden" name="requestedAction" value="select-plan-start">
    </form>
  </p>

  <p>
    <form class="form-inline" action="" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Another Option</button></div>
      <input type="hidden" name="requestedAction" value="assign-treatment-plan-start">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>


	

<c:import url="/WEB-INF/jsp/footer.jsp" />

