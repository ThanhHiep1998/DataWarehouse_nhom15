package loadFIle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;

public class Load {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/sinhvien";

	// Ten nguoi dung va mat khau cua co so du lieu
	static final String USER = "root";
	static final String PASS = "123456";
	static Connection conn = null;
	// cai nay chay procedure
	static CallableStatement stmt = null;
	// cai nay de luu danh sach duong dan file
	static ArrayList<String> listER = new ArrayList<String>();
	// cai nay de in cho dep
	static Map<String, String> map = new HashMap<>();
	// lop send mail
	static SendEmail sendMail;
	private static String USER_NAME = "gaoredlion1998@gmail.com"; // GMail user
	// mail gui // name (just
	// the part
	// before
	// "@gmail.com")
	private static String PASSWORD = "thanhhiep1998"; // GMail password
	private static String RECIPIENT = "thanhhiep11031998@gmail.com"; // mail nhan
	static String subject = "Thong bao";
	static String body = "Load thanh cong...";
	// cai nay la gui cho nhieu ng
	static String[] listEmail = { RECIPIENT };
	// may cai truong nay trong table dataconfig
	static String table_target = "";
	static String db_target = "";
	static String temp_target = "";
	static String db_config = "";

	static void connectDb() throws ClassNotFoundException, SQLException {

		// Buoc 2: Dang ky Driver
		Class.forName("com.mysql.jdbc.Driver");
		// Buoc 3: Mo mot ket noi
		System.out.println("Dang ket noi toi co so du lieu ...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.println("Tao cac lenh truy van SQL ...");
	}

	private static void getConfigData(int id) throws SQLException {
		// lay thong tin data
		String sql = "{call datacontrol.getAllDataconfig (?)}";
		stmt = conn.prepareCall(sql);
		int id_config = id;
		stmt.setInt(1, id_config);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			db_target = rs.getString(12);
			System.err.println(db_target);
			table_target = rs.getString(13);
			temp_target = rs.getString(14);
			db_config = rs.getString(15);
		}
	}

	private void getFileFromServer() throws SQLException {

		// lay data ve
		CkSsh ssh = new CkSsh();
		CkGlobal ck = new CkGlobal();
		ck.UnlockBundle("Hello");
		String hostname = "drive.ecepvn.org";
		int port = 2227;
		boolean success = ssh.Connect(hostname, port);
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}

		ssh.put_IdleTimeoutMs(5000);
		success = ssh.AuthenticatePw("guest_access", "123456");
		if (success != true) {
			System.out.println(ssh.lastErrorText());
			return;
		}
		CkScp scp = new CkScp();

		success = scp.UseSsh(ssh);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		scp.put_SyncMustMatch("sinhvien*.*");// down tat ca cac file bat dau
												// bang sinhvien
		String remotePath = "/volume1/ECEP/song.nguyen/DW_2020/data";
		String localPath = "C:\\Users\\Thanh Hiep\\Downloads\\New folder"; // thu muc muon
		// down file
		// ve
		success = scp.SyncTreeDownload(remotePath, localPath, 2, false);
		if (success != true) {
			System.out.println(scp.lastErrorText());
			return;
		}
		System.out.println("Done");
		ssh.Disconnect();
	}

	static void getFileER(int id_config) throws ClassNotFoundException, SQLException {

		String sql = "{call datacontrol.getFile_local (?)}";
		stmt = conn.prepareCall(sql);
		// Dau tien gan ket tham so IN, sau do la tham so OUT

		stmt.setInt(1, id_config);

		// Su dung phuong thuc execute de chay stored procedure.
		System.out.println("Thuc thi stored procedure ...");
		ResultSet rs = stmt.executeQuery();
		String file_src = "";
		while (rs.next()) {
			// gan gia tri key value bang idlog va file name log
			String key = rs.getString(1);
			String value = rs.getString(2);
			map.put(key, value);
			System.out.println(key + "  " + value);

		}
	}

	static void loadToStagging() throws SQLException {
		String useDB = "use " + db_target;
		System.out.println(useDB);

		System.out.println(table_target);
		stmt.executeUpdate(useDB);
		System.err.println("Bat dau load");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String target = "sinhvien.stagging";
			String k = entry.getKey();
			String v = entry.getValue();
			System.out.println("Key: " + k + ", Value: " + v);

			// dung load file ko the bo vao procedure
			String load_stagging = "LOAD DATA  INFILE '" + v + "' " + "INTO TABLE " + target + ""
					+ " FIELDS TERMINATED BY '\t' " + "ENCLOSED BY '' " + "LINES TERMINATED BY '\r\n';";

			System.out.println("Dang load dong:  " + v);
			stmt.executeUpdate(load_stagging);
			System.out.println("load ok");
			String sql_filename = "";

		}

		System.err.println("gui email ok");
	}

	private static void loadToTemp() throws SQLException {
		// chon db minh muon su dung
		String use_dc = "use " + db_target;
		stmt.executeUpdate(use_dc);
		// copy toan bo stagging qua temp
		String call_insert = " insert into " + temp_target + " select * from " + table_target + "";
		stmt = conn.prepareCall(call_insert);
		stmt.executeUpdate();
		System.err.println("load from stagging to temp");

		System.out.println("dung db sinh vien");
		// nay la de xoa stagging
		String call_truncate = " TRUNCATE TABLE " + table_target + ";";
		stmt = conn.prepareCall(call_truncate);
		System.out.println("xoa du lieu trong stagging");
		stmt.executeUpdate();
		System.err.println("load from stagging to temp");

	}

	private static void editTemp() throws SQLException {
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
		sendMail = new SendEmail();
		sendMail.sendFromGMail(USER_NAME, PASSWORD, listEmail, subject, body);
		System.out.println("gui thanh cong cmnr");
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

//		int id_config = Integer.parseInt(args[0]);

		connectDb();
		getConfigData(1);
		// getFileER(1);
		// loadToStagging();
		loadToTemp();
		editTemp();

	}
}