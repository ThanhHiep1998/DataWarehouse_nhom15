package Nhom15;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


public class LoadToDW {
	
	Config config = new Config();
	
	
	// Ten nguoi dung va mat khau cua co so du lieu
	static final String DB_URL = "jdbc:mysql://localhost:3306/sinhvien";
	static final String USER = "root";
	static final String PASS = "sa123";
	static Connection conn = null;
	
	
	// cai nay chay procedure
	static CallableStatement stmt = null;
	
	
	// cai nay de luu danh sach duong dan file
	static ArrayList<String> listER = new ArrayList<String>();
	
	// cai nay de in cho dep
	static Map<String, String> map = new HashMap<>();
	
	// lop send mail
	static SendMail sendMail;
	
	// mail gui // name (just
		// the part
		// before
		// "@gmail.com")
	
	private static String USER_NAME = "gaoredlion1998@gmail.com"; // GMail user
	private static String PASSWORD = "thanhhiep1998"; // GMail password
	private static String RECIPIENT = "datawarehousenhom15@gmail.com"; // mail nhan
	static String subject = "Thong bao";
	static String body = "Load thanh cong...";
	// cai nay la gui cho nhieu ng
	static String[] listEmail = { RECIPIENT };
	
	// may cai truong nay trong table dataconfig
	static String table_target = "";
	static String db_target = "";
	static String temp_target = "";

	static void connectDb() throws ClassNotFoundException, SQLException {
		//Mo mot ket noi
		System.out.println("Dang ket noi toi co so du lieu ...");
		conn = ConnectDB.getConnectMySQL(DB_URL, USER,PASS);
	}
	
	private static void getData() throws SQLException {
		 Statement statement = conn.createStatement();
		String sql = "Select db_target, table_target, temp_target  from dataconfig ";
		ResultSet rs = statement.executeQuery(sql);
		
		if (rs.next()) {
			db_target = rs.getString(1);
			table_target = rs.getString(2);
			temp_target = rs.getString(3);
		}
	}

	
	private static void loadToTemp() throws SQLException {
		// chon db minh muon su dung
		String use_db = "use " + db_target;
		// copy toan bo stagging qua temp
		String call_insert = " insert into " + temp_target + " select * from " + table_target + "";
		stmt = conn.prepareCall(call_insert);
		stmt.executeUpdate();
		System.err.println("load from stagging to temp");

		System.out.println("sử dụng database:" +  use_db);
		// nay la de xoa stagging
		String call_truncate = " TRUNCATE TABLE " + table_target + ";";
		stmt = conn.prepareCall(call_truncate);
		System.out.println("xoa du lieu trong stagging");
		stmt.executeUpdate();
		System.err.println("load from stagging to temp");

	}

	private static void editTemp() throws SQLException, AddressException, MessagingException {
		String use_dc = "use " + db_target;
		stmt.executeUpdate(use_dc);
		// cai cu cac nay de chay duoc update
		String setSafeMode = "SET SQL_SAFE_UPDATES = 0;";
		stmt.executeUpdate(use_dc);
		// xoa cmn dong stt
		String deleteStt = "delete from  " + temp_target + " where stt='stt'";
		stmt.executeUpdate(deleteStt);
		System.out.println("xoa dong stt");
		// them sk vao ne
		String alter_temp = " ALTER TABLE " + temp_target + " ADD sk INT PRIMARY KEY AUTO_INCREMENT;";
		stmt = conn.prepareCall(alter_temp);
		stmt.executeUpdate();
		System.out.println("tao khoa sk cho temp");
//		sendMail = new SendMail();
//		sendMail.sendFromGMail(USER_NAME, PASSWORD, listEmail, subject, body);
//		sendMail.sendMail("load success");
		System.out.println("gui thanh cong cmnr");
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, AddressException, MessagingException {
		connectDb();
		getData();
		loadToTemp();
		editTemp();
		

	}
}