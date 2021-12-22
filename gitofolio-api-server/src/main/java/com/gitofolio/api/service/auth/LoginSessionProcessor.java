package com.gitofolio.api.service.auth;

import org.springframework.stereotype.Service;

import com.gitofolio.api.service.user.dtos.UserDTO;

import javax.servlet.http.HttpSession;

@Service
public class LoginSessionProcessor extends SessionProcessor<UserDTO>{
	
	public LoginSessionProcessor(){
		this.initSessionInfo();
	}
	
	@Override
	protected void initSessionInfo(){
		this.sessionId = "uId";
	}
	
	@Override
	protected void hook(){
		httpSession.setMaxInactiveInterval(60*30);
	}
}