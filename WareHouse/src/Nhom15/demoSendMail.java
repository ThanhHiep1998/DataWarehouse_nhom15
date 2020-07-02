package Nhom15;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class demoSendMail {
	
	public void sendMail(String text) throws AddressException, MessagingException {
		Properties p = new Properties();
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.host", "smtp.gmail.com");
		p.put("mail.smtp.port", 587);
		// get Session
		Session s = Session.getInstance(p, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("phamvanquang16599@gmail.com", "0975390766");
			}
		});
		Message msg = new MimeMessage(s);
		msg.setFrom(new InternetAddress("phamvanquang16599@gmail.com"));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("phamquang16599@gmail.com"));
		msg.setSubject("Write Log");
		msg.setSubject("Write Log");
		msg.setText(text);

		Transport.send(msg);
	}
	
	public static void main(String[] args) throws AddressException, MessagingException {
		demoSendMail sendMail = new demoSendMail();
		sendMail.sendMail("Ghi log thành công");
		System.out.println("send success");
	}
	}
