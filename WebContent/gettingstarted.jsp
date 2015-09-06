<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/7/2015
  Time: 8:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="header.jsp" />

  <div class="row">
    <div class="col-md-12">
      <form class="form-inline" action="./LoadData" method="POST">
        <div style="margin-bottom:20px;">To get started testing, load the test data by clicking below.</div>
        <div><button type="submit" class="btn btn-primary">Load Data</button></div>
      </form>
    </div>
  </div>


<c:import url="footer.jsp" />