
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h1>List Management</h1>
</div>


<c:import url="/WEB-INF/jsp/message-modal.jsp"/>

	
<div class="form-horizontal">
	
	<div class="col-sm-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Keywords">
		    <h3 class="panel-title"><span class="glyphicon glyphicon-tags panel-icon" aria-hidden="true"></span>&nbsp;Keywords</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-horizontal" action="/secure/ListManagement" method="POST">
				<input type="hidden" name="requestedAction" value="invite-client">
				<input type="hidden" name="path" value="${path }">
				<table class="table table-striped"> 
						<thead> 
							<tr> 
								<th></th> 
								<th></th> 
								<th></th>
							</tr> 
						</thead>
						<tbody> 
						<c:forEach items="${coreTaskKeyords}" var="keyword" varStatus="loopStatus">
							<c:set var="keywordValue" value="${keyword.value}"></c:set>
							<tr class="" title="Invitation Accepted"> 
								<th scope=row>${loopStatus.count}.</th> 
								<td>${keywordValue.keyword}</td>  
								<td>
									<a role="button" data-toggle="modal" data-target="#delete_invitation${invitation.invitationCode }"
										class="btn btn-default btn-xs pull-right"
										title="Delete keyword"> 
										<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
									</a>			
								</td>
							</tr> 
						</c:forEach>
 
						</tbody> 
				</table>

				<button type="submit" class="btn btn-default">Save</button>

			</form>
		  </div>
		</div>
	</div>
	
	<div class="col-sm-6">
		<div class="panel panel-primary">
		  <div class="panel-heading" title="Treatment Issues">
		    <h3 class="panel-title"><span class="glyphicon glyphicon-certificate panel-icon" aria-hidden="true"></span>Treatment Issues</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-horizontal" action="/secure/ListManagement" method="POST">
				<input type="hidden" name="requestedAction" value="invite-client">
				<input type="hidden" name="path" value="${path }">
				<table class="table table-striped"> 
						<thead> 
							<tr> 
								<th></th> 
								<th></th> 
								<th></th>
							</tr> 
						</thead>
						<tbody> 
						<c:forEach items="${coreTreatmentIssues}" var="treatmentIssue" varStatus="loopStatus">
							<tr class="" title="Invitation Accepted"> 
								<th scope=row>${loopStatus.count}.</th> 
								<td>${treatmentIssue.treatmentIssueName}</td>  
								<td>
									<a role="button" data-toggle="modal" data-target="#delete_invitation${invitation.invitationCode }"
										class="btn btn-default btn-xs pull-right"
										title="Delete keyword"> 
										<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
									</a>			
								</td>
							</tr> 
						</c:forEach>
 
						</tbody> 
				</table>

				<button type="submit" class="btn btn-default">Save</button>

			</form>
		  </div>
		</div>
	</div>
	
</div>
<c:import url="/WEB-INF/jsp/footer.jsp" />