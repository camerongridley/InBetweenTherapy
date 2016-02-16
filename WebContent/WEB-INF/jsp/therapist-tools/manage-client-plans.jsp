
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
	        <div class="panel panel-default">
	            <div class="panel-heading"><h4>Active Plans</h4></div>
	            <div class="panel-body">
		            <div class="form-horizontal hide-overflow">
		                <c:forEach items="${activeAssignedClientPlans}" var="activePlan">
		                <div class="col-sm-6 col-md-4 col-lg-3">
			                <div class="panel panel-primary">
							  <div class="panel-heading" title="${activePlan.title}">
							    <h3 class="panel-title">${activePlan.title}</h3>
							  </div>
							  <div class="panel-body">
							    <div class="progress" title="This plan is ${activePlan.percentComplete()}% complete.">
								  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${activePlan.percentComplete()}" aria-valuemin="0" aria-valuemax="100" style="width: ${activePlan.percentComplete()}%;">
								    ${activePlan.percentComplete()}%
								  </div>
								</div>
								<p><strong>Current Stage: </strong></p>
								<p>${activePlan.currentStage.title } (${activePlan.currentStage.percentComplete*100}%)</p>
								<a href="/secure/ManageClients?requestedAction=load-client-view-treatment-plan&path=${path}&treatmentPlanID=${activePlan.treatmentPlanID}" type="button" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
								  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
								</a>
								<a href="/secure/EditTreatmentPlan?requestedAction=plan-edit-load-plan&path=${path}&treatmentPlanID=${activePlan.treatmentPlanID}&clientUserID=${client.userID}" type="button" class="btn btn-default" aria-label="Left Align" title="Edit the Treatment Plan">
								  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
								</a>
								<button type="button" class="btn btn-default glyphicon glyphicon-remove" data-toggle="modal" data-target="#delete_plan_modal${activePlan.treatmentPlanID}" aria-hidden="true" title="Delete this treatment plan."></button>
							  </div>
							</div>
						</div>	
		                </c:forEach>
		            </div>
	            </div>
	        </div>    
        </div>

    </form>
    
    <form class="form-horizontal" action="/secure/ManageClients" method="POST">
		<input type="hidden" name="requestedAction" value="select-client">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="defaultTreatmentPlanID" value="${defaultTreatmentPlanID }">
		
        <div class="form-horizontal">
	        <div class="panel panel-default">
	            <div class="panel-heading"><h4>Unstarted Plans</h4></div>
	            <div class="panel-body">
		            <div class="form-horizontal">
		                <c:forEach items="${unstartedAssignedClientPlans}" var="unstartedPlan">
		                <div class="col-sm-6 col-md-4 col-lg-3">
			                <div class="panel panel-primary">
							  <div class="panel-heading">
							    <h3 class="panel-title">${unstartedPlan.title}</h3>
							  </div>
							  <div class="panel-body">
								<a href="/secure/ManageClients?requestedAction=load-client-view-treatment-plan&path=${path}&treatmentPlanID=${unstartedPlan.treatmentPlanID}" type="button" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
								  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
								</a>
								<a href="/secure/EditTreatmentPlan?requestedAction=plan-edit-load-plan&path=${path}&treatmentPlanID=${unstartedPlan.treatmentPlanID}&clientUserID=${client.userID}" type="button" class="btn btn-default" aria-label="Left Align" title="Edit the Treatment Plan">
								  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
								</a>
								<button type="button" class="btn btn-default glyphicon glyphicon-remove" data-toggle="modal" data-target="#delete_plan_modal${unstartedPlan.treatmentPlanID}" aria-hidden="true" title="Delete this treatment plan."></button>
							  </div>
							</div>
						</div>	
		                </c:forEach>
		            </div>
	            </div>
	        </div>    
        </div>

    </form>
    
    <div class="panel panel-default">
	    <div class="panel-heading"><h4>Assign a New Plan</h4></div>
	    <div class="panel-body">
	    
			<form class="form-horizontal" action="/secure/ManageClients" method="POST">
				<input type="hidden" name="requestedAction" value="select-treatment-plan-for-assignment">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="clientUserID" value="${client.userID }">
				
				<div class="form-group">
					<label for="selectedDefaultTreatmentPlanID" class="col-sm-2 control-label">Select Default Treatment Plan</label>
			        <div class="col-sm-5">
			            <select class="form-control" id="defaultTreatmentPlanID" name="defaultTreatmentPlanID">
			                <option  value="">Select a treatment plan to assign.</option>
			                <c:forEach var="defaultPlan" items="${defaultTreatmentPlanList }">
			                    <option value="${defaultPlan.treatmentPlanID}" <c:if test="${defaultPlan.treatmentPlanID == defaultTreatmentPlanID }">selected</c:if> >${defaultPlan.title}</option>
			                </c:forEach>
			            </select>
			        </div>
				</div>
				<div class="form-group">
			        <div class="col-sm-offset-2 col-sm-10">
			        <p>Preview of selected default treatment plans.</p>
			        </div>
		        </div>
		     </form>
		     
		     <form class="form-horizontal" action="/secure/ManageClients" method="POST">
				<input type="hidden" name="requestedAction" value="copy-plan-to-client">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="clientUserID" value="${client.userID }">
				<input type="hidden" name="defaultTreatmentPlanID" value="${defaultTreatmentPlanID }">
				
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                <button type="submit" class="btn btn-default">Save</button>
		            </div>
		        </div>
		    </form>
	    
	    </div>
	</div>
	
	<form class="form-horizontal" action="/secure/ManageClients" method="POST">
		<input type="hidden" name="requestedAction" value="select-client">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="defaultTreatmentPlanID" value="${defaultTreatmentPlanID }">
		
        <div class="form-horizontal">
	        <div class="panel panel-default">
	            <div class="panel-heading"><h4>Completed Plans</h4></div>
	            <div class="panel-body">
		            <div class="form-horizontal">
		                <c:forEach items="${completedAssignedClientPlans}" var="completedPlan">
		                <div class="col-sm-6 col-md-4 col-lg-3">
			                <div class="panel panel-primary">
							  <div class="panel-heading">
							    <h3 class="panel-title">${completedPlan.title}</h3>
							  </div>
							  <div class="panel-body">
							    <div class="progress" title="This plan is ${completedPlan.percentComplete()}% complete.">
								  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${completedPlan.percentComplete()}" aria-valuemin="0" aria-valuemax="100" style="width: ${completedPlan.percentComplete()}%;">
								    ${completedPlan.percentComplete()}%
								  </div>
								</div>
								<a href="/secure/ManageClients?requestedAction=load-client-view-treatment-plan&path=${path}&treatmentPlanID=${completedPlan.treatmentPlanID}" type="button" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
								  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
								</a>
								<a href="/secure/EditTreatmentPlan?requestedAction=plan-edit-load-plan&path=${path}&treatmentPlanID=${completedPlan.treatmentPlanID}&clientUserID=${client.userID}" type="button" class="btn btn-default" aria-label="Left Align" title="Edit the Treatment Plan">
								  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
								</a>
								<button type="button" class="btn btn-default glyphicon glyphicon-remove" data-toggle="modal" data-target="#delete_plan_modal${completedPlan.treatmentPlanID}" aria-hidden="true" title="Delete this treatment plan."></button>
							  </div>
							</div>
						</div>	
		                </c:forEach>
		            </div>
	            </div>
	        </div>    
        </div>

    </form>
    
