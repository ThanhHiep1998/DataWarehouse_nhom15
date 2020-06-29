//package TH;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Iterator;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//public class LoadData{
//	public static void main(String[] args) throws Exception {
//		try {
//			Connection connectControlDB = ConnectDatabase.getConnection("jdbc:mysql://localhost:3306/controldb", "root",
//					"sa123");
//
//			Statement statement_db = connectControlDB.createStatement();
//
//			String sql = "Select id, source, des, username_source,username_des,pw_source,pw_des, delimiters, filetype from controldb";
//
//			ResultSet rs = statement_db.executeQuery(sql);
//			Config config = null;
//			
//			// Di chuyển con trỏ xuống bản ghi kế tiếp
//			while (rs.next()) {
//				config = new Config(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
//						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
//			}
//			// Đóng kết nối
//			connectControlDB.close();
//			
//			// Tạo kết nối mới
//			Connection conn = ConnectDatabase.getConnection(config.getDestination(), config.getUsername_Des(),config.getPw_Des());
//			int batchSize = 20;
//			long start = System.currentTimeMillis();
//			
//			// nhập file
//			FileInputStream inputStream = new FileInputStream(config.getSource());
//			Workbook workbook = new XSSFWorkbook(inputStream);
//			Sheet firstSheet = workbook.getSheetAt(0);
//			Iterator<Row> rowIterator = firstSheet.iterator();
//			conn.setAutoCommit(false);
//
//			// Chuẩn bị cho querry
//			int noOfColumns = firstSheet.getRow(0).getLastCellNum();
//			String noCoString = "c1";
//			String noCoValue = "?";
//			for (int i = 2; i <= noOfColumns; i++) {
//				noCoString += ", c" + i;
//				noCoValue += ", ?";
//			}
//
//			// câu lệnh thực thi
//			String sql1 = "INSERT INTO sinhvien (" + noCoString + ") VALUES (" + noCoValue + ")";
//			PreparedStatement statement = conn.prepareStatement(sql1);
//			int count = 0;
//			
//			// chạy vòng lặp cho querry với mỗi hàng
//			while (rowIterator.hasNext()) { 
//				Row nextRow = rowIterator.next();
//				Iterator<Cell> cellIterator = nextRow.cellIterator();
//				int i = 1;
//				// chạy vòng lặp cho querry với mỗi ô
//				while (cellIterator.hasNext()) {
//					Cell nextCell = cellIterator.next();
//					nextCell.setCellType(CellType.STRING);
//					statement.setString(i, (String) nextCell.getStringCellValue());
//					i++;
//				}
//				statement.addBatch();
//				if (count % batchSize == 0) {
//					statement.executeBatch();
//				}
//			}
//			workbook.close();
//			statement.executeBatch();
//			conn.commit();
//			conn.close();
//			long end = System.currentTimeMillis();
//			System.out.printf("Import done in %d ms\n", (end - start));
//		} catch (IOException ex1) {
//			System.out.println("Error reading file");
//			ex1.printStackTrace();
//		} catch (SQLException ex2) {
//			System.out.println("Database error");
//			ex2.printStackTrace();
//		}
//	}
//
//}
