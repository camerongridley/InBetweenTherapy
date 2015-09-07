<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/jsp/header.jsp" />
		


	<c:import url="/jsp/message-modal.jsp"/>
	
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
	</div>



<c:import url="/jsp/footer.jsp" />