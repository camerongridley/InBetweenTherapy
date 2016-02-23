
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>


<c:import url="/WEB-INF/jsp/header.jsp" />

<div class="page-header">
	<h1>Unauthorized Access</h1>
</div>
  
<c:import url="/WEB-INF/jsp/message-modal.jsp"/>
	
 <div class="row">
    <div class="col-md-12">
      <p>
      	You do not appear to have authorization to the data you are attempting to access.  Please contact customer support for additional help or information.
      </p>
    </div>
</div>
	

<c:import url="/WEB-INF/jsp/footer.jsp" />