package com.gitofolio.api.service.auth;

import com.gitofolio.api.service.auth.exception.AuthenticateException;

import java.util.Optional;

import javax.servlet.http.HttpSession;

public abstract class SessionProcessor<V extends Object>{
	
	protected String sessionId;
	
	public void setAttribute(HttpSession httpSession, V parameter){
		hook(httpSession);
		httpSession.setAttribute(sessionId, parameter);
	}
	
	public Optional<V> getAttribute(HttpSession httpSession){
		return Optional.ofNullable((V)httpSession.getAttribute(this.sessionId));
	}
	
	public void deleteAll(HttpSession httpSession){
		httpSession.invalidate();
	}
	
	protected void hook(HttpSession httpSession){
		return;
	}
	
	protected abstract void initSessionInfo();
	
}