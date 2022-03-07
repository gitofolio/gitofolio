package com.gitofolio.api.service.auth.oauth.authenticate;

public interface Authenticator<V, T>{
	
	V authenticate(T parameter);
	
}