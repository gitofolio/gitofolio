package com.gitofolio.api.service.auth.token;

public interface TokenValidator{
	
	String currentLogined();
	
	boolean validateToken(String validateTarget);
	
	boolean validateToken(TokenAble validateTarget);
	
}