$(function() {

	// Variable to store your files
	var files;
	function prepareUpload(event) {
		files = event.target.files;
	}
	// Add events
	$('input[type=file]').on('change', prepareUpload);


	function loadEvent(data) {
		console.log(data)
	}

	var eventCode = $.url().param('code');

	$.ajax({
		url : "/api/event/" + eventCode,
		success : loadEvent
	});

});