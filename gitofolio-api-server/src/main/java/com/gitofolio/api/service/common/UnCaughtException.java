package com.gitofolio.api.service.common;

public abstract class UnCaughtException extends RuntimeException{
	/*
		??? ???????????? ???????????? ???????????? ???????????? ????????? ?????? ?????? ?????? ????????? ????????? ???????????? ????????????
	*/
	
	public UnCaughtException(){}
	
	public UnCaughtException(String message){
		super(message);
	}
	
} 