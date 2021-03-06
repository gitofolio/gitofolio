package com.gitofolio.api.controller.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.method.annotation.*;

import com.gitofolio.api.service.user.exception.*;
import com.gitofolio.api.service.common.errordtos.ErrorDTO;

@ControllerAdvice
public class UserExceptionController{
	
	private final MultiValueMap<String, String> headers;
	
	@ExceptionHandler({NonExistUserException.class})
	public ResponseEntity<ErrorDTO> nonExistUserExceptionHandler(NonExistUserException nonExistUserException){
		ErrorDTO errorDTO = new ErrorDTO(nonExistUserException.getTitle()
													  , nonExistUserException.getMessage()
													  , nonExistUserException.getRequest());
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({IllegalParameterException.class})
	public ResponseEntity<ErrorDTO> illegalParameterExceptionHandler(IllegalParameterException illegalParameterException){
		ErrorDTO errorDTO = new ErrorDTO(illegalParameterException.getTitle()
										, illegalParameterException.getMessage()
										, illegalParameterException.getRequest());
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler({DuplicationUserException.class})
	public ResponseEntity<ErrorDTO> duplicateUserExceptionHandler(DuplicationUserException duplicateUserException){
		ErrorDTO errorDTO = new ErrorDTO(duplicateUserException.getTitle()
										, duplicateUserException.getMessage()
										, duplicateUserException.getRequest());
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({EditException.class})
	public ResponseEntity<ErrorDTO> editException(EditException editException){
		ErrorDTO errorDTO = new ErrorDTO(editException.getTitle()
										, editException.getMessage());
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<ErrorDTO> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){
		ErrorDTO errorDTO = new ErrorDTO("ILLEGALPARAMETERERROR", "????????? ???????????? ????????? ?????????????????????.");
		
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<ErrorDTO> AllUnknownExceptionHandler(Exception e){
		ErrorDTO errorDTO = new ErrorDTO("INTERNALSERVERERROR", "????????? ???????????? ?????? ????????? ??????????????????.", "?????? ???????????? ???????????????");
		e.printStackTrace();
		return new ResponseEntity(errorDTO, this.headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public UserExceptionController(){
		this.headers = new HttpHeaders();
		this.headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
	}
	
}