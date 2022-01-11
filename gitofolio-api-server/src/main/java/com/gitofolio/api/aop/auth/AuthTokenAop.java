package com.gitofolio.api.aop.auth;

import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;

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
	
	@Before("@annotation(com.gitofolio.api.aop.auth.annotation.AuthToken)")
	public void tokenValidCheck(JoinPoint joinPoint){
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		Class targetClass = joinPoint.getTarget().getClass();
		
		AuthToken authToken = null;
		Method[] methods = targetClass.getMethods();
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				authToken = method.getAnnotation(AuthToken.class);
				break;
			}
		}
		
		TokenValidator tokenValidator = authToken.tokenType().getTokenValidator();
		int idx = authToken.idx();
		
		boolean res = false;
		if(args[idx] instanceof String) res = tokenValidator.validateToken((String)args[idx]);
		else if(args[idx] instanceof TokenAble) res = tokenValidator.validateToken((TokenAble)args[idx]);
		else throw new IllegalArgumentException("AuthTokenAop 에러 : "+idx+"번째 파라미터의 타입은 "+ args[idx].getClass().toString() + " 입니다.");
		
		if(!res) throw new AuthenticateException("jwt토큰인증 실패", "인증할 수 없는 토큰입니다.");
	}
	
}