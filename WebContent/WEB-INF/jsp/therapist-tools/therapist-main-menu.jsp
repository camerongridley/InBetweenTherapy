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
<div class="col-sm-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Create Treatment Plan">
		    <h3 class="panel-title"><span class="glyphicon glyphicon-list panel-icon" aria-hidden="true"></span>Custom Treatment Plans</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-inline" action="/secure/LoadData" method="POST">
		      <div class=><button type="submit" class="form-control btn btn-primary" disabled>Create Treatment Plan</button></div>
		      <input type="hidden" name="requestedAction" value="createplan">
		      <input type="hidden" name="path" value="assignClientTreatmentPlan">
		    </form>
		    
		    <form class="form-inline" action="/secure/LoadData" method="POST">
		      <div><button type="submit" class="form-control btn btn-primary" disabled>Edit Treatment Plans</button></div>
		      <input type="hidden" name="requestedAction" value="editplan">
		      <input type="hidden" name="path" value="assignClientTreatmentPlan">
		    </form>
		  </div>
		</div>
	</div>	
	
	<div class="col-sm-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Manage Clients">
		    <h3 class="panel-title"><span class="glyphicon glyphicon-user panel-icon" aria-hidden="true"></span>Manage Clients</h3>
		  </div>
		  <div class="panel-body">
			<form class="form-horizontal" action="/secure/ManageClients" method="POST">
				<input type="hidden" name="requestedAction" value="select-client">
				<input type="hidden" name="path" value="manageClients">
				<input type="hidden" name="coreTreatmentPlanID" value="${coreTreatmentPlanID }">
				
		        <div class="form-group">

		            <div class="col-sm-12">
		                <select class="form-control" id="clientUUID" name="clientUUID">
		                    <option  value="">Select a client.</option>
		                    <c:forEach items="${encodedClientMap}" var="clientMap">
		                    <c:set var="uuid" scope="page" value="${clientMap.key}"/>
		                    <c:set var="client" scope="page" value="${clientMap.value}"/>
		                        <option value="${uuid}">${client.email}</option>
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

  
  	<div class="col-sm-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Invite New Client">
		    <h3 class="panel-title"><span class="glyphicon glyphicon-send panel-icon" aria-hidden="true"></span>Invite New Client</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-horizontal" action="/secure/ManageClients" method="POST">
				<input type="hidden" name="requestedAction" value="invite-client">
				<input type="hidden" name="path" value="${path }">
				
		        <div class="form-group">
		            <div class="col-sm-12">
		                <input type="text" class="form-control" id="recipientFirstName" name="recipientFirstName" value="<c:out value="${recipientFirstName }"/>" placeholder="Enter the recipient's first name">
		            </div>
		            <div class="col-sm-12">
		                <input type="text" class="form-control" id="recipientLastName" name="recipientLastName" value="<c:out value="${recipientLastName }"/>" placeholder="Enter the recipient's last name">
		            </div>
		            <div class="col-sm-12">
		                <input type="text" class="form-control" id="recipientInvitationEmail" name="recipientInvitationEmail" value="<c:out value="${recipientInvitationEmail }"/>" placeholder="Enter the recipient's email address">
		            </div>
		        </div>
		        <div class="form-group">
			        <div class="col-sm-12">
			        	<button type="submit" class="btn btn-default">Send Invitation</button>
			        </div>
		        </div>
			</form>
		  </div>
		</div>
	</div>	
	
	<div class="col-sm-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Invitation Status">
		    <h3 class="panel-title"><span class="glyphicon glyphicon-envelope panel-icon" aria-hidden="true"></span>Invitation Status</h3>
		  </div>
		  <div class="panel-body">

				<table class=table> 
					<caption>Optional table caption.</caption> 
						<thead> 
							<tr> 
								<th>#</th> 
								<th>Name</th> 
								<th>Date Invited</th> 
								<th>Date Accepted</th> 
								<th></th>
							</tr> 
						</thead> 
						<tbody> 
						<c:forEach items="${invitationList}" var="invitation" varStatus="loopStatus">
							<c:if test="${invitation.accepted }">
								<tr class="success" title="Invitation Accepted"> 
							</c:if>
							<c:if test="${!invitation.accepted }">
								<tr class="warning" title="Invitation Not Yet Accepted"> 
							</c:if>
							
								<th scope=row>${loopStatus.count}</th> 
								<td>${invitation.recipientFirstName}&nbsp;${invitation.recipientLastName}</td> 
								<td>${invitation.formattedDateInvited}</td> 
								<td>${invitation.formattedDateAccepted}</td> 
								<td>
									<a role="button" data-toggle="modal" data-target="#delete_invitation${invitation.invitationCode }"
										class="btn btn-default btn-xs pull-right"
										title="Delete invitation."> 
										<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
									</a>			
								</td>
							</tr> 
						</c:forEach>
 
						</tbody> 
				</table>

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

<c:forEach items="${invitationList}" var="invitation" varStatus="loopStatus">
	<div class="modal" id="delete_invitation${invitation.invitationCode }">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		          <h4 class="modal-title">Delete Invitation</h4>
		      </div>
		      <div class="modal-body">
		        <p>Are you sure you want to delete the invitation to <strong>${invitation.recipientFirstName} ${invitation.recipientLastName}</strong> ?  <span class="warning-message" >You cannot undo this.</span></p>
		        
		      </div>
		      <div class="modal-footer">
		      	<a role="button" href="/secure/ManageClients?requestedAction=invitation-delete&path=${path}&invitationCode=${invitation.invitationCode}" class="btn btn-default" title="Delete invitation.">
				  OK
				</a>
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		      </div>
		    </div>
		  </div>
		</div>
</c:forEach>

<c:import url="/WEB-INF/jsp/footer.jsp" />