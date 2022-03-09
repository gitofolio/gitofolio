package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.*;

import org.apache.catalina.connector.ClientAbortException;

@ControllerAdvice
public class ConnectionException{

	@ExceptionHandler({ClientAbortException.class})
	public void ignoreBrokenPipe(ClientAbortException clientAbortException){
		return;
	}
	
}