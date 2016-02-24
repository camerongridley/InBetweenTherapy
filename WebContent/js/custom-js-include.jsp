<script>
$(document).ready(function(){
    var scrollTo = "${scrollTo}";

    if(scrollTo !== "" && scrollTo!== null && scrollTo !== undefined){
    	$('html, body').animate({
            scrollTop: $("#" + scrollTo).offset().top-135
        }, 0);
    } 
});
</script>