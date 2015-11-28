
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

    <div class="page-header">
        <h1>Assign Treatment Plan</h1>
        
    </div>
    
	<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	<form class="form-horizontal" action="/secure/AssignTreatmentPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-client">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="defaultTreatmentPlanID" value="${defaultTreatmentPlanID }">
		
        <div class="form-group">
            <label for="defaultStage" class="col-sm-2 control-label">Clients:</label>
            <div class="col-sm-10">
                <select class="form-control" id="clientUserID" name="clientUserID">
                    <option  value="">Select a client.</option>
                    <c:forEach items="${clientMap}" var="clientMap">
                    <c:set var="client" scope="page" value="${clientMap.value}"/>
                        <option value="${client.userID}" <c:if test="${client.userID == clientUserID}">selected</c:if>>${client.email}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	        <p>Preview of selected client plans.</p>
	        </div>
        </div>
	</form>
	
	<form class="form-horizontal" action="/secure/AssignTreatmentPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-treatment-plan">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="clientUserID" value="${clientUserID }">
		
		<div class="form-group">
			<label for="selectedDefaultTreatmentPlanID" class="col-sm-2 control-label">Select Default Treatment Plan</label>
	        <div class="col-sm-5">
	            <select class="form-control" id="defaultTreatmentPlanID" name="defaultTreatmentPlanID">
	                <option  value="">Select a treatment plan to edit.</option>
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
     
     <form class="form-horizontal" action="/secure/AssignTreatmentPlan" method="POST">
		<input type="hidden" name="requestedAction" value="copy-plan-to-client">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="clientUserID" value="${clientUserID }">
		<input type="hidden" name="defaultTreatmentPlanID" value="${defaultTreatmentPlanID }">
		
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Save</button>
            </div>
        </div>
    </form>

	<script>
		$(function() {
		    $('#defaultTreatmentPlanID').change(function() {
		    	this.form.submit();
		    });
		    
		    $('#clientUserID').change(function() {
		    	this.form.submit();
		    });
		});
	</script>
	
<c:import url="/WEB-INF/jsp/footer.jsp" />