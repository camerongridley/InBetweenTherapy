<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

    <!-- ERROR MESSAGE DISPLAY  -->
    <c:if test="${errorMessage != null}">
		<div class="alert alert-danger alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Error!</strong> ${errorMessage}
		</div>
    </c:if>
    <!-- END ERROR MESSAGE DISPLAY -->
    
    <!-- SUCCESS MESSAGE DISPLAY  -->
    <c:if test="${successMessage != null}">
		<div class="alert alert-success alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Way to go!</strong> ${successMessage}
		</div>
    </c:if>
    <!-- END SUCCESS MESSAGE DISPLAY -->
    
    <!-- SUCCESS MESSAGE DISPLAY  -->
    <c:if test="${infoMessage != null}">
		<div class="alert alert-info alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		  <strong>Heads up!</strong> ${infoMessage}
		</div>
    </c:if>
    <!-- END SUCCESS MESSAGE DISPLAY -->