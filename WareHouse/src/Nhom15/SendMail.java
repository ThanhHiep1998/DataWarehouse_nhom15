package Nhom15;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {

	public static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}
			
			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
	
public void sendMail(String subject, String body) throws AddressException, MessagingException {
		
		// Tạo đối tượng Properties và chỉ định thông tin host, port
		Properties p = new Properties();
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.starttls.enable", "true");
		
		//host name
		p.put("mail.smtp.host", "smtp.gmail.com");
		
		//port
		p.put("mail.smtp.port", 587);
		
		// Tạo đối tượng Session (phiên làm việc)
		Session s = Session.getInstance(p, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("phamquang16599@gmail.com", "0975390766");
			}
		});
		
		//Tạo đối tượng messeage
		Message msg = new MimeMessage(s);
		// email người gửi
		msg.setFrom(new InternetAddress("phamquang16599@gmail.com"));
		// email người nhận
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("datawarehousenhom15@gmail.com"));
		// chủ đề của mail
		msg.setSubject(subject);
		// nội dung mail
		msg.setText(body);

		// Gửi mail
		Transport.send(msg);
	}
}