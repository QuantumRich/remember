$(function() {
	$('#datetimepicker1').datetimepicker({
		language : 'en',
		format : 'MM/DD/YYYY',
		useCurrent : true,
		autoclose : true,
		pickTime : false
	});

	$.ajax({
		type : "POST",
		url : "/event",
		data : {
			param1: "Warriors vs. Clippers"
		},
		success : function() {
			
		}
	});
});