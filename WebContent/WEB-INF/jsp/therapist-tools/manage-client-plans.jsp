
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
		
        <div class="form-group">
            <h3>Assigned Plans</h3>
            <div class="col-sm-5">
                <c:forEach items="${assignedClientPlans}" var="assignedPlan">
                    <p>${assignedPlan.title}</p>
                </c:forEach>
            </div>
        </div>
        <div class="form-group">
	        <div class="col-sm-offset-2 col-sm-10">
	        	<button type="submit" class="btn btn-default">Load Plan</button>
	        </div>
        </div>
	</form>

	
<c:import url="/WEB-INF/jsp/footer.jsp" />