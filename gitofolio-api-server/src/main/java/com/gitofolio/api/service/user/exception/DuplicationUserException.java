package com.gitofolio.api.service.user.exception;

import com.gitofolio.api.service.common.UnCaughtException;

public class DuplicationUserException extends UnCaughtException{
	
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