package com.gitofolio.api.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.gitofolio.api.service.auth.exception.AuthenticateException;

import java.util.Optional;

import javax.servlet.http.HttpSession;

@Service
public abstract class SessionProcessor<V extends Object>{
	
	@Autowired
	protected HttpSession httpSession;
	protected String sessionId;
	
	public void setAttribute(V parameter){
		hook();
		httpSession.setAttribute(sessionId, parameter);
	}
	
	public Optional<V> getAttribute(){
		return Optional.ofNullable((V)httpSession.getAttribute(this.sessionId));
	}
	
	public void deleteAll(){
		httpSession.invalidate();
	}
	
	protected void hook(){
		return;
	}
	
	protected abstract void initSessionInfo();
	
}