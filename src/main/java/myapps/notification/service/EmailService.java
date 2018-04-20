package myapps.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.DatatypeConverter;

import myapps.notification.service.model.Email;
import myapps.notification.service.utils.EmailQueue;

public class EmailService {

	private static Properties props = null;
	private static Session session = null;
	private static int seq = 0;
	private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

	 
	public EmailService() throws IOException {
		if (props == null) {
			props = new Properties();
			props.load(loader.getResourceAsStream("application.properties"));
		}
		if (session == null) {
			session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getProperty("mail.username"),
							props.getProperty("mail.password"));
				}
			});
		}
	}

	private String getContentId() {
		int c = getSeq();
		return c + "." + System.currentTimeMillis() + "@" + "gmail";
	}

	private synchronized int getSeq() {
	    return (seq++) % 100000;
	  }
	
	public boolean sendEmail(String to, String subject, String body) {
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(props.getProperty("mail.username")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
			return true;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendEmail(List<InternetAddress> tos, String subject, String body) {
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
			message.setRecipients(Message.RecipientType.TO, tos.toArray(new Address[tos.size()]));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
			return true;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean sendEmail(List<InternetAddress> tos, String subject, String body, byte[] image){
		try {
			String cid = getContentId();
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setText("<html><body><h1>" + body + "</h1> <img width=256 height=256 src=\"cid:" + cid + "\" /></body></html>", "US-ASCII", "html");
			
			MimeBodyPart imgPart = new MimeBodyPart();
			imgPart.setDataHandler(new DataHandler(new ByteArrayDataSource(image, "image/png")));
			imgPart.setContentID("<" + cid + ">");
			imgPart.setDisposition(MimeBodyPart.INLINE);
			
			MimeMultipart content = new MimeMultipart("related");
			content.addBodyPart(bodyPart);
			content.addBodyPart(imgPart);
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
			message.setRecipients(Message.RecipientType.BCC, tos.toArray(new Address[tos.size()]));
			message.setSubject(subject);
			message.setContent(content);
			
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean sendCashUpAppShareEmail(Email email){
		String img = "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";
		byte[] image = DatatypeConverter.parseBase64Binary(img);
		return sendEmail(email.getTos(), email.getSubject(), email.getBody(), image);
	}
	
	public boolean sendEmail(String emailId, String body, String subject, String image) throws MessagingException{
		String cid = getContentId();
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setText(body.replace("newCid", cid), "US-ASCII", "html");
		
		MimeBodyPart imgPart = new MimeBodyPart();
		imgPart.setDataHandler(new DataHandler(new ByteArrayDataSource(DatatypeConverter.parseBase64Binary(image), "image/png")));
		imgPart.setContentID("<" + cid + ">");
		imgPart.setDisposition(MimeBodyPart.INLINE);
		
		MimeMultipart content = new MimeMultipart("related");
		content.addBodyPart(bodyPart);
		content.addBodyPart(imgPart);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailId));
		message.setSubject(subject);
		message.setContent(content);
		
		Transport.send(message);
		return true;
	}
	
	public boolean sendEmail(List<InternetAddress> emailIds, String subject, MimeMultipart content) throws AddressException, MessagingException{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
		message.setRecipients(Message.RecipientType.BCC, emailIds.toArray(new Address[emailIds.size()]));
		message.setSubject(subject);
		message.setContent(content);
		Transport.send(message);
		return true;
	}
	
	public boolean sendEmail(String emailId, String subject, MimeMultipart content) throws AddressException, MessagingException{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailId));
		message.setSubject(subject);
		message.setContent(content);
		Transport.send(message);
		return true;
	}
	
	public boolean sendActivationMail(String emailId, String subject, MimeMultipart content) throws AddressException, MessagingException{
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailId));
		message.setRecipient(Message.RecipientType.BCC, new InternetAddress("no-reply@huddil.com"));
		message.setSubject(subject);
		message.setContent(content);
		Transport.send(message);
		return true;
	}
	
	public boolean sendEmail(List<InternetAddress> emailIds, String body, String subject, String image) throws MessagingException{
		String cid = getContentId();
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setText(body.replace("newCid", cid), "US-ASCII", "html");
		
		MimeBodyPart imgPart = new MimeBodyPart();
		imgPart.setDataHandler(new DataHandler(new ByteArrayDataSource(DatatypeConverter.parseBase64Binary(image), "image/png")));
		imgPart.setContentID("<" + cid + ">");
		imgPart.setDisposition(MimeBodyPart.INLINE);
		
		MimeMultipart content = new MimeMultipart("related");
		content.addBodyPart(bodyPart);
		content.addBodyPart(imgPart);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(props.getProperty("mail.username.from")));
		message.setRecipients(Message.RecipientType.BCC, emailIds.toArray(new Address[emailIds.size()]));
		message.setSubject(subject);
		message.setContent(content);
		
		Transport.send(message);
		return true;
	}
	
	public static void main(String[] args) throws IOException, AddressException {
		/*String img = "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";
		byte[] image = DatatypeConverter.parseBase64Binary(img);
		List<InternetAddress> tos = new ArrayList<InternetAddress>();
		tos.add(new InternetAddress("kannan.m@myapps-solutions.com"));
		tos.add(new InternetAddress("jitin.v@myapps-solutions.com"));
		System.out.println(new EmailService().sendEmail(tos, "test mail", "first message", image));
		System.out.println(new EmailService().sendEmail(tos, "test mail", "first message", image));*/
		new EmailQueue().addEmail(1, "cashup.test@gmail.com", "Your friend shared an app");
	}
}
