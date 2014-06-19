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
				$("#event-code-url").val(eventCode);
			}
		});
	});
	

});