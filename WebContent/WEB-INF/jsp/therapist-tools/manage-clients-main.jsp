
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

    <div class="page-header">
        <h1>Manage Clients</h1>
        
    </div>
    
	<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	<form class="form-horizontal" action="/secure/ManageClients" method="POST">
		<input type="hidden" name="requestedAction" value="select-client">
		<input type="hidden" name="path" value="${path }">
		<input type="hidden" name="coreTreatmentPlanID" value="${coreTreatmentPlanID }">
		
        <div class="form-group">
            <label for="clientUserID" class="col-sm-2 control-label">Clients:</label>
            <div class="col-sm-5">
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
	        	<button type="submit" class="btn btn-default">Load Client Data</button>
	        </div>
        </div>
	</form>

	
<c:import url="/WEB-INF/jsp/footer.jsp" />