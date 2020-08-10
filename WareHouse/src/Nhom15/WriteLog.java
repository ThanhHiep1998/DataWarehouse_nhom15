package Nhom15;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.chilkatsoft.CkGlobal;
import com.chilkatsoft.CkScp;
import com.chilkatsoft.CkSsh;


public class WriteLog {
	
//	 khai báo 
	Config config = new Config();
	public static WriteLog writeLog = new WriteLog();
	String result = "", format_Name= "";
	static SendMail sendMail = new SendMail();
	
	//tạo kết nối tới config
	
	public void connectConfig(String id_ConFig) throws AddressException, MessagingException {
		try {
//			Connection connectControlDB = ConnectDB.getConnectSQLServer("jdbc:sqlserver://localhost;databaseName=controldb", "root","sa123");
//			Connection connectControlDB = ConnectDB.getConnectSQLServer("jdbc:sqlserver://localhost;databaseName=testwh", "root","sa123");
			Connection connectControlDB = ConnectDB.getConnectMySQL("jdbc:mysql://localhost:3306/controldb", "root","sa123");
			Statement statement = connectControlDB.createStatement();
			String sql = "Select id, source, destination, username_Source,username_Des, pw_Source,pw_Des,delimiters, port, format_Name from config where id ='"+ id_ConFig + "'";
			ResultSet rs = statement.executeQuery(sql);
			
			
			// Di chuyển con trỏ xuống bản ghi kế tiếp.
			if(rs.next()) {
				// set giá trị 
				config.setId(rs.getString(1));
				config.setSource(rs.getString(2));
				config.setDestination(rs.getString(3));
				config.setUsername_Source(rs.getString(4));
				config.setUsername_Des(rs.getString(5));
				config.setPw_Source(rs.getString(6));
				config.setPw_Des(rs.getString(7));
				config.setDelimiters(rs.getString(8));
				config.setPort(rs.getInt(9));
				config.setFormat_Name(rs.getString(10));
				format_Name = config.getFormat_Name();
				}
				
			
			// Đóng kết nối
			connectControlDB.close();
			config.toString();
				
			} catch (SQLException e) {
				System.out.println("error statement");
				e.printStackTrace();
			}
		}
	
	
	// thực hiện download file từ sever
//	public static void download(String hostName, int port, String userName, String pass, String remotePath,String localPath) 
		public static void DownloadBySCP() throws AddressException, MessagingException {
		// khai báo biến
			String path = writeLog.config.getSource();
			String hostName = writeLog.getHost(path);
			String remotePath = writeLog.getRemotePath(path);
			int port = writeLog.config.getPort();
			String userName = writeLog.config.getUsername_Source();
			String pass = writeLog.config.getPw_Source();
			String localPath =  "D:\\quang";

			//kết nối tới máy chủ ssh
			CkSsh ssh = new CkSsh();
			CkGlobal glob = new CkGlobal();
			glob.UnlockBundle("Anything for 30-day trial");
			
			boolean success = ssh.Connect(hostName, port);
			if (success != true) {
				sendMail.sendMail("Lỗi", "Kết nối tới server thất bại");
				System.out.println("Kết nối đến server bị lỗi...");				
				return;
			}
			// đợi 5s để đọc phản hồi
			ssh.put_IdleTimeoutMs(5000);
			
			// Xác thực bằng tên đăng nhập / mật khẩu:
			success = ssh.AuthenticatePw(userName, pass);
			System.out.println("Đã đăng nhập");
			if (success != true) {
				System.out.println("Đăng nhập thất bại");
				return;
			}
			
			// kết nối thành công rồi sử dụng
			CkScp scp = new CkScp();
			success = scp.UseSsh(ssh);
			if (success != true) {
				System.out.println(scp.lastErrorText());
				return;
			}
			
			int mode = 2;
			// download theo định dạng
			scp.put_SyncMustMatch(writeLog.format_Name);
			success = scp.SyncTreeDownload(remotePath, localPath, mode, false);
			if (success != true) {
				System.out.println(scp.lastErrorText());
				return;
			}
			System.out.println("SCP download file success.");
			System.out.println("-------------------------------");
			
			// Ngắt kết nối server
			ssh.Disconnect();
		}
		
		
		static {
			try {
			//	System.loadLibrary("chilkat");
					System.load("D:\\download\\chilkat-9.5.0-jdk11-x64\\chilkat.dll");
			} catch (UnsatisfiedLinkError e) {
				System.err.println("Native code library failed to load.\n" + e);
				System.exit(1);
			}
		}
		
		
	//   drive.ecepvn.org/volume1/ECEP/song.nguyen/DW_2020/data	
		
	//cắt ra phần host:  drive.ecepvn.org

