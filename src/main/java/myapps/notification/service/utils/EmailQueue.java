package myapps.notification.service.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import myapps.notification.service.EmailService;
import myapps.notification.service.model.Email;

public class EmailQueue implements Runnable {

	private static Map<Integer, Email> emailQueue = null;
	private static EmailService emailService = null;

	public EmailQueue() throws IOException {
		if (emailService == null)
			emailService = new EmailService();
		if (emailQueue == null)
			emailQueue = new HashMap<Integer, Email>();
	}

	public void addEmail(int userId, String to, String subject) throws AddressException, IOException {
		Email email = emailQueue.get(userId);
		if (email == null)
			email = new Email(new InternetAddress(to), subject, "Email share template should be included");
		else
			email.addTo(new InternetAddress(to));
		emailQueue.put(userId, email);
		Scheduler.startScheduler();
	}

	public void addEmail(int userId, Email email) {
		if (emailQueue.containsKey(userId)) {
			email.setTos(email.getTos());
		}
	}

	public void run() {
		if (emailQueue.isEmpty())
			return;
		emailQueue.forEach((userId, email) -> {
			emailQueue.remove(userId);
			if (!emailService.sendCashUpAppShareEmail(email))
				addEmail(userId, email);
		});
		if (emailQueue.isEmpty()) {
			Scheduler.stopScheduler();
		}
	}

}
