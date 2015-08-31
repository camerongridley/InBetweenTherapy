<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="header.jsp" />
		
<div class="container">
	<div class="row">
		<div class="col-md-2">
			Login as a client:
		</div>
		<div class="col-md-10">
			<form class="form-inline" action="./LogIn" method="POST">
				<div><button type="submit" class="btn btn-primary">Log in</button></div>
				<input type=hidden name="email" value="bob@aol.com">
				<input type=hidden name="password" value="password">
			</form>
		</div>
		<%--<div class="col-md-10">
			<form class="form-inline" action="./LoadData" method="POST">
				<div><button type="submit" class="btn btn-primary">Load Data</button></div>
				<input type=hidden name="userType" value="client">
				<input type=hidden name="userID" value="1">
			</form>
		</div>--%>
	</div>
	<hr>
	<div class="row">
		<div class="col-md-2">
			Login as a therapist:
		</div>
		<div class="col-md-10">
			<form class="form-inline" action="./LogIn" method="POST">
				<div><button type="submit" class="btn btn-primary">Log in</button></div>
				<input type=hidden name="email" value="camerongridley@gmail.com">
				<input type=hidden name="password" value="password">

			</form>
		</div>
		<%--<div class="col-md-10">
			<form class="form-inline" action="./LoadData" method="POST">
				<div><button type="submit" class="btn btn-primary">Go To Main Menu</button></div>
				<input type=hidden name="userType" value="therapist">
				<input type=hidden name="userID" value="1">

			</form>
		</div>--%>
	</div>
	<hr>
	<div class="row">
		<div class="col-md-2">
			Login as an admin:
		</div>
		<div class="col-md-10">
			<form class="form-inline" action="./LogIn" method="POST">
				<div><button type="submit" class="btn btn-primary">Log in</button></div>
				<input type=hidden name="email" value="cgridley@gmail.com">
				<input type=hidden name="password" value="admin">

			</form>
		</div>
		<%--<div class="col-md-10">
			<form class="form-inline" action="./LoadData" method="POST">
				<div><button type="submit" class="btn btn-primary">Go To Main Menu</button></div>
				<input type=hidden name="userType" value="therapist">
				<input type=hidden name="userID" value="1">

			</form>
		</div>--%>
	</div>
</div>


<c:import url="footer.jsp" />