<!-- Delete TreatmentPlan Modal -->
<c:forEach items="${allAssignedClientPlans}" var="treatmentPlan">
	<div class="modal" id="delete_plan_modal${treatmentPlan.treatmentPlanID}">
		<form class="form-horizontal" action="/secure/ManageClients" method="POST">
			<input type="hidden" name="requestedAction" value="delete-plan"> 
			<input type="hidden" name="path" value="${path }">
			<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
			<input type="hidden" name="clientUserID" value="${client.userID}">
			
			
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			          <h4 class="modal-title">Delete Treatment Plan</h4>
			      </div>
			      <div class="modal-body">
			        <p>Are you sure you want to delete <strong>${treatmentPlan.title}</strong> from ${client.userName}'s account?</p>
			        
			      </div>
			      <div class="modal-footer">
			      	<button type="submit" class="btn btn-default" aria-hidden="true" title="Delete this treatment plan.">OK</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
			      </div>
			    </div>
			  </div>
		</form>
	</div>
</c:forEach>
<!-- End Delete TreatmentPlan Modal -->
    
    
	<script>
		$(function() {
		    $('#defaultTreatmentPlanID').change(function() {
		    	this.form.submit();
		    });
		    
		});
	</script>    

	
<c:import url="/WEB-INF/jsp/footer.jsp" />