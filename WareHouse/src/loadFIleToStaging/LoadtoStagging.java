package loadFIleToStaging;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mysql.jdbc.PreparedStatement;

public class LoadtoStagging {

	private static void updateLogs() throws SQLException {

		String jdbcURL = "jdbc:mysql://localhost:3306";
		String username = "root";
		String password = "123456";
		// PreparedStatement statement = null;
		ConnectDB connect = new ConnectDB();
		connect.connectDatabase(jdbcURL, username, password);
		Statement stmt = connect.connection.createStatement();
		String sql2 = "SET SQL_SAFE_UPDATES = 0";
		// update trang thai trong log
		String sql4 = "update sinhvien.hocsinh set sinhvien.sihvien.ten='Hiep' where sinhvien.sinhvien.STT like '1'";
		stmt.executeUpdate(sql2);
		stmt.executeUpdate(sql4);

	}

	public static void main(String[] args) throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306";
		String username = "root";
		String password = "123456";
		// PreparedStatement statement = null;
		ConnectDB connect = new ConnectDB();
		connect.connectDatabase(jdbcURL, username, password);
		System.out.println("OK");

		Statement stmt = connect.connection.createStatement();
		System.out.println("SSSSS");

		/// time
		long start = System.currentTimeMillis();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		System.out.println(start);
		///insert cả 1 db này vào db khác, 2db phái giống nhau về số lượng côt
		
		String sql2 = "insert datamart.datamart select * from sinhvien.sinhvien ";

		String sql4 = "ALTER TABLE datamart.datamart ADD id varchar(50)";
		String kk = "ddd";
		String sql5 = "UPDATE datamart.datamart SET  id = '" + kk + "'";

		System.out.println("ZZZZZZ");
		stmt.executeUpdate(sql2);
		stmt.executeUpdate(sql4);
		stmt.executeUpdate(sql5);
		System.out.println("dddddddddd");
		updateLogs();
		System.out.println("llllllllllllll");
		connect.connection.close();
		long end = System.currentTimeMillis();
		System.out.println(dtf.format(now));
		System.out.println(end);
		System.out.printf("Import done in %d ms\n", (end - start));

	}

}
