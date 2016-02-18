<script>
$(document).ready(function(){
    //var reqAc = "js:requestedAction: ${requestedAction}";
    //var pat = "js: path: ${path}";
    var scroll = "${scrollTo}";
    //console.log(reqAc);
    //console.log(pat);
    console.log("scrollTo: ${scrollTo}");
    if(scroll !== "" && scroll!== null && scroll !== undefined){
    	console.log("Inside if statement.");
    	$('html, body').animate({
            scrollTop: $("#${scrollTo}").offset().top-100
        }, 0);
    }
    
});
</script>