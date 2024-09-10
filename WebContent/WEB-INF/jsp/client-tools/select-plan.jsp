<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

    <div class="page-header">
        <h2><span class="glyphicon glyphicon glyphicon-th-list panel-icon" aria-hidden="true"></span>Your Treatment Plans</h2>
    </div>
    <c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	<div class="form-horizontal">
	        <div class="panel panel-success">
	            <div class="panel-heading"><h4>Plans In Progress</h4></div>
	            <div class="panel-body">
		            <div class="form-horizontal hide-overflow">
		                <c:forEach items="${inProgressPlansList}" var="activePlan">
		                <div class="col-sm-6 col-md-4 col-lg-3">
			                <div class="panel panel-primary">
							  <div class="panel-heading" title="${activePlan.title}">
							    <h3 class="panel-title"><c:if test="${user.activeTreatmentPlanID == activePlan.treatmentPlanID}"><span style="color:yellow;" class="glyphicon glyphicon-star panel-icon" title="This is currently set as the primary active plan." aria-hidden="true"></span></c:if>${activePlan.title}</h3>
							  </div>
							  <div class="panel-body">
							    <div class="progress" title="This plan is ${activePlan.percentComplete()}% complete.">
								  <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="${activePlan.percentComplete()}" aria-valuemin="0" aria-valuemax="100" style="width: ${activePlan.percentComplete()}%;">
								    ${activePlan.percentComplete()}%
								  </div>
								</div>
								<p><strong>Current Stage: </strong></p>
								<p>${activePlan.currentStage.title } (${activePlan.currentStage.percentComplete}%)</p>
								
								<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
									<input type="hidden" name="requestedAction" value="make-active-plan">
									<input type="hidden" name="path" value="${path }">
									<input type="hidden" name="initialize" value="no">
									<input type="hidden" name="clientUUID" value="${clientUUID}">
									<input type="hidden" name="treatmentPlanID" value="${activePlan.treatmentPlanID}">
									
									<button type="submit" class="btn btn-default" aria-label="Left Align" title="Make this your active Treatment Plan.">
									  <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
									</button>
								</form>
		
								<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
									<input type="hidden" name="requestedAction" value="select-plan-preview">
									<input type="hidden" name="path" value="${path }">
									<input type="hidden" name="initialize" value="no">
									<input type="hidden" name="clientUUID" value="${clientUUID}">
									<input type="hidden" name="treatmentPlanID" value="${activePlan.treatmentPlanID}">
									
									<button type="submit" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
									  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
									</button>
								</form>
								

								<button type="button" class="btn btn-default glyphicon glyphicon-remove" onclick="updateDeleteModal(${activePlan.treatmentPlanID}, '${activePlan.title}')" data-toggle="modal" data-target="#delete_plan_modal" aria-hidden="true" title="Delete this treatment plan."></button>
							  </div>
							</div>
						</div>	
		                </c:forEach>
		            </div>
	            </div>
	        </div>    
        </div>
        
        <div class="form-horizontal">
	        <div class="panel panel-info">
	            <div class="panel-heading"><h4>Unstarted Plans</h4></div>
	            <div class="panel-body">
		            <div class="form-horizontal">
		                <c:forEach items="${assignedPlansList}" var="unstartedPlan">
		                <div class="col-sm-6 col-md-4 col-lg-3">
			                <div class="panel panel-primary">
							  <div class="panel-heading">
							    <h3 class="panel-title">${unstartedPlan.title}</h3>
							  </div>
							  <div class="panel-body">
							  	<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
									<input type="hidden" name="requestedAction" value="make-active-plan">
									<input type="hidden" name="path" value="${path }">
									<input type="hidden" name="initialize" value="yes">
									<input type="hidden" name="clientUUID" value="${clientUUID}">
									<input type="hidden" name="treatmentPlanID" value="${unstartedPlan.treatmentPlanID}">
									
									<button type="submit" class="btn btn-default" aria-label="Left Align" title="Make this your active Treatment Plan.">
									  <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
									</button>
								</form>
							  	
							  	<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
									<input type="hidden" name="requestedAction" value="select-plan-preview">
									<input type="hidden" name="path" value="${path }">
									<input type="hidden" name="clientUUID" value="${clientUUID}">
									<input type="hidden" name="treatmentPlanID" value="${unstartedPlan.treatmentPlanID}">
									
									<button type="submit" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
									  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
									</button>
								</form>
								
								<button type="button" class="btn btn-default glyphicon glyphicon-remove" onclick="updateDeleteModal(${unstartedPlan.treatmentPlanID}, '${unstartedPlan.title}')" data-toggle="modal" data-target="#delete_plan_modal" aria-hidden="true" title="Delete this treatment plan."></button>
							  </div>
							</div>
						</div>	
		                </c:forEach>
		            </div>
	            </div>
	        </div>    
        </div>
        
        <div class="form-horizontal">
        <div class="panel panel-default">
            <div class="panel-heading"><h4>Completed Plans</h4></div>
            <div class="panel-body">
	            <div class="form-horizontal">
	                <c:forEach items="${completedPlansList}" var="completedPlan">
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
								<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
									<input type="hidden" name="requestedAction" value="make-active-plan">
									<input type="hidden" name="path" value="${path }">
									<input type="hidden" name="initialize" value="no">
									<input type="hidden" name="clientUUID" value="${clientUUID}">
									<input type="hidden" name="treatmentPlanID" value="${completedPlan.treatmentPlanID}">
									
									<button type="submit" class="btn btn-default" aria-label="Left Align" title="Make this your active Treatment Plan.">
									  <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
									</button>
								</form>
								
								<form class="form-horizontal form-inline-controls" action="/secure/ClientSelectPlan" method="POST">
									<input type="hidden" name="requestedAction" value="select-plan-preview">
									<input type="hidden" name="path" value="${path }">
									<input type="hidden" name="clientUUID" value="${clientUUID}">
									<input type="hidden" name="treatmentPlanID" value="${completedPlan.treatmentPlanID}">
									
									<button type="submit" class="btn btn-default" aria-label="Left Align" title="Client View of Treatment Plan">
									  <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
									</button>
								</form>
								
								<button type="button" class="btn btn-default glyphicon glyphicon-remove" onclick="updateDeleteModal(${completedPlan.treatmentPlanID}, '${completedPlan.title}')" data-toggle="modal" data-target="#delete_plan_modal" aria-hidden="true" title="Delete this treatment plan."></button>
						  </div>
						</div>
					</div>	
	                </c:forEach>
	            </div>
            </div>
        </div>    
       </div>


