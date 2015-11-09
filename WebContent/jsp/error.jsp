<%--
  Created by IntelliJ IDEA.
  User: cgrid_000
  Date: 8/6/2015
  Time: 7:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="header.jsp" />


  <div class="row">
    <div class="col-md-12">
      <h1>An error has occurred!</h1>
      <p>Our apologies.  We seem to be having some trouble on our end.
        The error has been logged and will be researched.  Please try again later.
      </p>
      Error: ${pageContext.exception.message} <br/>
      Error Code: ${pageContext.errorData.statusCode} : ${pageContext.errorData.servletName}
    </div>
  </div>
  
  
  <h2>
        Error: ${pageContext.exception}  <br/>
    </h2>
 
    Exception stack trace:<br/>
     
    <c:forEach var="trace" items="${pageContext.exception.stackTrace}">
        ${trace}<br/>
    </c:forEach> 


<c:import url="footer.jsp" />