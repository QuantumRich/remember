$(function() {
	$('#datetimepicker1').datetimepicker({
		language : 'en',
		format : 'MM/DD/YYYY',
		useCurrent : true,
		autoclose : true,
		pickTime : false
	});

	$("#createEventButton").click(function()
	{
		$.ajax({
			type : "POST",
			url : "/event",
			data : {
				eventTitle : $("#InputEventTitle").val(),
				eventDesc : $("#InputEventDesc").val(),
				eventDate : $("#InputDate").val()
			},
			success : function(eventCode) {
				$("#event-code-url").val("http://" + window.location.host + "/event?code=" + eventCode);
			}
		});
	});
	
	$("#goToEventButton").click(function()
	{
		var url = $("#event-code-url").val();
		window.location.replace(url);
	});
	

});