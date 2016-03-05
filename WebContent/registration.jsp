<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
<div class="row">
  	<div class="col-md-6">
    
          <form class="form-horizontal" action="/Registration" method="POST">
	          <fieldset>
		            <div id="legend">
		              <h2>Register</h2>
		            </div>
		            <div class="control-group">
		              <label class="control-label" for="userName">Username</label>
		              <div class="controls">
		                <input type="text" id="userName" name="userName" placeholder="" class="form-control input-lg" value="${userName }">
		                <p class="help-block">Username can contain any letters or numbers, without spaces</p>
		              </div>
		            </div>
		         
		            <div class="control-group">
		              <label class="control-label" for="email">E-mail</label>
		              <div class="controls">
		                <input type="email" id="email" name="email" placeholder="Please provide your E-mail." class="form-control input-lg" value="${email }">
		                <p class="help-block">Please provide your E-mail</p>
		              </div>
		            </div>
		         	<div class="control-group">
		              <label class="control-label" for="firstName">First Name</label>
		              <div class="controls">
		                <input type="text" id="firstName" name="firstName" placeholder="Please provide your first name." class="form-control input-lg" value="${firstName }">
		                <p class="help-block">Please provide your first name.</p>
		              </div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" for="lastName">Last Name</label>
		              <div class="controls">
		                <input type="text" id="lastName" name="lastName" placeholder="Please provide your last name." class="form-control input-lg" value="${lastName }">
		                <p class="help-block">Please provide your last name.</p>
		              </div>
		            </div>
		            <div class="control-group">
		              <label class="control-label" for="password">Password</label>
		              <div class="controls">
		                <input type="password" id="password" name="password" placeholder="" class="form-control input-lg">
		                <p class="help-block">Password should be at least 6 characters</p>
		              </div>
		            </div>
		         
		            <div class="control-group">
		              <label class="control-label" for="passwordConfirm">Password (Confirm)</label>
		              <div class="controls">
		                <input type="password" id="passwordConfirm" name="passwordConfirm" placeholder="" class="form-control input-lg">
		                <p class="help-block">Please confirm password</p>
		              </div>
		            </div>
		         
		         	<label class="control-label" for="userRole">Register as a:</label>
		         	<div class="radio">
					  <label>
					    <input type="radio" name="userRole" id="userRole" value="client" <c:if test='${userRole.equals("client") || userRole.equals("")}'>checked</c:if>>
					    Client
					  </label>
					</div>
					<div class="radio">
					  <label>
					    <input type="radio" name="userRole" id="userRole" value="therapist" <c:if test='${userRole.equals("therapist")}'>checked</c:if>>
					    Therapist
					  </label>
					</div>
		         	<br>
		            <div class="control-group">
		              <!-- Button -->
		              <div class="controls">
		                <button class="btn btn-success">Register</button>
		              </div>
		            </div>
	          </fieldset>
        </form>
    
    </div> 
</div>
	

<c:import url="/WEB-INF/jsp/footer.jsp" />