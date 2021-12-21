package com.gitofolio.api.interceptor.auth;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.exception.AuthenticateException;
import com.gitofolio.api.service.auth.SessionProcessor;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticateInterceptor implements HandlerInterceptor{
	
	@Autowired
	@Qualifier("loginSessionProcessor")
	private SessionProcessor<UserDTO> loginSessionProcessor;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		HttpSession httpSession = request.getSession();
		
		if(request.getMethod().equals("GET")) return true;
		
		UserDTO loginedUser = this.loginSessionProcessor.getAttribute(httpSession)
			.orElseThrow(()-> new AuthenticateException("인증 오류", "로그인 하지 않은 유저의 요청입니다. 로그인을 해주세요"));
		
		return true;
	}
	
}