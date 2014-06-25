package controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import model.EventDetails;
import model.Picture;
import play.Play;
import play.db.DB;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.index;

import com.google.common.base.Strings;

public class Application extends Controller {

	public static Result index() {
		return ok(index.render("Your new application is ready."));
	}

	public static Result upload(String code) throws SQLException {
		Connection connection = DB.getConnection();
		MultipartFormData body = request().body().asMultipartFormData();
		ArrayList<Picture> pics = new ArrayList<Picture>();
		for(int i=0; i!=-1 ;i++)
		{
			FilePart picture = body.getFile("picture" + i);
			//String code = body.asFormUrlEncoded().get("code")[0];
			if (picture != null) 
			{
				String fileName = picture.getFilename();
				String contentType = picture.getContentType();
				if(contentType.startsWith("image/"))
				{
					File file = picture.getFile();
					//System.out.println("File Name: " + fileName + ", " + contentType);
					Picture p = saveFile(file, fileName, code, connection);	
					pics.add(p);
				}
				else
				{
					//TODO Set flag that one or more of these files is not an image.
					//return badRequest("Error: File is not an image.");
				}

			}
			else
			{
				connection.close();
				break;
			}
		}
		
		return ok(Json.toJson(pics));			
	}

	public static Result createEvent() throws Exception {
		Map<String, String[]> formData = request().body().asFormUrlEncoded();
		String eventTitle = formData.get("eventTitle")[0];
		String eventDesc = formData.get("eventDesc")[0];
		String eventDateString = formData.get("eventDate")[0];
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date eventDate = df.parse(eventDateString);
		
		if(Strings.isNullOrEmpty(eventTitle))
			throw new Exception();
		
		// System.out.print(value);
		Connection connection = DB.getConnection();
		Long eventCode = System.currentTimeMillis();

		try {
			String stmt = "INSERT INTO event (event_code, event_name, event_desc, event_date) VALUES (?,?,?,?)";
			PreparedStatement prepStmt = connection.prepareStatement(stmt);
			prepStmt.setLong(1, eventCode);
			System.out.println(eventCode);
			prepStmt.setString(2, eventTitle);
			System.out.println(eventTitle);
			prepStmt.setString(3, eventDesc);
			System.out.println(eventDesc);
			prepStmt.setDate(4, new java.sql.Date(eventDate.getTime()));
			// System.out.println(eventCode.toString());

			prepStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(eventCode + "");

	}

	public static Result getEvent(String code) throws SQLException {
		Connection connection = DB.getConnection();
		String stmt = "SELECT * FROM EVENT WHERE EVENT_CODE = ?";
		PreparedStatement prepStmt = connection.prepareStatement(stmt);
		prepStmt.setString(1, code);
		ResultSet r = prepStmt.executeQuery();
		
		EventDetails e = new EventDetails();
		while (r.next()) 
		{
			e.title = r.getString("EVENT_NAME");
			e.description = r.getString("EVENT_DESC");
			e.date = r.getDate("EVENT_DATE").toString();
		}
		
		stmt = "SELECT * FROM PICTURE WHERE EVENT_CODE = ?";
		prepStmt = connection.prepareStatement(stmt);
		prepStmt.setString(1, code);
		r = prepStmt.executeQuery();
		
		ArrayList<Picture> pics = new ArrayList<Picture>();
		while (r.next()) 
		{
			Picture p = new Picture(
					"/database/"+r.getString("FILENAME"), 
					r.getString("FILENAME"), 
					r.getString("CAPTION"),
					r.getDate("DATE_UPLOADED"));
			pics.add(p);
		}
		
		e.pics = pics;
		return ok(Json.toJson(e));
	}
	
	private static Picture saveFile(File file, String origFileName, String code, Connection con) //file, filename, code
			throws SQLException {
		
		String fileName = UUID.randomUUID().toString()+"-"+origFileName; 
		java.sql.Date dateUploaded = new java.sql.Date(System.currentTimeMillis());
		String stmt = "INSERT INTO picture (filename,EVENT_CODE,DATE_UPLOADED) VALUES (?,?,?)";
		PreparedStatement prepStmt = con.prepareStatement(stmt);
		prepStmt.setString(1, fileName);
		prepStmt.setLong(2, Long.parseLong(code));
		prepStmt.setDate(3, dateUploaded);
		prepStmt.execute();
		
		File directory = new File("public\\database");
		directory.mkdir();
		String uploadFolder = Play.application().configuration().getString("uploadFolder");
		file.renameTo(new File(uploadFolder, fileName));
		Picture p = new Picture("/database/"+fileName, fileName, null, dateUploaded);
		return p;
	}

}
