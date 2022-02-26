package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.servlet.*;
import org.springframework.web.*;
import org.springframework.util.*;
import com.gitofolio.api.service.common.errordtos.ErrorDTO;

@ControllerAdvice
public class DispatcherServletExceptionController{

	private final MultiValueMap<String, String> headers;
	
	@ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<ErrorDTO> dispatcherServletExceptionHandler(Exception e){
		ErrorDTO errorDTO = new ErrorDTO("Bad Request", "지원하지 않는 HTTP Method혹은, URL입니다.", "api 문서를 참고해주세요 : https://api.gitofolio.com/restdocs");
		
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.BAD_REQUEST);
	}
	
	public DispatcherServletExceptionController(){
		this.headers = new HttpHeaders();
		this.headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
	}
	
}