<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />

  <div class="page-header">
    <h1>Therapist Main Menu</h1>
  </div>
  <p>
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Create Treatment Plan</button></div>
      <input type="hidden" name="requestedAction" value="createplan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>Edit Treatment Plans</button></div>
      <input type="hidden" name="requestedAction" value="editplan">
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
    <form class="form-inline" action="/secure/LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary" disabled>View Client Progress</button></div>
      <input type="hidden" name="requestedAction" value="viewclient">
    </form>
  </p>



<c:import url="/jsp/footer.jsp" />