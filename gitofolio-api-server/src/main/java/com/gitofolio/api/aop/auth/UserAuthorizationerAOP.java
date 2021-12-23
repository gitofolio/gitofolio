package com.gitofolio.api.aop.auth;

import com.gitofolio.api.service.auth.SessionProcessor;
import com.gitofolio.api.aop.auth.annotation.UserAuthorizationer;
import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.exception.AuthenticateException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.*;
import java.lang.annotation.Annotation;

@Aspect
@Component
public class UserAuthorizationerAOP{
	
	private SessionProcessor<UserDTO> loginSessionProcessor;
	
	@Before("@annotation(com.gitofolio.api.aop.auth.annotation.UserAuthorizationer)")
	public void authorize(JoinPoint joinPoint){
		
		Class target = joinPoint.getTarget().getClass();
		Method[] targetMethods = target.getMethods();
		String methodName = joinPoint.getSignature().getName();
		
		UserAuthorizationer userAuthorizationerAnnotation = null;
		for(Method method : targetMethods) 
			if(method.getName().equals(methodName)) 
				userAuthorizationerAnnotation = method.getAnnotation(UserAuthorizationer.class);
		
		int idx = userAuthorizationerAnnotation.idx();

		Object[] params = joinPoint.getArgs();
		Object param = params[idx];
		if(param.getClass().equals(UserDTO.class)) this.processAuthorization((UserDTO)param);
		else if(param.getClass().equals(String.class)) this.processAuthorization((String)param);
	}
	
	@Autowired
	public UserAuthorizationerAOP(@Qualifier("loginSessionProcessor") SessionProcessor<UserDTO> loginSessionProcessor){
		this.loginSessionProcessor = loginSessionProcessor;
	}
	
	private void processAuthorization(UserDTO userDTO){
		UserDTO loginedUserDTO = this.loginSessionProcessor.getAttribute().orElseThrow(()->new AuthenticateException("인증 오류", "로그인 되어있는 유저가 없습니다."));
		if(!loginedUserDTO.compare(userDTO)) throw new AuthenticateException("인증 오류", "로그인 되어있는 유저와 요청 대상 유저가 일치하지 않습니다.");
	}
	
	private void processAuthorization(String userName){
		UserDTO loginedUserDTO = this.loginSessionProcessor.getAttribute().orElseThrow(()->new AuthenticateException("인증 오류", "로그인 되어있는 유저가 없습니다."));
		if(!loginedUserDTO.getName().equals(userName)) throw new AuthenticateException("인증 오류", "로그인 되어있는 유저와 요청 대상 유저가 일치하지 않습니다.");
	}
	
}