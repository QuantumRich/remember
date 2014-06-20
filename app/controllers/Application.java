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
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart picture = body.getFile("picture");
		
		if (picture != null) {
			String fileName = picture.getFilename();
			String contentType = picture.getContentType();
			File file = picture.getFile();
			System.out.println("File Name: " + fileName + ", " + contentType);
			saveFile(file, fileName, code);
			return ok("File uploaded");
		} else {
			flash("error", "Missing file");
			return notFound();
		}
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
		
		ArrayList<Picture> pics = new ArrayList<>();
		while (r.next()) 
		{
			Picture p = new Picture();
			p.url = "/database/"+r.getString("FILENAME");
			pics.add(p);
		}
		
		e.pics = pics;
		return ok(Json.toJson(e));
	}
	
	private static void saveFile(File file, String origFileName, String code)
			throws SQLException {
		Connection connection = DB.getConnection();
		String fileName = UUID.randomUUID().toString()+"-"+origFileName; //TODO: check MIME type

		String stmt = "INSERT INTO picture (filename,EVENT_CODE) VALUES (?,?)";
		PreparedStatement prepStmt = connection.prepareStatement(stmt);
		prepStmt.setString(1, fileName);
		prepStmt.setLong(2, Long.parseLong(code));
		prepStmt.execute();

		String uploadFolder = Play.application().configuration()
				.getString("uploadFolder");
		file.renameTo(new File(uploadFolder, fileName));
	}

}
