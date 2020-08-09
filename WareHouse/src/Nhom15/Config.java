package Nhom15;

public class Config {
	String id;
	String source;
	String destination;
	String username_Source;
	String username_Des;
	String pw_Source;
	String pw_Des;
	String delimiters;
	int port;
	String format_Name;
	

	public Config() {
		super();
	}


	public Config(String id, String source, String destination, String username_Source, String username_Des,
			String pw_Source, String pw_Des, String delimiters, int port, String format_Name) {
		super();
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.username_Source = username_Source;
		this.username_Des = username_Des;
		this.pw_Source = pw_Source;
		this.pw_Des = pw_Des;
		this.delimiters = delimiters;
		this.port = port;
		this.format_Name = format_Name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public String getUsername_Source() {
		return username_Source;
	}


	public void setUsername_Source(String username_Source) {
		this.username_Source = username_Source;
	}


	public String getUsername_Des() {
		return username_Des;
	}


	public void setUsername_Des(String username_Des) {
		this.username_Des = username_Des;
	}


	public String getPw_Source() {
		return pw_Source;
	}


	public void setPw_Source(String pw_Source) {
		this.pw_Source = pw_Source;
	}


	public String getPw_Des() {
		return pw_Des;
	}


	public void setPw_Des(String pw_Des) {
		this.pw_Des = pw_Des;
	}


	public String getDelimiters() {
		return delimiters;
	}


	public void setDelimiters(String delimiters) {
		this.delimiters = delimiters;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public String getFormat_Name() {
		return format_Name;
	}


	public void setFormat_Name(String format_Name) {
		this.format_Name = format_Name;
	}
	


}