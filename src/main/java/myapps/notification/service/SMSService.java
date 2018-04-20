package myapps.notification.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.InternetAddress;

import com.google.gson.Gson;

import myapps.notification.service.model.DeliveryReport;
import myapps.notification.service.model.ErrorsOrWarnings;
import myapps.notification.service.model.SMSResponse;
import myapps.notification.service.utils.SMSCodes;

public class SMSService {

	private Properties props = null;
	private HttpURLConnection send = null;
	private Gson gson = null;

	public SMSService() throws IOException {
		if (props == null) {
			props = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			props.load(loader.getResourceAsStream("application.properties"));
		}
		if (send == null)
			send = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
		if (gson == null)
			gson = new Gson();

	}

	public String sendSMS(String message, String phoneNumber, String apiKey, String sender) {
		try {
			// String apiKey = "apikey=" + props.getProperty("sms.api.key");
			// String sender = "&sender=" + props.getProperty("sms.sender");
			String numbers = "&numbers=" + phoneNumber;

			String data = "apikey=" + apiKey + numbers + "&message=" + message + "&sender=" + sender;
			send.setDoOutput(true);
			send.setRequestMethod("POST");
			send.setRequestProperty("Content-Length", Integer.toString(data.length()));
			send.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(send.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			SMSResponse response = gson.fromJson(stringBuffer.toString(), SMSResponse.class);
			List<ErrorsOrWarnings> errors = response.getErrors();
			if (errors != null && !errors.isEmpty()) {
				for (ErrorsOrWarnings error : errors) {
					if (error.getCode() == SMSCodes.insufficientCredits) {
						List<InternetAddress> tos = new ArrayList<InternetAddress>();
						tos.add(new InternetAddress("kannan.m@myapps-solutions.com"));
						tos.add(new InternetAddress("jagadees.k@myapps-solutions.com"));
						new EmailService().sendEmail(tos, "Attention required", "Text Local credits exhausted");
					}
				}
				return null;
			} else
				response.getMessages().get(0).getId();
		} catch (Exception e) {
			return "Error " + e;
		}
		return null;
	}

	public int SMSStatus(String id) {
		try {
			String apiKey = "apikey=" + props.getProperty("sms.api.key");
			String message_id = "&message_id=" + id;

			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/status_message/?")
					.openConnection();
			String data = apiKey + message_id;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			DeliveryReport report = gson.fromJson(stringBuffer.toString(), DeliveryReport.class);
			if (report.getStatus().equalsIgnoreCase("success")) {
				if (report.getMessage().getStatus().equalsIgnoreCase("d"))
					return SMSCodes.delivered;
				else
					return SMSCodes.waiting;
			}
		} catch (Exception e) {
			return SMSCodes.failure;
		}
		return SMSCodes.failure;
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(new SMSService().sendSMS("test message",
		// "919952902452"));
		System.out.println(new SMSService().SMSStatus("1217034703"));
	}
}
