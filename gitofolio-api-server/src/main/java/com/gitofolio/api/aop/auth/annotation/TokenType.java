package com.gitofolio.api.aop.auth.annotation;

import com.gitofolio.api.service.auth.token.TokenValidator;
import com.gitofolio.api.service.auth.exception.*;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public enum TokenType{
	
	JWT,
	PERSONAL_ACCESS_TOKEN,
	AUTO{
		@Override
		public TokenValidator getTokenValidator(){
			String authType = "";
			try{
				authType = this.getAuthType();
			}catch(Exception e){
				throw new AuthenticateException("인증 에러", "토큰이 존재하지 않습니다.");
			}
			return getTokenValidator(authType);
		}
		
		private String getAuthType() throws Exception{
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			String authType = (authorization.split(" "))[0];
			return authType;
		}
		
		private TokenValidator getTokenValidator(String authType){
			switch(authType){
				case "Bearer": return TokenType.JWT.getTokenValidator();
				case "Pat": return TokenType.PERSONAL_ACCESS_TOKEN.getTokenValidator();
			}
			throw new AuthenticateException("인증 에러", "식별할 수 없는 토큰타입 입니다.");
		}
	};
	
	private TokenValidator tokenValidator;
	
	public TokenValidator getTokenValidator(){
		return this.tokenValidator;
	}
	
	@Component
	private static class TokenTypeConsistutor{
		
		@Autowired
		public TokenTypeConsistutor(@Qualifier("jwtTokenValidator") TokenValidator jwtTokenValidator,
								    @Qualifier("personalAccessTokenValidator") TokenValidator personalAccessTokenValidator){
			JWT.tokenValidator = jwtTokenValidator;
			PERSONAL_ACCESS_TOKEN.tokenValidator = personalAccessTokenValidator;
		}
		
	}
	
}