package com.gitofolio.api.service.auth.token;

public interface TokenValidator{
	
	String currentLogined();
	
	boolean validateToken(String token);
	
	boolean validateToken(TokenAble token);
	
}