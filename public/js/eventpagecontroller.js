$(function() {

	var eventCode = $.url().param('code');
	
	function loadEvent(data) {
		console.log(data)
		$(document).on('change', '.btn-file :file', function() {
			  var input = $(this),
			      numFiles = input.get(0).files ? input.get(0).files.length : 1,
			      label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
			  input.trigger('fileselect', [numFiles, label]);
			});

		$(document).ready( function() {
		   $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
		        
		        var input = $(this).parents('.input-group').find(':text'),
		            log = numFiles > 1 ? numFiles + ' files selected' : label;
		        
		        if( input.length ) {
		            input.val(log);
		            
		        } else {
		            if( log ) alert(log);
		        }
		        //TODO POST
		        $.ajax({
		        	  type: "POST",
		        	  contentType:'multipart/form-data',
		        	  url: "/api/upload/" + eventCode,
		        	  data: {
		        		  picture: $(this).val(),
		        	  }
		        	  //success: success,
		        	  //dataType: dataType
		        	});
		        
		    });
		});
	}



	$.ajax({
		url : "/api/event/" + eventCode,
		success : loadEvent
	});

});