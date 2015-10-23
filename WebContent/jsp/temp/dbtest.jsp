<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<sql:query var="rs" dataSource="jdbc/DoItRight">
SELECT user_id, email FROM user
</sql:query>

<html>
  <head>
    <title>DB Test</title>
  </head>
  <body>

  <h2>Results</h2>

<c:forEach var="row" items="${rs.rows}">
    Foo ${row.user_id}<br/>
    Bar ${row.email}<br/>
</c:forEach>

------
------
<form action="./TestDBServlet" method="post" class="form-inline">
	<input type="text" name="userID">
	<button type="submit">Get Email</button>

</form>
TestModel email: <c:out value="${testModel.email }"></c:out>



  </body>
</html>