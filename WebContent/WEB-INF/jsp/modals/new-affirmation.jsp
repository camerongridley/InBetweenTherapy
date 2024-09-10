<!-- New Affirmation -->
<div class="modal fade" id="newAffirmation" tabindex="-1"
	role="dialog" aria-labelledby="newAffirmationModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<form class="form-horizontal" action="/secure/treatment-components/EditTreatmentPlan" method="POST">
				<input type="hidden" name="requestedAction" value="create-new-treatment-issue"> 
				<input type="hidden" name="path" value="${path }"> 
				<input type="hidden" name="treatmentPlanID" value="${treatmentPlan.treatmentPlanID}">
				<input type="hidden" name="clientUUID" value="${clientUUID }" >
				
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="newAffirmationModalLabel">Enter
						a new affirmation</h4>
				</div>
				<div class="modal-body">
					<input type="text" class="form-control"
						id="newAffirmation" name="newAffirmation"
						value="<c:out value="${newAffirmation }"/>"
						placeholder="Enter a new affirmation.">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-primary" >Save</button>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- End New Affirmation -->