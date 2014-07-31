$(function() {
	
	/* navigation bar functions */
	$('#navHome').click(function(){
		$('#aboutRemember').hide();
		$('#homeRemember').show();
		$('.navLink').removeClass('active');
		$('#navHome').addClass('active');
	});
	$('#navAbout').click(function(){
		$('#aboutRemember').show();
		$('#homeRemember').hide();
		$('.navLink').removeClass('active');
		$('#navAbout').addClass('active');
	});
	$('#navContact').click(function(){
		//fill in later
		$('.navLink').removeClass('active');
		$('#navContact').addClass('active');
	});
	
	
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
			success : function(eventCode) 
			{
				$("#event-code-url").val("http://" + window.location.host + "/event?code=" + eventCode);
				$("#createModal").modal()
			},
			error: function()
			{
				alert("Please enter in an event title and correct date.")
			}
		});
	});
	
	$("#goToEventButton").click(function()
	{
		var url = $("#event-code-url").val();
		window.location.replace(url);
	});
	
	$("#InputEventTitle").keyup(function()
	{
		var text = $(this).val();
		if(text != "")
		{
			$("#titleCheck").addClass("glyphicon-ok");
			$("#titleInput").addClass("has-success")
			$("#titleCheck").removeClass("glyphicon-remove");
			$("#titleInput").removeClass("has-error")
		}
		else
		{
			$("#titleCheck").removeClass("glyphicon-ok");
			$("#titleInput").removeClass("has-success")
			$("#titleCheck").addClass("glyphicon-remove");
			$("#titleInput").addClass("has-error")
		}
	})
	
	

});