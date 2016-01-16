<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/favicon.ico">

    <title>Do It Right!</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/custom.css" rel="stylesheet">

	<script src="/js/jquery-1.11.3.min.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/index.jsp">Do It Right!</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
          <li class="active"><a href="/secure/MenuNav?destination=home">Home</a></li>
            <li><a href="/#contact">Contact</a></li>
            <li class="dropdown">
              <a href="/#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown<span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="/task-review.jsp">Record Exercise</a></li>
                <li><a href="/#">Another action</a></li>
                <li><a href="/#">Something else here</a></li>
                <li role="separator" class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href="/#">Separated link</a></li>
                <li><a href="/#">One more separated link</a></li>
              </ul>
            </li>
            <c:if test="${user!=null }"></c:if>
            <c:choose>
			  <c:when test='${user.role=="admin" }'>
			  	<li class="dropdown">
	              <a href="/#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Admin Tools<span class="caret"></span></a>
	              <ul class="dropdown-menu">
	                <li><a href="/task-review.jsp">Record Exercise</a></li>
	                <li><a href="/#">Another action</a></li>
	                <li><a href="/#">Something else here</a></li>
	                <li role="separator" class="divider"></li>
	                <li class="dropdown-header">Nav header</li>
	                <li><a href="/#">Separated link</a></li>
	                <li><a href="/#">One more separated link</a></li>
	              </ul>
	            </li>
			  </c:when>
			  <c:when test='${user.role=="therapist" }'>
			  	<li class="dropdown">
	              <a href="/#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Therapist Tools<span class="caret"></span></a>
	              <ul class="dropdown-menu">
	                <li><a href="/task-review.jsp">Record Exercise</a></li>
	                <li><a href="/#">Another action</a></li>
	                <li><a href="/#">Something else here</a></li>
	                <li role="separator" class="divider"></li>
	                <li class="dropdown-header">Nav header</li>
	                <li><a href="/#">Separated link</a></li>
	                <li><a href="/#">One more separated link</a></li>
	              </ul>
	            </li>
			  </c:when>
			  <c:when test='${user.role=="client" }'>
			  	<li class="dropdown">
	              <a href="/#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Client Tools<span class="caret"></span></a>
	              <ul class="dropdown-menu">
	                <li><a href="/task-review.jsp">Record Exercise</a></li>
	                <li><a href="/#">Another action</a></li>
	                <li><a href="/#">Something else here</a></li>
	                <li role="separator" class="divider"></li>
	                <li class="dropdown-header">Nav header</li>
	                <li><a href="/#">Separated link</a></li>
	                <li><a href="/#">One more separated link</a></li>
	              </ul>
	            </li>
			  </c:when>
			</c:choose>
          </ul>

          <ul class="nav navbar-nav navbar-right">
            	<c:choose>
				  <c:when test="${user == null }">
				  	<li><a href="/login.jsp">Sign In&nbsp;<span class="glyphicon glyphicon-log-in" aria-hidden="true"></span></a></li>
				  </c:when>

				  <c:otherwise>
				    <li><a href="/secure/AccountManagement" style="padding-right:0px;">Hello, ${user.email} (${user.role})</a></li>
				    <li title="Sign Out"><a href="/LogOut"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span></a></li>
				  </c:otherwise>
				</c:choose>
          </ul>

        </div><!--/.nav-collapse -->
      </div>
    </nav>

    <div class="container">

      <!-- Main component for a primary marketing message or call to action -->
      <div class="jumbotron">
        <h1>Do It Right!</h1>
        <p>Get the most out of therapy.</p>
       <!--
        <p>
          <a class="btn btn-lg btn-primary" href="../../components/#navbar" role="button">Get started &raquo;</a>
        </p>
       -->
      </div>
