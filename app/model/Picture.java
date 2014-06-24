package model;

import java.sql.Date;


public class Picture {
	public String url;
	public String id;
	public String caption;
	public String dateUploaded;
	
	
	public Picture(String url, String id, String caption, Date dateUploaded)
	{
		this.url = url;
		this.id = id;
		this.caption = caption;
		if(dateUploaded != null)
			this.dateUploaded = dateUploaded.toGMTString();
	}
	
	//TODO make setters for added info
}


