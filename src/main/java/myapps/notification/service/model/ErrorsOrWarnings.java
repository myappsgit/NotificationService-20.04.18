package myapps.notification.service.model;

import java.io.Serializable;

public class ErrorsOrWarnings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2345152753055808443L;
	private int code;
	private String message;

	public ErrorsOrWarnings(){
		
	}
	
	public ErrorsOrWarnings(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
