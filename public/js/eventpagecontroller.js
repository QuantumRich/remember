$(function() {

	var eventCode = $.url().param('code');
	
	function showPhotos(photos) {
		//var temp = "<div class='cell' style='width:{width}px; height: {height}px; background-image: url(i/photo/{index}.jpg)'></div>";
		$.each(photos, function(i,photos) {
			console.log(photos);
		})
	}
	
	function loadEvent(data) {
		showPhotos(data.pics);
		//console.log(data)	//Put append/show pics method
		$(document).on('change', '.btn-file :file', function() {
			  var input = $(this),
			      numFiles = input.get(0).files ? input.get(0).files.length : 1,
			      label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
			  input.trigger('fileselect', [numFiles, label]);
			});

		$(document).ready( function() { 		//Line not necessary
		   $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
		       
			   var data = new FormData();
			   
			   
		        var input = $(this).parents('.input-group').find(':text'),
		            log = numFiles > 1 ? numFiles + ' files selected' : label;
		        
		        var stuff = $('.btn-file :file');
		            
	            $.each($('.btn-file :file')[0].files, function(i, file) {
	            	data.append("picture" + i, file);
	            });
		            
	            /*if( input.length ) {
		            input.val(log);
		            
		        } else {
		            if( log ) console.log(log);
		        }*/
		        $.ajax({
		        	  type: "POST",
		        	  url: "/api/upload/" + eventCode,
		        	  data: data,
		        	  processData: false,
		              contentType: false,
		        	  success: function(uploadedPics) {
		        		  //console.log(uploadedPics);
		        		  //put append/show pics method here.
		        		  showPhotos(uploadedPics);
		        		  
		        	  }
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