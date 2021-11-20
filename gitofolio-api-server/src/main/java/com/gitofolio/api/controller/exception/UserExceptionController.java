package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.common.errordtos.ErrorDTO;

@ControllerAdvice
public class UserExceptionController{
	
	@ExceptionHandler({NonExistUserException.class})
	public ResponseEntity<ErrorDTO> nonExistUserExceptionHandler(NonExistUserException nonExistUserException){
		ErrorDTO errorDTO = new ErrorDTO(nonExistUserException.getTitle()
													  , nonExistUserException.getMessage()
													  , nonExistUserException.getRequest());
		return new ResponseEntity(errorDTO, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({IllegalParameterException.class})
	public ResponseEntity<ErrorDTO> illegalParameterExceptionHandler(IllegalParameterException illegalParameterException){
		ErrorDTO errorDTO = new ErrorDTO(illegalParameterException.getTitle()
										, illegalParameterException.getMessage()
										, illegalParameterException.getRequest());
		return new ResponseEntity(errorDTO, HttpStatus.NOT_ACCEPTABLE);
	}
	
}