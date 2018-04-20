package myapps.notification.service.model;

import java.io.Serializable;
import java.util.List;

public class SMSResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 375373379296073282L;
	private List<Message> messages;
	private List<ErrorsOrWarnings> warnings;

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<ErrorsOrWarnings> getWarnings() {
		return warnings;
	}

	public void setWarnings(List<ErrorsOrWarnings> warnings) {
		this.warnings = warnings;
	}

	public List<ErrorsOrWarnings> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorsOrWarnings> errors) {
		this.errors = errors;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private List<ErrorsOrWarnings> errors;
	private String status;

}
