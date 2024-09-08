<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />
		
	<h1>Account Management</h1>

	<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	

      <!-- Example row of columns -->
      <div class="row">
        <div class="col-lg-4">
          <h2>User Information</h2>
          <p>List user info here.</p>
          <form class="form-horizontal" action="/secure/AccountManagement" method="POST">
          	<input type="hidden" name="requestedAction" value="user-edit-account-info">
          	<p><button type="submit" name="submitButton" class="btn btn-default">Edit Account</button></p>
          </form>
        </div>
        <div class="col-lg-4">
          <h2>Treatment Plan Management</h2>
          <p>List Treatment Plan management stuff here </p>
          <p><a class="btn btn-primary" href="#" role="button">View details &raquo;</a></p>
       </div>
        <div class="col-lg-4">
          <h2>Help</h2>
          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa.</p>
          <p><a class="btn btn-primary" href="#" role="button">View details &raquo;</a></p>
        </div>
      </div>



<c:import url="/WEB-INF/jsp/footer.jsp" />