package controllers;

import java.io.File;

import play.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import views.html.*;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result upload() 
	{
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");
		if (picture != null) 
		{
			String fileName = picture.getFilename();
			String contentType = picture.getContentType();
			File file = picture.getFile();
			System.out.println("File Name: " + fileName + ", " + contentType);
			return ok("File uploaded");
		} 
		else 
		{
			flash("error", "Missing file");
			return ok();
		}

	}

}