<!-- Delete TreatmentPlan Modal -->
<div class="modal" id="delete_plan_modal">
	<form id="formDeletePlanModal" action="/secure/ClientSelectPlan" method="POST">
		<input type="hidden" name="requestedAction" value="delete-plan">
		<input type="hidden" name="path" value="${path }">	
		<input type="hidden" id="treatmentPlanIDDynamic" name="treatmentPlanID" value="123" >
<!-- 		<input type="hidden" id="treatmentPlanTitleDynamic" name="treatmentPlanTitle" value=""> -->
		<input type="hidden" name="clientUUID" value="${clientUUID }" >	
		
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		          <h4 class="modal-title">Delete Treatment Plan</h4>
		      </div>
		      <div class="modal-body">
		        <p>Are you sure you want to delete <strong><label id="treatmentPlanTitleDynamic">label text</label></strong>? <span class="warning-message" >You cannot undo this.</span></p>
		        
		      </div>
		      <div class="modal-footer">
		      	<button type="submit" class="btn btn-default" aria-hidden="true" title="Delete this treatment plan.">OK</button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      </div>
		    </div>
		  </div>
	</form>
</div>

<script>
function test(id, title){
	console.log("test function: " + id + " - " + title);
}

function updateDeleteModal(treatmentPlanID, treatmentPlanTitle){
	console.log("delete plan: " + treatmentPlanID + " - " + treatmentPlanTitle);
	
	var inputTreatmentPlanID = document.getElementById("treatmentPlanIDDynamic");
	if(inputTreatmentPlanID!= undefined){
		console.log("planID:" + inputTreatmentPlanID.value)
		inputTreatmentPlanID.value = treatmentPlanID;
		console.log("planID after:" + inputTreatmentPlanID.value)
	}

	var labelTreatmentPlanTitle = document.getElementById("treatmentPlanTitleDynamic");
	if(labelTreatmentPlanTitle!= undefined){
		console.log("label inner html:" + labelTreatmentPlanTitle.innerHTML)
		labelTreatmentPlanTitle.innerHTML = treatmentPlanTitle;
	}
	
};

</script>

