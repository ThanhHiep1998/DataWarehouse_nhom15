package Nhom15;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectDB {
	
		static String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=DATAWAREHOUSE_data_Student";
		static String username = "sa";
		static String pass = "12345678";
		
		public static Connection getConnect(String s1, String s2, String s3) {
			
			Connection connection = null;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//gọi class nó ra
				connection = (Connection) DriverManager.getConnection(s1, s2, s3);
				System.out.println("Connect success...");
			}catch (SQLException | ClassNotFoundException e) {
				System.out.println("Error when you connect to database !Error is: " + e.getMessage());
			}
			return connection;

		}
		
		
		public static Connection getConnection(String dburl, String username, String pw) {
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


