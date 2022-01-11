package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.gitofolio.api.service.user.exception.NonExistUserException;
import com.gitofolio.api.service.user.exception.IllegalParameterException;
import com.gitofolio.api.service.user.exception.DuplicationUserException;
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
	
	@ExceptionHandler({DuplicationUserException.class})
	public ResponseEntity<ErrorDTO> duplicateUserExceptionHandler(DuplicationUserException duplicateUserException){
		ErrorDTO errorDTO = new ErrorDTO(duplicateUserException.getTitle()
										, duplicateUserException.getMessage()
										, duplicateUserException.getRequest());
		return new ResponseEntity(errorDTO, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorDTO> AllUnknownExceptionHandler(Exception e){
		ErrorDTO errorDTO = new ErrorDTO("INTERNALSERVERERROR", "서버가 처리할수 없는 오류가 발생했습니다.", "버그 리포트를 보내주세요");
		e.printStackTrace();
		return new ResponseEntity(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}