package com.gitofolio.api.service.user.exception;

public class EditException extends RuntimeException{
	
	private String request;
	private String title;
	
	public EditException(String title, String message, String request){
		super(message);
		this.title = title;
		this.request = request;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getRequest(){
		return this.request;
	}
	
}