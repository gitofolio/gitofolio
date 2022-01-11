package com.gitofolio.api.service.auth.token;

public interface TokenGenerator{
	
	String generateToken(String target);
	
	String generateToken(TokenAble target);
	
}