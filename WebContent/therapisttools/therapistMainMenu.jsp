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
    <h1>Therapist Main Menu</h1>
  </div>
  <p>
    <form class="form-inline" action="./LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary">Create Treatment Plan</button></div>
      <input type=hidden name="chosenAction" value="createplan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="./LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary">Edit Treatment Plans</button></div>
      <input type=hidden name="chosenAction" value="editplan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="./LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary">Assign Plan to Client</button></div>
      <input type=hidden name="chosenAction" value="assignplan">
    </form>
  </p>

  <p>
    <form class="form-inline" action="./LoadData" method="POST">
      <div><button type="submit" class="btn btn-primary">View Client Progress</button></div>
      <input type=hidden name="chosenAction" value="viewclient">
    </form>
  </p>

</div>


<c:import url="../footer.jsp" />