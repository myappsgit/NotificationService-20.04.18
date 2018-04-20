package myapps.notification.service.model;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

public class Email {

	private List<InternetAddress> tos;
	private String subject;
	private String body;

	public Email() {
	}

	public Email(InternetAddress to, String subject, String body) {
		this(to, subject);
		this.body = body;
	}
	
	public Email(InternetAddress to, String subject) {
		this.tos = new ArrayList<InternetAddress>();
		this.tos.add(to);
		this.subject = subject;
	}

	public Email(List<InternetAddress> tos, String subject, String body) {
		this.tos = tos;
		this.subject = subject;
		this.body = body;
	}

	public List<InternetAddress> getTos() {
		return tos;
	}

	public void setTos(List<InternetAddress> tos) {
		this.tos = new ArrayList<InternetAddress>();
		this.tos.addAll(tos);
	}
	
	public void addTo(InternetAddress to){
		this.tos.add(to);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((tos == null) ? 0 : tos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Email other = (Email) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (tos == null) {
			if (other.tos != null)
				return false;
		} else if (!tos.equals(other.tos))
			return false;
		return true;
	}

}
