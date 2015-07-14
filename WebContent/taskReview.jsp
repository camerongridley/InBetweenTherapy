<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="header.jsp" />
	
	
	
<div class="container">
	<div class="row">
		<div class="col-md-12">
	
			<h1>Treatment Issue: ${txIssue.name }</h1>
			
			<c:forEach var="stage" items="${txIssue.stages}" varStatus="stageStatus">
				<strong>Stage ${stageStatus.index }: <c:out value="${stage.name }" /></strong></br>
				<c:forEach var="task" items="${stage.tasks }" varStatus="taskStatus">
				
					<div class="panel panel-default panel-task">
					  <div class="panel-heading panel-heading-task">
					  	<input type="checkbox" id="${task.id }" aria-label="Task: ${task.name }">
						<a role="button" data-toggle="collapse" href="#collapse${task.id }" aria-expanded="true" aria-controls="collapse${task.id }">
				          ${task.name }
				        </a>
					  </div>
					  <div id="collapse${task.id }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${task.id }">
						  <div class="panel-body panel-body-task">
						    ${task.description }
						  </div>
					  </div>
					</div>
					
				</c:forEach>
			</c:forEach>
		
		</div>
	</div>
</div>
		
<c:import url="footer.jsp" />