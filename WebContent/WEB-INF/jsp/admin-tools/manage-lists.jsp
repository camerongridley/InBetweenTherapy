
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
		    <h3 class="panel-title"><span class="glyphicon glyphicon-tags panel-icon" aria-hidden="true"></span>Keywords</h3>
		  </div>
		  <div class="panel-body">
		    <form class="form-horizontal" action="/secure/ListManagement" method="POST">
				<input type="hidden" name="requestedAction" value="invite-client">
				<input type="hidden" name="path" value="${path }">
				
		        <div class="form-group">
			        <c:forEach items="${coreTaskKeyords}" var="keyword">
						<c:set var="keywordValue" value="${keyword.value}"></c:set>
						<div class="col-sm-12">
							${keywordValue.keyword}
						</div>
					
					</c:forEach>
		            
		        </div>
		        <div class="form-group">
			        <div class="col-sm-12">
			        	<button type="submit" class="btn btn-default">Save</button>
			        </div>
		        </div>
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
				
		        <div class="form-group">
			        <c:forEach items="${coreTreatmentIssues}" var="treatmentIssue">
						<div class="col-sm-12">
							${treatmentIssue.treatmentIssueName}
						</div>
					
					</c:forEach>
		            
		        </div>
		        <div class="form-group">
			        <div class="col-sm-12">
			        	<button type="submit" class="btn btn-default">Save</button>
			        </div>
		        </div>
			</form>
		  </div>
		</div>
	</div>
	
</div>
<c:import url="/WEB-INF/jsp/footer.jsp" />