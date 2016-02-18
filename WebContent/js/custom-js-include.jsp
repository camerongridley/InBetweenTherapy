<script>
$(document).ready(function(){
    var scroll = "${scrollTo}";

    console.log("scrollTo: ${scrollTo}");
    if(scroll !== "" && scroll!== null && scroll !== undefined){
    	console.log("Inside if statement.");
    	$('html, body').animate({
            scrollTop: $("#${scrollTo}").offset().top-135
        }, 0);
    }
    
});
</script>