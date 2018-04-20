package myapps.notification.service.model;

import java.io.Serializable;

public class DeliveryReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6611654412131026546L;
	private Message message;
	private String status;

	public DeliveryReport(Message message, String status) {
		super();
		this.message = message;
		this.status = status;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
