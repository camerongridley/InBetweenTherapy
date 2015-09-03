<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 2:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="../header.jsp" />

<div class="container">
  <div class="page-header">
    <h1>Admin Main Menu</h1>
  </div>
  <p>
    <form class="form-inline" action="./CreateTreatmentPlan" method="POST">
      <div><button type="submit" class="btn btn-primary">Create Treatment Plan Template</button></div>
      <input type=hidden name="treatmentPlanCreationStep" value="beginning">
    </form>
  </p>

  <p>
    <form class="form-inline" action="./EditTreatmentPlan" method="POST">
      <div><button type="submit" class="btn btn-primary">Edit Treatment Plan Template</button></div>
      <input type=hidden name="chosenAction" value="editplan">
    </form>
  </p>


</div>


<c:import url="../footer.jsp" />