	public String getHost(String path) {
		String hostName = "";
		int num = path.indexOf("/");
		hostName = path.substring(0, num);
		return hostName;
	}
	
	
	// cắt ra phần remotepath: /volume1/ECEP/song.nguyen/DW_2020/data
	
	public String getRemotePath(String path) {
		String remotePath = "";
		int num = path.indexOf("/");
		remotePath = path.substring(num);
		return remotePath;
	}
	
	
	
	// duyệt rồi ghi log cho từng file
		public void checkFile() throws AddressException, MessagingException {
			File Folder = new File("D:\\quang");
			File[] childFile = Folder.listFiles();
			for (File file : childFile) {
				executeWriteLog(file);
					
		}
			// send mail
		sendMail.sendMail("Write Log", result);
		System.out.println("SEND MAIL SUCCESS");
	}
	

	// thực hiện việc ghi log
	public void executeWriteLog(File file) throws AddressException, MessagingException {
		// lấy ngày, giờ hiện tại 
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String dateFormat = format.format(date);

		try {
			// tạo kết nối để thực hiện việc ghi log
//			Connection connectControlDB = ConnectDB.getConnectSQLServer("jdbc:sqlserver://localhost;databaseName=controldb", "root","sa123");
//			Connection connectControlDB = ConnectDB.getConnectSQLServer("jdbc:sqlserver://localhost;databaseName=testwh", "root","sa123");
			Connection connectControlDB = ConnectDB.getConnectMySQL("jdbc:mysql://localhost:3306/controldb?useSSL=false","root", "sa123");
			String sql_insert = "{call sp_insert_log (?,?,?,?,?,?,?) }" ;
			CallableStatement callableStatement = connectControlDB.prepareCall(sql_insert);
//			callableStatement.setString(1, config.getId().concat("_"+ file.getName()));
			callableStatement.setString(1, "1".concat("_"+file.getName()));
//			callableStatement.setInt(2, Integer.valueOf(config.getId()));
			callableStatement.setInt(2, 2);
			callableStatement.setString(3, file.getName());
			callableStatement.setString(4, "ER");
			callableStatement.setString(5, writeLog.getExtend(file.getAbsolutePath()));
			callableStatement.setString(6, dateFormat);
			callableStatement.setString(7, "NULL");
			
			int count = callableStatement.executeUpdate();
			
			// đóng kết nối
			connectControlDB.close();
			
			if ( count > 0 ) {
			// in ra màn hình
			System.out.println("write log: " + file.getName());
			result += file.getName() +"\t" + "------- write log success"  + " \n";
			System.out.println("------------------------------");
			
			}else {
				// in ra màn hình
				System.out.println(file.getName() + "\t" +"da ghi log roi");
				result += file.getName() +"\t" + "------- đã viết log rồi"  + " \n";
				System.out.println("---------------------------------------------------");
				
			}
			
		} catch (Exception e) {
			System.out.println("fail");
			System.out.println(e);
		}
		
		
	}

	
	//cắt lấy phần mở rộng
	public String getExtend(String path) {
		String extend = "";
		int num = path.indexOf(".") + 1;
		extend = path.substring(num);
		return extend;
	}
	
	
	// send Mail
//	public void sendMail(String text) throws AddressException, MessagingException {
//		
//		// Tạo đối tượng Properties và chỉ định thông tin host, port
//		Properties p = new Properties();
//		p.put("mail.smtp.auth", "true");
//		p.put("mail.smtp.starttls.enable", "true");
//		
//		//host name
//		p.put("mail.smtp.host", "smtp.gmail.com");
//		
//		//port
//		p.put("mail.smtp.port", 587);
//		
//		// Tạo đối tượng Session (phiên làm việc)
//		Session s = Session.getInstance(p, new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication("phamquang16599@gmail.com", "0975390766");
//			}
//		});
//		
//		//Tạo đối tượng messeage
//		Message msg = new MimeMessage(s);
//		// email người gửi
//		msg.setFrom(new InternetAddress("phamquang16599@gmail.com"));
//		// email người nhận
//		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("datawarehousenhom15@gmail.com"));
//		// chủ đề của mail
//		msg.setSubject("Write Log");
//		// nội dung mail
//		msg.setText(text);
//
//		// Gửi mail
//		Transport.send(msg);
//	}
	
	
//	public void mainWriteLog(String id_ConFig) throws AddressException, MessagingException {
//		writeLog.connectConfig(id_ConFig);
//		writeLog.DownloadBySCP();
//		writeLog.checkFile();
//		
//	}

		
	public static void main(String[] args) throws AddressException, MessagingException {
//		for(int i =0;i<args.length;i++) {
//			writeLog.mainWriteLog((args[i]));
//		}
		
//		writeLog.connectConfig("2");
//		writeLog.DownloadBySCP();
		writeLog.checkFile();
	}
}