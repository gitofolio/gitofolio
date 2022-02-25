package com.gitofolio.api.service.user.exception;

import com.gitofolio.api.service.common.UnCaughtException;

public class NonExistUserException extends UnCaughtException{
	
	private String request;
	private String title;
	
	public NonExistUserException(){
		super();
	}
	
	public NonExistUserException(String message){
		super(message);
		this.title="";
		this.request="";
	}
	
	public NonExistUserException(String title, String message){
		super(message);
		this.title = title;
		this.request = "";
	}
	
	public NonExistUserException(String title, String message, String request){
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