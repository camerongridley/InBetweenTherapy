<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
	<form class="form-signin" action="./LogIn" method="POST">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" name="email" class="form-control" placeholder="Email address" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      </form>

	<hr>
	<h2>Quick Logins for Testing Purposes</h2>
	<div class="row">
		<div class="col-sm-4">
			<form class="form-inline" action="./LogIn" method="POST">
				<div><button type="submit" class="btn btn-primary" style="margin-bottom:5px">Log in Client</button></div>
				<input type="hidden" name="email" value="bob@aol.com">
				<input type="hidden" name="password" value="password">
			</form>
		</div>
		<div class="col-sm-4">
			<form class="form-inline" action="./LogIn" method="POST">
				<div><button type="submit" class="btn btn-primary" style="margin-bottom:5px">Log in Therapist</button></div>
				<input type="hidden" name="email" value="camerongridley@gmail.com">
				<input type="hidden" name="password" value="password">
			</form>
		</div>
		<div class="col-sm-4">
			<form class="form-inline" action="./LogIn" method="POST">
				<div><button type="submit" class="btn btn-primary" style="margin-bottom:5px">Log in Admin</button></div>
				<input type="hidden" name="email" value="cgridley@gmail.com">
				<input type="hidden" name="password" value="admin">
			</form>
		</div>
	</div>
	

<c:import url="/WEB-INF/jsp/footer.jsp" />