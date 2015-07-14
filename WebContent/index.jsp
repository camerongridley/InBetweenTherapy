<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="header.jsp" />
	
<div class="container theme-showcase" role="main">

      <div class="page-header">
        <h1>Tables</h1>
      </div>
      <div class="row">
        <div class="col-md-6">
          <table class="table">
            <thead>
              <tr>
                <th>#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Username</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>1</td>
                <td>Mark</td>
                <td>Otto</td>
                <td>@mdo</td>
              </tr>
              <tr>
                <td>2</td>
                <td>Jacob</td>
                <td>Thornton</td>
                <td>@fat</td>
              </tr>
              <tr>
                <td>3</td>
                <td>Larry</td>
                <td>the Bird</td>
                <td>@twitter</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="col-md-6">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Username</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>1</td>
                <td>Mark</td>
                <td>Otto</td>
                <td>@mdo</td>
              </tr>
              <tr>
                <td>2</td>
                <td>Jacob</td>
                <td>Thornton</td>
                <td>@fat</td>
              </tr>
              <tr>
                <td>3</td>
                <td>Larry</td>
                <td>the Bird</td>
                <td>@twitter</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>	
<div class="container">
	<div class="row">
		<div class="col-md-6">
			<p>Select a stage of therapy:</p>
		</div>
		<div class="col-md-6">
			<form class="form-inline" action="./LoadData" method="POST">
				<div class="dropdown">
					<button id="dLabel" type="button" data-toggle="dropdown"
						aria-haspopup="true" aria-expanded="false">
						Choose One: <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" aria-labelledby="dLabel">
						<c:forEach var="stage" items="${txStages}">
							<li id='<c:out value="${stage}"/>'><c:out value="${stage}"/></li>
						</c:forEach>
					</ul>
				</div>
				
				<button type="submit" class="btn btn-primary">Load Data</button>
			</form>
		</div>		
	</div>	
</div>


<c:import url="footer.jsp" />