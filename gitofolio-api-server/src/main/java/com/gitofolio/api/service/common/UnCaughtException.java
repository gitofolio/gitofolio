package com.gitofolio.api.service.common;

public abstract class UnCaughtException extends RuntimeException{
	/*
		이 클래스를 상속받은 메소드는 주의하지 않아도 되는 예외 라는 논리적 의미를 갖고있는 클래스임
	*/
	
	public UnCaughtException(){}
	
	public UnCaughtException(String message){
		super(message);
	}
	
} 