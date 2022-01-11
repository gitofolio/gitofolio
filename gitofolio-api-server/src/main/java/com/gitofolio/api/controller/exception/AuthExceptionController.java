package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.common.errordtos.ErrorDTO;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.SignatureException;

@ControllerAdvice
public class AuthExceptionController{
	
	@ExceptionHandler({AuthenticateException.class})
	public ResponseEntity<ErrorDTO> authenticateExceptionHandler(AuthenticateException authenticateException){
		ErrorDTO errorDTO = new ErrorDTO(authenticateException.getTitle(), authenticateException.getMessage());
		
		return new ResponseEntity(errorDTO, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({UnsupportedJwtException.class, MissingClaimException.class, IncorrectClaimException.class, SignatureException.class})
	public ResponseEntity<ErrorDTO> invalidJwtTokenExceptionHandler(Exception e){
		ErrorDTO errorDTO = new ErrorDTO("jwt 토큰 인증 오류", e.getMessage());
		
		return new ResponseEntity(errorDTO, HttpStatus.UNAUTHORIZED);
	}
	
}