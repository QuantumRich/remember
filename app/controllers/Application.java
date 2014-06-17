package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;
import views.html.helper.input;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result upload() 
	{
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");                         //
		if (picture != null) 
		{
			String fileName = picture.getFilename();
			String contentType = picture.getContentType();
			File file = picture.getFile();
			System.out.println("File Name: " + fileName + ", " + contentType);
			saveFile(file, fileName);
			return ok("File uploaded");
		} 
		else 
		{
			flash("error", "Missing file");
			return notFound();
		}
		
		

	}
	
	private static void saveFile (File file, String fileName) 
	{
		
		String myUploadPath = "D:\\";
        file.renameTo(new File(myUploadPath, fileName));
/*		FileInputStream from = null; // Stream to read from source
	    FileOutputStream to = null; // Stream to write to destination
	    try {
	      from = new FileInputStream(file); // Create input stream
	      to = new FileOutputStream(new File("D:\\"+filename)); // Create output stream
	      byte[] buffer = new byte[4096]; // To hold file contents
	      int bytes_read; // How many bytes in buffer

	      // Read a chunk of bytes into the buffer, then write them out,
	      // looping until we reach the end of the file (when read() returns
	      // -1). Note the combination of assignment and comparison in this
	      // while loop. This is a common I/O programming idiom.
	      while ((bytes_read = from.read(buffer)) != -1)
	        // Read until EOF
	        to.write(buffer, 0, bytes_read); // write
	    }
	    // Always close the streams, even if exceptions were thrown
	    finally {
	      if (from != null)
	        try {
	          from.close();
	        } catch (IOException e) {
	          ;
	        }
	      if (to != null)
	        try {
	          to.close();
	        } catch (IOException e) {
	          ;
	        }
	    }
	    */
		
		
		
	}

}