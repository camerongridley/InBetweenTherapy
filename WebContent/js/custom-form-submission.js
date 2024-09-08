function submitForm(formName){
	var form = document.getElementById(formName);
	
	form.submit();
};



 /**This is for dynamically updating a form before submitting it.  Particularly useful workaround for nested forms and 
  * when numerous elements are being dynamically generated and you don't want to create a separate form for each element
 * @param formID
 * @param treatmentPlanID - the hidden input of the designated form 'treatmentPlanIDDynamic' is assigned this value
 * @param stageID - the hidden input of the designated form 'stageIDDynamic' is assigned this value
 * @param stageOrder - the hidden input of the designated form 'stageOrderDynamic' is assigned this value
 * @param taskID - the hidden input of the designated form 'taskIDDynamic' is assigned this value
 * @param taskOrder - the hidden input of the designated form 'taskOrderDynamic' is assigned this value
 */
function updateAndSubmitTreatmentComponentForm(formID, treatmentPlanID, stageID, stageOrder, taskID, taskOrder){
	console.log("task order: " + taskOrder);
	var form = document.getElementById(formID);
	//document.getElementById("taskIDDynamic").value = taskID;
	//document.getElementById("stageIDDynamic").value = stageID;
	//document.getElementById("treatmentPlanIDDynamic").value = treatmentPlanID;
	
	var inputTreatmentPlanID = form.elements["treatmentPlanIDDynamic"];
	if(inputTreatmentPlanID!= undefined){
		inputTreatmentPlanID.value = treatmentPlanID;
	}

	var inputStageID = form.elements["stageIDDynamic"];
	if(inputStageID!= undefined){
		inputStageID.value = stageID;
	}
	
	var inputStageOrder = form.elements["stageOrderDynamic"];
	if(inputStageOrder!= undefined){
		inputStageOrder.value = stageOrder;
	}
	
	var inputTaskID = form.elements["taskIDDynamic"];
	if(inputTaskID!= undefined){
		inputTaskID.value = taskID;
	}

	var inputTaskOrder = form.elements["taskOrderDynamic"];
	if(inputTaskOrder!= undefined){
		inputTaskOrder.value = taskOrder;
	}
	
	//var requestedAction = form.elements["requestedAction"];
	//console.log("Updating and submitting form " + form.id + " - requestedAction: " + requestedAction.value);
	//alert("Updating and submitting form " + formID + " - requestedAction: " + requestedAction.value);

	
	form.submit();
};