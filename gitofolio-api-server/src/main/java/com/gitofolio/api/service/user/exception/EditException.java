package com.gitofolio.api.service.user.exception;

import com.gitofolio.api.service.common.UnCaughtException;

public class EditException extends UnCaughtException{
	
	private String request;
	private String title;
	
	public EditException(String title, String message, String request){
		super(message);
		this.title = title;
		this.request = request;
	}
	
	public EditException(String title, String message){
		super(message);
		this.title = title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getRequest(){
		return this.request;
	}
	
}