package com.gitofolio.api.service.auth.authenticate;

public interface Authenticator<V, T>{
	
	V authenticate(T parameter);
	
}