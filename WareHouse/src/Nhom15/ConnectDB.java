package Nhom15;

import java.sql.Connection;
import java.sql.DriverManager;


public class ConnectDB {
	
		
		public static Connection getConnectMySQL(String dburl, String username, String pw) {
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(dburl, username, pw);
				System.out.println("connect successfully!");
			} catch (Exception ex) {
				System.out.println("connect failure!");
				ex.printStackTrace();
			}
			return conn;
		}
		
		public static Connection getConnecSQLServer(String dburl, String username, String pw) {
			Connection conn = null;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conn = DriverManager.getConnection(dburl, username, pw);
				System.out.println("connect successfully!");
			} catch (Exception ex) {
				System.out.println("connect failure!");
				ex.printStackTrace();
			}
			return conn;
		}
		
		
	}


