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
			<p>
			<form class="form-inline" action="/secure/CreateTreatmentPlan"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Create
						Treatment Plan Template</button>
				</div>
				<input type="hidden" name="requestedAction"	value="plan-create-start">
				<input type="hidden" name="path" value="creatingPlanTemplate">
			</form>
			</p>

			<p>
			<form class="form-inline" action="/secure/EditTreatmentPlan" method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Edit
						Treatment Plan Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="plan-edit-start">
				<input type="hidden" name="path" value="editingPlanTemplate">
			</form>
			</p>
		</div>

		<div class="well well-sm">
			<p>
			<form class="form-inline" action="/secure/CreateStage"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Create Stage
						Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="stage-create-start">
				<input type="hidden" name="path" value="creatingStageTemplate">
			</form>
			</p>
			<p>
			<form class="form-inline" action="/secure/EditStage"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Edit
						Stage Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="stage-edit-start">
				<input type="hidden" name="path" value="editingStageTemplate">
			</form>
			</p>
		</div>

		<div class="well well-sm">
			<p>
			<form class="form-inline" action="/secure/CreateTask" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Create
						Task Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="create-task-start">
				<input type="hidden" name="path" value="creatingTaskTemplate">
			</form>
			</p>
			<p>
			<form class="form-inline" action="/secure/EditTask" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Edit
						Task Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="edit-task-start">
				<input type="hidden" name="path" value="editingTaskTemplate">
			</form>
			</p>
		</div>
		<div class="well well-sm">
			<p>
			<form class="form-inline" action="" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" disabled>Manage Lists</button>
				</div>
				<input type="hidden" name="requestedAction" value="manage-lists">
			</form>
			</p>
		</div>
	</div>
</div>



<c:import url="/WEB-INF/jsp/footer.jsp" />