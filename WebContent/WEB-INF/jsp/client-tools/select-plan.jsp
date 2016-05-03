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
        <h1>Your Treatment Plans</h1>
    </div>
    <c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	<h3>Continue a plan.</h3>
	<form class="form-horizontal" action="/secure/ClientSelectPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-plan-load">
		<input type="hidden" name="path" value="client-execute-plan">
		<input type="hidden" name="initialize" value="no">
		
        <div class="form-group">
            <label for="selectedPlanID" class="col-sm-3 control-label">Plans in Progress:</label>
            <div class="col-sm-8">
                <select class="form-control" id="selectedPlanID" name="selectedPlanID">
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
            <label for="selectedPlanID" class="col-sm-3 control-label">Assigned Plans:</label>
            <div class="col-sm-8">
                <select class="form-control" id="selectedPlanID" name="selectedPlanID">
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
            <label for="assignedTreatmentPlanID" class="col-sm-3 control-label">Assigned Plans:</label>
            <div class="col-sm-8">
                <select class="form-control" id="selectedPlanID" name="selectedPlanID">
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

<c:import url="/WEB-INF/jsp/footer.jsp" />