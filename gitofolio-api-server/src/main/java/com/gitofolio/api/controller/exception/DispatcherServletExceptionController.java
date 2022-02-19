package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.servlet.*;
import org.springframework.web.*;
import com.gitofolio.api.service.common.errordtos.ErrorDTO;

@ControllerAdvice
public class DispatcherServletExceptionController{

	@ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<ErrorDTO> dispatcherServletExceptionHandler(Exception e){
		ErrorDTO errorDTO = new ErrorDTO("Bad Request", "지원하지 않는 HTTP Method혹은, URL입니다.", "api 문서를 참고해주세요 : https://api.gitofolio.com/restdocs");
		
		return new ResponseEntity(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
}