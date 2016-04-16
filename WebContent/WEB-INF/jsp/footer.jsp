<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

    </div> <!-- /container -->
	    <hr>
	<div class="container">
	    <footer>
	        <p>&copy; 2015 Cameron Gridley</p>
	    </footer>
	</div>

	<c:import url="/js/custom-js-include.jsp" />

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>

    <script>
 
    $( document ).ready(function() {
    	console.log( "Ready for js to run!" );
     
    });
    
    function submitForm(formName){
		var form = document.getElementById(formName);
		
		form.submit();
	};
 
    </script>
    
  </body>
</html>

