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
        <h1>Create a Treatment Plan</h1>
    </div>
    <p>
    <form class="form-inline" action="../" method="POST">
        <div><button type="submit" class="btn btn-primary">Create Treatment Plan</button></div>
    </form>
    </p>

    <p>
    <form class="form-inline" action="../" method="POST">
        <div><button type="submit" class="btn btn-primary">Edit Treatment Plans</button></div>
    </form>
    </p>

    <p>
    <form class="form-inline" action="../" method="POST">
        <div><button type="submit" class="btn btn-primary">Assign Plan to Client</button></div>
    </form>
    </p>

    <p>
    <form class="form-inline" action="../" method="POST">
        <div><button type="submit" class="btn btn-primary">View Client Progress</button></div>
    </form>
    </p>

</div>


<c:import url="../footer.jsp" />