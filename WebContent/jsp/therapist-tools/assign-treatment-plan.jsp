
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

    <div class="page-header">
        <h1>Assign Treatment Plan</h1>
        
    </div>
    
	<c:import url="/jsp/message-modal.jsp"/>

	<form class="form-horizontal" action="./AssignTreatmentPlan" method="POST">
		<input type="hidden" name="requestedAction" value="select-client"">
		<input type="hidden" name="path" value="${path }">
		
        <div class="form-group">
            <label for="defaultStage" class="col-sm-2 control-label">Clients:</label>
            <div class="col-sm-10">
                <select class="form-control" id="clientList" name="clientList">
                    <option  value="">Select a client.</option>
                    <c:forEach items="${clientMap}" var="clientMap">
                    <c:set var="client" scope="page" value="${clientMap.value}"/>
                        <option value="${client.userID}" <c:if test="${client.userID == selectedUserID}">selected</c:if>>${client.email}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
        <p>Preview of selected client plans.</p>
        </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Save</button>
            </div>
        </div>
    </form>

	
	
<c:import url="/jsp/footer.jsp" />