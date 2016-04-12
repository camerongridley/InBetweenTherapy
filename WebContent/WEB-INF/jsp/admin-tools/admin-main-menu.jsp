<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h1>Admin Main Menu</h1>
</div>

<c:import url="/WEB-INF/jsp/message-modal.jsp" />

<div class="row">
	<div class="col-md-6">
		<div class="well well-sm">

			<form class="form-inline form-inline-controls" action="/secure/treatment-components/CreateTreatmentPlan"
				method="POST">
					<button type="submit" class="btn btn-primary">Create
						Treatment Plan Template</button>
				<input type="hidden" name="requestedAction"	value="plan-create-start">
				<input type="hidden" name="path" value="templateTreatmentPlan">
			</form>

			<form class="form-inline form-inline-controls" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
					<button type="submit" class="btn btn-primary">Edit
						Treatment Plan Template</button>
				<input type="hidden" name="requestedAction" value="plan-edit-start">
				<input type="hidden" name="path" value="templateTreatmentPlan">
			</form>

		</div>

		<div class="well well-sm">
			<p>
			<form class="form-inline" action="/secure/treatment-components/CreateStage"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Create Stage
						Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="stage-create-start">
				<input type="hidden" name="path" value="templateStage">
			</form>
			</p>
			<p>
			<form class="form-inline" action="/secure/treatment-components/EditStage"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Edit
						Stage Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="stage-edit-start">
				<input type="hidden" name="path" value="templateStage">
			</form>
			</p>
		</div>

		<div class="well well-sm">
			<p>
			<form class="form-inline" action="/secure/treatment-components/CreateTask" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Create
						Task Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="create-task-start">
				<input type="hidden" name="path" value="templateTask">
			</form>
			</p>
			<p>
			<form class="form-inline" action="/secure/treatment-components/EditTask" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Edit
						Task Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="edit-task-start">
				<input type="hidden" name="path" value="templateTask">
			</form>
			</p>
		</div>
		<div class="well well-sm">
			<p>
			<form class="form-inline" action="/secure/ListManagement" method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Manage Lists</button>
				</div>
				<input type="hidden" name="requestedAction" value="manage-lists">
				<input type="hidden" name="path" value="adminMaintanence">
			</form>
			</p>
		</div>
	</div>
</div>



<c:import url="/WEB-INF/jsp/footer.jsp" />