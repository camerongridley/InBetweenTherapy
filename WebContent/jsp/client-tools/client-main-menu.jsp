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
        <h1>Main Menu</h1>
    </div>
    <p>
    <form class="form-inline" action="./LoadData" method="POST">
        <div><button type="submit" class="btn btn-primary">Continue Plan In Progress</button></div>
        <input type="hidden" name="requestedAction" value="continue">
    </form>
    </p>

    <p>
    <form class="form-inline" action="./ClientSelectPlan" method="POST">
        <div><button type="submit" class="btn btn-primary">Start New Plan</button></div>
        <input type="hidden" name="requestedAction" value="select-plan-start">
    </form>
    </p>

    <%--<p>
    <form class="form-inline" action="../" method="POST">
        <div><button type="submit" class="btn btn-primary">Assign Plan to Client</button></div>
    </form>
    </p>

    <p>
    <form class="form-inline" action="../" method="POST">
        <div><button type="submit" class="btn btn-primary">View Client Progress</button></div>
    </form>
    </p>--%>



<c:import url="/jsp/footer.jsp" />