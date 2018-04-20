package myapps.notification.service;

import java.io.IOException;

import javax.mail.internet.AddressException;

import myapps.notification.service.utils.EmailQueue;

public class Task{

	public static void main(String[] args) throws AddressException, IOException, InterruptedException {
		new EmailQueue().addEmail(1, "kannan.m@myapps-solutions.com", "first mail");
		Thread.sleep(3000);
		System.out.println("second mail");
		new EmailQueue().addEmail(1, "jitin.v@myapps-solutions.com", "first mail");
	}

}
