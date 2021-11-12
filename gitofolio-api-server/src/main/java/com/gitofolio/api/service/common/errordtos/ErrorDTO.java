package com.gitofolio.api.service.common.errordtos;

public class ErrorDTO{
	
	private String title;
	private String message;
	private String request;
	
	// constructor
	public ErrorDTO(String title, String message){
		this.title = title;
		this.message = message;
	}
	
	public ErrorDTO(String title, String message, String request){
		this.title = title;
		this.message = message;
		this.request = request;
	}
	
	// setter
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public void setRequest(String request){
		this.request = request;
	}
	
	// getter
	public String getTitle(){
		return this.title;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getRequest(){
		return this.request;
	}
	
}