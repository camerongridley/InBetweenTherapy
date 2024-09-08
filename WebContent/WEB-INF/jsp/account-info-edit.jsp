<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
<div class="row">
  	<div class="col-md-6">
    
          <form class="form-horizontal" action="/secure/AccountManagement" method="POST">
          	<input type="hidden" name="requestedAction" value="user-update-account-info">
			<input type="hidden" name="path" value="${path }">
	          <fieldset>
		            <div id="legend">
		              <h2>Account Information</h2>
		            </div>
		            <div class="control-group">
		              <label class="control-label" for="userName">Username</label>
		              <div class="controls">
		                <input type="text" id="userName" name="userName" placeholder="" class="form-control input-lg" value="${user.userName }">
		                <p class="help-block">Username can contain any letters or numbers, without spaces</p>
		              </div>
		            </div>
		         
		            <div class="control-group">
		              <label class="control-label" for="email">E-mail</label>
		              <div class="controls">
		                <input type="email" id="email" name="email" placeholder="Please provide your E-mail." class="form-control input-lg" value="${user.email }">
		                <p class="help-block">Please provide your E-mail</p>
		              </div>
		            </div>
		         	<div class="control-group">
		              <label class="control-label" for="firstName">First Name</label>
		              <div class="controls">
		                <input type="text" id="firstName" name="firstName" placeholder="Please provide your first name." class="form-control input-lg" value="${user.firstName }">
		                <p class="help-block">Please provide your first name.</p>
		              </div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" for="lastName">Last Name</label>
		              <div class="controls">
		                <input type="text" id="lastName" name="lastName" placeholder="Please provide your last name." class="form-control input-lg" value="${user.lastName }">
		                <p class="help-block">Please provide your last name.</p>
		              </div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" for="password">Password</label>
		              <div class="controls">
		                <input type="password" id="password" name="password" placeholder="" class="form-control input-lg">
		                <p class="help-block">Your password is required to update account info</p>
		              </div>
		            </div>
		         
			        <button class="btn btn-success" type="button" data-toggle="collapse" data-target="#changePasswordCollapse" aria-expanded="false" aria-controls="changePasswordCollapse">
					  Change Password
					</button>
					<div class="collapse" id="changePasswordCollapse">
					  <div class="well">
								    
						<div class="control-group" id="newPasswordDiv">
			              <label class="control-label" for="newPassword">New Password</label>
			              <div class="controls">
			                <input type="password" id="newPassword" name="newPassword" placeholder="" class="form-control input-lg">
			                <p class="help-block">The new password should be at least 6 characters</p>
			              </div>
			            </div>
			            
						<div class="control-group" id="newPasswordConfirmDiv">
			              <label class="control-label" for="newPasswordConfirm">New Password (Confirm)</label>
			              <div class="controls">
			                <input type="password" id="newPasswordConfirm" name="newPasswordConfirm" placeholder="" class="form-control input-lg">
			                <p class="help-block">Please confirm the password</p>
			                <p class="help-block" id="pCheckPasswordMatch"></p>
			              </div>
			            </div>
						<div class="registrationFormAlert" id="divCheckPasswordMatch">
						</div>
						
					  </div>
					</div>
		            
		            <p>
		            <div class="control-group">
		              <!-- Button -->
		              <div class="controls">
		                <button class="btn btn-primary">Save Changes</button>
		              </div>
		            </div>
		            </p>
	          </fieldset>
        </form>
    
    </div> 
</div>
	
<script>

function checkPasswordMatch() {
    var password = $("#newPassword").val();
    var confirmPassword = $("#newPasswordConfirm").val();

    if(password){
    	$('#newPasswordDiv').addClass('has-success');
    }else{
    	$('#newPasswordDiv').removeClass('has-success');
    }
    
    if (password != confirmPassword){
    	$('#newPasswordConfirmDiv').addClass('has-error');
    	$('#newPasswordConfirmDiv').removeClass('has-success');
        $("#pCheckPasswordMatch").html("Passwords do not match!");
    }else{
    	$('#newPasswordConfirmDiv').addClass('has-success');
    	$('#newPasswordConfirmDiv').removeClass('has-error');
    	$("#pCheckPasswordMatch").html("Passwords match.");
    }
    	     
}



$(document).ready(function () {
   $("#newPasswordConfirm").keyup(checkPasswordMatch);
});

</script>

<c:import url="/WEB-INF/jsp/footer.jsp" />