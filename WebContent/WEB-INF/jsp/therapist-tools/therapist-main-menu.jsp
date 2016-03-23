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
  <h1>Therapist Main Dashboard</h1>
</div>

<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

<div class="form-horizontal"> 
	<div class="col-sm-6 col-md-4 col-lg-3">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Create Treatment Plan">
		    <h3 class="panel-title">Manage Clients</h3>
		  </div>
		  <div class="panel-body">
			<form class="form-horizontal" action="/secure/ManageClients" method="POST">
				<input type="hidden" name="requestedAction" value="select-client">
				<input type="hidden" name="path" value="${path }">
				<input type="hidden" name="coreTreatmentPlanID" value="${coreTreatmentPlanID }">
				
		        <div class="form-group">

		            <div class="col-sm-12">
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
             
        </div>
        
	  </div>
		</div>
	</div>	 
  	
	<div class="col-sm-6 col-md-4 col-lg-3">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Create Treatment Plan">
		    <h3 class="panel-title">Custom Treatment Plans</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-inline" action="/secure/LoadData" method="POST">
		      <div><button type="submit" class="btn btn-primary" disabled>Create Treatment Plan</button></div>
		      <input type="hidden" name="requestedAction" value="createplan">
		      <input type="hidden" name="path" value="assignClientTreatmentPlan">
		    </form>
		    
		    <form class="form-inline" action="/secure/LoadData" method="POST">
		      <div><button type="submit" class="btn btn-primary" disabled>Edit Treatment Plans</button></div>
		      <input type="hidden" name="requestedAction" value="editplan">
		      <input type="hidden" name="path" value="assignClientTreatmentPlan">
		    </form>
		  </div>
		</div>
	</div>	 
  
  	<div class="col-sm-12 col-md-6 col-lg-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Invite New Client">
		    <h3 class="panel-title">Invite New Client</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-horizontal" action="/secure/ManageClients" method="POST">
				<input type="hidden" name="requestedAction" value="invite-client">
				<input type="hidden" name="path" value="${path }">
				
		        <div class="form-group">
		            <div class="col-sm-12">
		                <input type="text" class="form-control" id="clientInvitationName" name="clientInvitationName" value="<c:out value="${clientInvitationName }"/>" placeholder="Enter the client's name">
		            </div>
		            <div class="col-sm-12">
		                <input type="text" class="form-control" id="clientInvitationEmail" name="clientInvitationEmail" value="<c:out value="${clientInvitationEmail }"/>" placeholder="Enter the client's email address">
		            </div>
		        </div>
		        <div class="form-group">
			        <div class="col-sm-offset-2 col-sm-10">
			        	<button type="submit" class="btn btn-default">Send Invitation</button>
			        </div>
		        </div>
			</form>
		  </div>
		</div>
	</div>	
</div> 
<!--   <p>
    <form class="form-inline" action="/secure/ManageClients" method="POST">
      <div><button type="submit" class="btn btn-primary">Manage Clients</button></div>
      <input type="hidden" name="requestedAction" value="client-management-menu">
      <input type="hidden" name="path" value="manageClients">
    </form>
  </p>
  
  <p>
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Create Treatment Plan</button></div>
      <input type="hidden" name="requestedAction" value="createplan">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Edit Treatment Plans</button></div>
      <input type="hidden" name="requestedAction" value="editplan">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="/secure/ManageClients" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Invite a New Client</button></div>
      <input type="hidden" name="requestedAction" value="invite-new-client">
      <input type="hidden" name="path" value="inviteClient">
    </form>
  </p> -->


<c:import url="/WEB-INF/jsp/footer.jsp" />