<%-- 
	<h3>Continue a plan.</h3>
	<form class="form-horizontal" action="/secure/ClientSelectPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-plan-load">
		<input type="hidden" name="path" value="client-execute-plan">
		<input type="hidden" name="initialize" value="no">
		
        <div class="form-group">
            <label for="treatmentPlanID" class="col-sm-3 control-label">Plans in Progress:</label>
            <div class="col-sm-8">
                <select class="form-control" id="treatmentPlanID" name="treatmentPlanID">
                    <option  value="">Select a plan.</option>
                    <c:forEach items="${inProgressPlansList}" var="inProgressPlan">
                        <option value="${inProgressPlan.treatmentPlanID}" >${inProgressPlan.title}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-sm-1">
            	<button type="submit" class="btn btn-primary">Go!</button>
            </div>
        </div>
        <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	        <p>Preview of selected client plans.</p>
	        </div>
        </div>
	</form>

	<h3>Start a new plan.</h3>
	<form class="form-horizontal" action="/secure/ClientSelectPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-plan-load">
		<input type="hidden" name="path" value="client-execute-plan">
		<input type="hidden" name="initialize" value="yes">

		
        <div class="form-group">
            <label for="treatmentPlanID" class="col-sm-3 control-label">Assigned Plans:</label>
            <div class="col-sm-8">
                <select class="form-control" id="treatmentPlanID" name="treatmentPlanID">
                    <option  value="">Select a plan.</option>
                    <c:forEach items="${assignedPlansList}" var="assignedPlan">
                        <option value="${assignedPlan.treatmentPlanID}" >${assignedPlan.title}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-sm-1">
            	<button type="submit" class="btn btn-primary">Go!</button>
            </div>
        </div>
        <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	        <p>Preview of selected client plans.</p>
	        </div>
        </div>
	</form>
	
	<h3>View Completed Plans.</h3>
	<form class="form-horizontal" action="/secure/ClientSelectPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-plan-load">
		<input type="hidden" name="path" value="client-execute-plan">
		<input type="hidden" name="initialize" value="no">

		
        <div class="form-group">
            <label for="treatmentPlanID" class="col-sm-3 control-label">Assigned Plans:</label>
            <div class="col-sm-8">
                <select class="form-control" id="treatmentPlanID" name="treatmentPlanID">
                    <option  value="">Select a plan.</option>
                    <c:forEach items="${completedPlansList}" var="completedPlan">
                        <option value="${completedPlan.treatmentPlanID}" >${completedPlan.title}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-sm-1">
            	<button type="submit" class="btn btn-primary">Go!</button>
            </div>
        </div>
        <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	        <p>Preview of selected client plans.</p>
	        </div>
        </div>
	</form>
	
        	
	<h3>Your Plans.</h3>
	<form class="form-horizontal" action="/secure/ClientSelectPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-plan-load">
		<input type="hidden" name="path" value="client-execute-plan">
		<input type="hidden" name="initialize" value="no">

		<div class="list-group col-md-6">
			<ul class="list-group">
			  <c:forEach items="${inProgressPlansList}" var="inProgressPlan">
	              <a href="#" class="list-group-item list-group-item-success" value="${inProgressPlan.treatmentPlanID}">${inProgressPlan.title}</a>
	          </c:forEach>
	        </ul>
			<ul class="list-group">
			  <c:forEach items="${assignedPlansList}" var="assignedPlan">
	              <a href="#" class="list-group-item list-group-item-info" value="${assignedPlan.treatmentPlanID}">${assignedPlan.title}</a>
	          </c:forEach>
			</ul>
			<ul class="list-group">  
			  <c:forEach items="${completedPlansList}" var="completedPlan">
	              <a href="#" class="list-group-item list-group-item-warning progress-bar-striped" value="${completedPlan.treatmentPlanID}">${completedPlan.title}</a>
	          </c:forEach>
			</ul>
		</div>
        
        <div class="list-group col-md-6">
			<ul class="list-group">
			  <c:forEach items="${inProgressPlansList}" var="inProgressPlan">
	              <li class="list-group-item list-group-item-success" value="${inProgressPlan.treatmentPlanID}">${inProgressPlan.title}</li>
	          </c:forEach>
			</ul>
			<ul class="list-group">
			  <c:forEach items="${assignedPlansList}" var="assignedPlan">
	              <li class="list-group-item list-group-item-info" value="${assignedPlan.treatmentPlanID}">${assignedPlan.title}</li>
	          </c:forEach>
			</ul>
			<ul class="list-group">  
			  <c:forEach items="${completedPlansList}" var="completedPlan">
	              <li class="list-group-item list-group-item-warning progress-bar-striped" value="${completedPlan.treatmentPlanID}">${completedPlan.title}</li>
	          </c:forEach>
			</ul>
		</div>
        
	</form> 
--%>
<script src="/js/custom-form-submission.js"></script>

<c:import url="/WEB-INF/jsp/footer.jsp" />