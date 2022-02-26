package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.util.*;

import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.common.errordtos.ErrorDTO;

import io.jsonwebtoken.*;

@ControllerAdvice
public class AuthExceptionController{
	
	private final MultiValueMap<String, String> headers;
	
	@ExceptionHandler({AuthenticateException.class})
	public ResponseEntity<ErrorDTO> authenticateExceptionHandler(AuthenticateException authenticateException){
		ErrorDTO errorDTO = new ErrorDTO(authenticateException.getTitle(), authenticateException.getMessage());
		
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({UnsupportedJwtException.class, MissingClaimException.class, IncorrectClaimException.class, SignatureException.class})
	public ResponseEntity<ErrorDTO> invalidJwtTokenExceptionHandler(Exception e){
		ErrorDTO errorDTO = new ErrorDTO("jwt 토큰 인증 오류", e.getMessage());
		
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.UNAUTHORIZED);
	}
	
	public AuthExceptionController(){
		this.headers = new HttpHeaders();
		this.headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
	}
	
}