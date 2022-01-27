package com.gitofolio.api.aop.auth;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;

import com.gitofolio.api.aop.AnnotationExtractor;
import com.gitofolio.api.aop.auth.annotation.AuthToken;
import com.gitofolio.api.aop.auth.annotation.TokenType;
import com.gitofolio.api.service.auth.token.TokenAble;
import com.gitofolio.api.service.auth.token.TokenValidator;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;

@Aspect
@Component
public class AuthTokenAop{
	
	private final AnnotationExtractor<AuthToken> annotationExtractor;
	
	@Before("@annotation(com.gitofolio.api.aop.auth.annotation.AuthToken)")
	public void tokenValidCheck(JoinPoint joinPoint){
		
		AuthToken authToken = this.annotationExtractor.extractAnnotation(joinPoint, AuthToken.class);
		
		TokenValidator tokenValidator = authToken.tokenType().getTokenValidator();
		int idx = authToken.idx();
		
		boolean res = false;
		
		Object[] args = joinPoint.getArgs();
		if(args[idx] instanceof String) res = tokenValidator.validateToken((String)args[idx]);
		else if(args[idx] instanceof TokenAble) res = tokenValidator.validateToken((TokenAble)args[idx]);
		else throw new IllegalArgumentException("AuthTokenAop 에러 : "+idx+"번째 파라미터의 타입은 "+ args[idx].getClass().toString() + " 입니다.");
		
		if(!res) throw new AuthenticateException("토큰인증 실패", "인증할 수 없는 토큰입니다.");
	}
	
	@Autowired
	public AuthTokenAop(@Qualifier("annotationExtractor") AnnotationExtractor<AuthToken> annotationExtractor){
		this.annotationExtractor = annotationExtractor;
	}
	
}