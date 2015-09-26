<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:import url="/jsp/header.jsp" />

<div class="page-header">
	<h1>Admin Main Menu</h1>
</div>

<c:import url="/jsp/message-modal.jsp" />

<div class="row">
	<div class="col-md-6">
		<div class="well well-sm">
			<p>
			<form class="form-inline" action="./CreateTreatmentPlan"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Create
						Treatment Plan Template</button>
				</div>
				<input type="hidden" name="requestedAction"	value="plan-create-start">
			</form>
			</p>

			<p>
			<form class="form-inline" action="./EditTreatmentPlan" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" disabled>Edit
						Treatment Plan Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="plan-edit-start">
			</form>
			</p>
		</div>

		<div class="well well-sm">
			<p>
			<form class="form-inline" action="./CreateStage"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary">Create Stage
						Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="stage-create-start">
			</form>
			</p>
			<p>
			<form class="form-inline" action="./EditStage"
				method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Edit
						Stage Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="stage-edit-start">
			</form>
			</p>
		</div>

		<div class="well well-sm">
			<p>
			<form class="form-inline" action="./CreateTask" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Create
						Task Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="create-task-start">
			</form>
			</p>
			<p>
			<form class="form-inline" action="./EditTask" method="POST">
				<div>
					<button type="submit" class="btn btn-primary" >Edit
						Task Template</button>
				</div>
				<input type="hidden" name="requestedAction" value="edit-task-start">
			</form>
			</p>
		</div>
	</div>
</div>



<c:import url="/jsp/footer.jsp" />