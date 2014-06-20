$(function() {	
	function loadEvent(data) {
		console.log(data)
	}
	
	var eventCode = $.url().param('code');
	
	$.ajax({
		url : "/api/event/"+eventCode,
		success : loadEvent
	});
});