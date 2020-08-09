package Nhom15;

import java.util.*;

import javax.mail.MessagingException;


public class Job_Schedule extends TimerTask {

	public void run() {
		WriteLog writeLog = new WriteLog();
		try {
			writeLog.checkFile();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) {

		// TimerTask là công việc được thực
		Job_Schedule job_schedule = new Job_Schedule();

		// Timer là lịch trình thực thi.
		Timer timer = new Timer();

		/*
		 * myTask : task được thực thi 
		 * 100: thời gian chờ chạy lần đầu 
		 * 2000: sau 2s thì chạy cái tiếp theo
		 */
		timer.schedule(job_schedule, 100, 20000);
	}

}