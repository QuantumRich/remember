$(function() {

	var eventCode = $.url().param('code');
	
	function showPhotos(photos) {
		//Flag photos found at http://www.free-country-flags.com/packs/1/flags_style1_large.rar
		var html = '';
		console.log(photos);
		$.each(photos, function(i,photo) {
			var w = 200 + (200 * Math.random());
			var temp = "<div class='cell' style='width:"+w+"px; height: 200px; background-image: url("+photo.url+")'>"
			+"<input value='11.jpg' id='delete_CheckBox' name='delete_CheckBox' type='checkbox' />"
			"<input name='delete_CheckBox' type='hidden' value='false' />"+"</div>";
			html += temp;
		})
		$("#freewall").append(html);
		
		var wall = new freewall("#freewall");
		wall.reset({
			draggable: true,
			selector: '.cell',
			animate: true,
			cellW: 20,
			cellH: 200,
			onResize: function() {
				wall.fitWidth();
			}
		});
		wall.fitWidth();
		// for scroll bar appear;
		$(window).trigger("resize");
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