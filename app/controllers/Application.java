package controllers;

import java.io.File;

import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

	public static Result index() 
	{
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
		String myUploadFolder = "D:\\";
        file.renameTo(new File(myUploadFolder, fileName));
	}

}