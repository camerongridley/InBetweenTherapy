<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

  <div class="page-header">
    <h1>Therapist Main Menu</h1>
  </div>
  <c:import url="/WEB-INF/jsp/message-modal.jsp"/>
  <p>
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Create Treatment Plan</button></div>
      <input type="hidden" name="requestedAction" value="createplan">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Edit Treatment Plans</button></div>
      <input type="hidden" name="requestedAction" value="editplan">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="/secure/AssignTreatmentPlan" method="POST">
      <div><button type="submit" class="btn btn-primary">Assign Plan to Client</button></div>
      <input type="hidden" name="requestedAction" value="assign-treatment-plan-start">
      <input type="hidden" name="path" value="assignClientTreatmentPlan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="/secure/ManageClients" method="POST">
      <div><button type="submit" class="btn btn-primary">Manage Clients</button></div>
      <input type="hidden" name="requestedAction" value="client-management-menu">
      <input type="hidden" name="path" value="manageClients">
    </form>
  </p>



<c:import url="/WEB-INF/jsp/footer.jsp" />