package myapps.notification.service.model;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2379199322829641804L;

	private String id;
	private String recipient;
	private String status;

	public Message() {

	}

	public Message(String id, String number) {
		this.id = id;
		this.recipient = number;
	}

	public Message(String id, String recipient, String status) {
		this.id = id;
		this.recipient = recipient;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return recipient;
	}

	public void setNumber(String number) {
		this.recipient = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
