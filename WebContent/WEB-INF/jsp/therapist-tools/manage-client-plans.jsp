
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

    <div class="page-header">
        <h1>Client: ${client.email}</h1>
        
    </div>
    
	<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	<form class="form-horizontal" action="/secure/ManageClients" method="POST">
		<input type="hidden" name="requestedAction" value="select-client">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="defaultTreatmentPlanID" value="${defaultTreatmentPlanID }">
		
        <div class="form-horizontal">
            <h3>Assigned Plans</h3>
            <div class="form-horizontal">
                <c:forEach items="${assignedClientPlans}" var="assignedPlan">
                <div class="col-md-4">
	                <div class="panel panel-primary">
					  <div class="panel-heading">
					    <h3 class="panel-title">${assignedPlan.title}</h3>
					  </div>
					  <div class="panel-body">
					    <div class="progress" title="This plan is ${assignedPlan.percentComplete()}% complete.">
						  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${assignedPlan.percentComplete()}" aria-valuemin="0" aria-valuemax="100" style="width: ${assignedPlan.percentComplete()}%;">
						    ${assignedPlan.percentComplete()}%
						  </div>
						</div>
						<p><strong>Current Stage: </strong>${assignedPlan.currentStage.title } (${assignedPlan.currentStage.percentComplete*100}%)</p>
						<a href="/secure/ManageClients?requestedAction=load-client-view-treatment-plan&path=${path}&treatmentPlanID=${assignedPlan.treatmentPlanID}" type="button" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
						  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
						</a>
						<a href="http://www.google.com" type="button" class="btn btn-default" aria-label="Left Align" title="Edit the Treatment Plan">
						  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
						</a>
						<a href="http://www.google.com" type="button" class="btn btn-default" aria-label="Left Align" title="Delete Treatment Plan from Client's Profile">
						  <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
						</a>
					  </div>
					</div>
				</div>	
                </c:forEach>
            </div>
        </div>

    </form>
    
    <form class="form-horizontal" action="/secure/AssignTreatmentPlan" method="POST">
    <div class="form-horizontal">
        <h3>Assign a New Plan</h3>
    </div>
      <div class="col-md-12"><button type="submit" class="btn btn-primary">Assign Plan to Client</button></div>
      <input type="hidden" name="requestedAction" value="assign-treatment-plan-start">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  	</p>
    
    
    <form class="form-horizontal" action="/secure/ManageClients" method="POST">
        <div class="form-horizontal">
	        <div class="col-md-12">
	        	<button type="submit" class="btn btn-default">Load Plan</button>
	        </div>
        </div>
	</form>

	
<c:import url="/WEB-INF/jsp/footer.jsp" />