
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

	
<div class="page-header">
    <h1>Client Main Menu</h1>
  </div>
  <c:import url="/WEB-INF/jsp/message-modal.jsp"/>
  
  Continue Most Recently Used Plan:
  <br>Plan Info Here
  
  <p>
    <form class="form-inline" action="/secure/ClientSelectPlan" method="POST">
      <div><button type="submit" class="btn btn-primary">Select a Plan</button></div>
      <input type="hidden" name="path" value="clientManagePlans">
      <input type="hidden" name="requestedAction" value="select-plan-start">
    </form>
  </p>

  <p>
    <form class="form-inline" action="" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Manage My Treatment Plans</button></div>
      <input type="hidden" name="requestedAction" value="editplan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Another Option</button></div>
      <input type="hidden" name="requestedAction" value="assign-treatment-plan-start">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>


	

<c:import url="/WEB-INF/jsp/footer.jsp" />

