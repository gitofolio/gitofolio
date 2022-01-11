package com.gitofolio.api.aop.auth.annotation;

import com.gitofolio.api.service.auth.token.TokenValidator;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public enum TokenType{
	
	JWT;
	
	private TokenValidator tokenValidator;
	
	public TokenValidator getTokenValidator(){
		return this.tokenValidator;
	}
	
	// constructor
	@Component
	private static class TokenTypeConsistutor{
		
		@Autowired
		public TokenTypeConsistutor(@Qualifier("jwtTokenValidator") TokenValidator jwtTokenValidator){
			JWT.tokenValidator = jwtTokenValidator;
		}
		
	}
	
}