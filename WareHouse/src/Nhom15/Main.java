package Nhom15;

import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Main extends TimerTask {

	static String[] list_IdConFig = null;
	public static void main(String args[]) throws AddressException, MessagingException {
		list_IdConFig = args.clone();
		// TimerTask là công việc được thực
		TimerTask myTask = new Main();
		
		// Timer là lịch trình thực thi.
		Timer timer = new Timer();
		
		/*
		 * myTask : task được thực thi 
		 * 100: thời gian chờ chạy lần đầu 
		 * 2000: sau 2s thì chạy cái tiếp theo
		 */
		timer.schedule(myTask, 0, 30000);
	}

	@Override
	public void run() {
		WriteLog writeLog = new WriteLog();
		for (int i = 0; i < list_IdConFig.length; i++) {
			try {
				writeLog.mainWriteLog(list_IdConFig[i]);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
}
