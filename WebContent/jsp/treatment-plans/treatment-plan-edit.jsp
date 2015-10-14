<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

    <div class="page-header">
        <h1>Update Treatment Plan</h1>
        <h2>Add and update detail to the treatment plan</h2>
    </div>
    
	<c:import url="/jsp/message-modal.jsp"/>
    
    
    
    
    <form class="form-horizontal" action="./EditTreatmentPlan" method="POST">
		<input type="hidden" name="requestedAction" value="plan-edit-select-plan">
		<input type="hidden" name="path" value="${path }" >
		<c:if test="${path=='editingPlan' }">
			<div class="form-group">
				<label for="selectedDefaultTreatmentPlanID" class="col-sm-2 control-label">Select Default Treatment Plan to Edit</label>
		        <div class="col-sm-5">
		            <select class="form-control" id="selectedDefaultTreatmentPlanID" name="selectedDefaultTreatmentPlanID">
		                <option  value="">Select a treatment plan to edit.</option>
		                <c:forEach var="defaultPlan" items="${defaultTreatmentPlanList }">
		                    <option value="${defaultPlan.treatmentPlanID}" <c:if test="${defaultPlan.treatmentPlanID == treatmentPlan.treatmentPlanID }">selected</c:if> >${defaultPlan.title}</option>
		                </c:forEach>
		            </select>
		             
		        </div>
	
			</div>
		</c:if>
	</form>
    
    <form class="form-horizontal" action="./EditTreatmentPlan" method="POST">
        <input type="hidden" name="requestedAction" value="plan-edit-update" >
        <input type="hidden" name="path" value="${path }" >
        <input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" >
        <div class="form-group">
            <label for="planName" class="col-sm-2 control-label">Plan Name</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="planTitle" name="planTitle" value="${treatmentPlan.title }" placeholder="Enter a treatment plan name here.">
            </div>
        </div>
        <div class="form-group">
            <label for="planDescription" class="col-sm-2 control-label">Plan Description</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="planDescription" name="planDescription" value="${treatmentPlan.description }" placeholder="Describe the treatment plan.">
            </div>
        </div>
        <div class="well well-sm">
            <div class="form-group col-md-12">
            <c:choose>
	            <c:when test="${customTreatmentIssues != null }">
	                <h4>Select <strong>one</strong> of the following options for designating the issue this plan will treat.</h4>
	            </c:when>
	            <c:otherwise>
	            	<h4>Select the issue this plan will treat.</h4>
	            </c:otherwise>
            </c:choose>
            
            </div>
            

            <div class="form-group">
                <label for="defaultTreatmentIssue" class="col-sm-2 control-label">Default Tx Issues</label>
                <div class="col-sm-9">
                    <select class="form-control" id="defaultTreatmentIssue" name="defaultTreatmentIssue">
                        <option  value="">Select a default treatment issue.</option>
                        <c:forEach items="${defaultTreatmentIssues}" var="defaultIssue">
                            <option value="${defaultIssue.treatmentIssueID}" <c:if test="${defaultIssue.treatmentIssueID == treatmentPlan.treatmentIssueID}">selected</c:if>>${defaultIssue.treatmentIssueName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-sm-1">
                	<button type="button" class="btn btn-default" aria-label="Left Align" data-toggle="modal" data-target="#newDefaultTreatmentIssueModal">
					  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
                </div>
            </div>

            <c:if test="${customTreatmentIssues != null }">
            <div class="form-group">
                <label for="customTreatmentIssue" class="col-sm-2 control-label">Existing Custom Tx Issues</label>
                <div class="col-sm-10">
                    <select class="form-control" id="customTreatmentIssue" name="customTreatmentIssue">
                        <option value="">Or select an issue you've previously created.</option>
                        <c:forEach items="${customTreatmentIssues}" var="customIssue">
                            <option value="${customIssue.treatmentIssueID}">${customIssue.treatmentIssueName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-sm-1">
                	<button type="button" class="btn btn-default" aria-label="Left Align" data-toggle="modal" data-target="#newCustomTreatmentIssueModal">
					  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
                </div>
            </div>
            
            </c:if>
        </div>
        
        

        <label for="stageList" class="control-label">Stages</label>
			<c:forEach items="${treatmentPlan.stages }" var="stage">
				<div class="panel panel-default panel-task" id="stageList" title="Click the stage title to expand and see the stage details.">
				  <div class="panel-heading">
				  	<input type="hidden" name="stageID" value="${stage.stageID}"/>
				  	<input type="hidden" name="stageTitle${stage.stageID}" value="${stage.title}"/>
					<a role="button" data-toggle="collapse" href="#collapse${stage.stageID }" aria-expanded="true" aria-controls="collapse${stage.stageID }">
			          <span class="">${stage.title }</span>
			        </a>
			        <a role="button" href="./EditStage?requestedAction=editStage&path=editingPlan&treatmentPlanID=${treatmentPlan.treatmentPlanID }&stageID=${stage.stageID}" class="btn btn-default btn-xs pull-right" title="Edit this stage.">
					  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
					</a>
				  </div>
				  <div id="collapse${stage.stageID }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${stage.stageID }">
					  <div class="panel-body">
					    ${stage.description } 
					  </div>
					  	<c:forEach items="${stage.tasks }" var="task">
				  			<div class="panel-body">
								&nbsp;&nbsp;&nbsp;Task: <c:out value="${task.title }"></c:out>
								<a role="button" href="./EditTask?requestedAction=editStage&path=editingPlan&treatmentPlanID=${treatmentPlan.treatmentPlanID }&stageID=${stage.stageID}&taskID=${task.taskID}" class="btn btn-default btn-xs pull-right" title="Edit this stage.">
								  <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
								</a>
							</div>
							
						</c:forEach>
				  </div>
				</div>
			</c:forEach>

        
        
        
        
        
        
        

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Next Step >></button>
            </div>
        </div>
    </form>

	<!-- New Default Treatment Issue Modal -->
	<div class="modal fade" id="newDefaultTreatmentIssueModal" tabindex="-1" role="dialog" aria-labelledby="newDefaultTreatmentIssueModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
		    <form class="form-horizontal" action="./EditTreatmentPlan" method="POST">
		    <input type="hidden" name="requestedAction" value="create-default-treatment-issue">
		    <input type="hidden" name="path" value="${path }" >
		    <input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}" >
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="newDefaultTreatmentIssueModalLabel">Enter a new default Treatment Issue</h4>
		      </div>
		      <div class="modal-body">
		        <input type="text" class="form-control" id="newDefaultTreatmentIssue" name="newDefaultTreatmentIssue" value="${newDefaultTreatmentIssue }" placeholder="Enter a new default treatment issue.">
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="submit" class="btn btn-primary">Save</button>
		      </div>
		    </form>  
	    </div>
	  </div>
	</div>

	<script>
		$(function() {
		    $('#selectedDefaultTreatmentPlanID').change(function() {
		    	this.form.submit();
		    });
		});
	</script>
<c:import url="/jsp/footer.jsp" />