package loadFIle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBconnect {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/datacontrol";

	// Ten nguoi dung va mat khau cua co so du lieu
	static final String USER = "root";
	static final String PASS = "123456";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;

		// Buoc 2: Dang ky Driver
		Class.forName("com.mysql.jdbc.Driver");

		// Buoc 3: Mo mot ket noi
		System.out.println("Dang ket noi toi co so du lieu ...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM datacontrol.t1");
		// getting the record of 3rd row

		while (rs.next()) {
			System.out.println(rs.getString(1) + "  " + rs.getString(2));
			System.out.println("aa");
		}

	}
}
