package com.gitofolio.api.service.user.exception;

public class DuplicationUserException extends RuntimeException{
	
	private String title;
	private String request;
	
	public DuplicationUserException(String title, String message, String request){
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