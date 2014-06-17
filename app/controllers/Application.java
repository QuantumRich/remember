package controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import play.Play;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.index;

import play.db.*;

public class Application extends Controller {

	public static Result index() 
	{
		return ok(index.render("Your new application is ready."));
	}

	public static Result upload() throws SQLException 
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
	
	private static void saveFile (File file, String origFileName) throws SQLException 
	{
		Connection connection = DB.getConnection();
		String fileName = UUID.randomUUID().toString();       //TODO: get file extension.
		
		String stmt = "INSERT INTO picture (filename) VALUES (?)";
		PreparedStatement prepStmt = connection.prepareStatement(stmt);
		prepStmt.setString(1, fileName);
	    prepStmt.execute();
		
		String uploadFolder = Play.application().configuration().getString("uploadFolder");
        file.renameTo(new File(uploadFolder, fileName));
	}

}
