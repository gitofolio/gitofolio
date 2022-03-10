package com.gitofolio.api.service.auth.oauth;

public interface Authenticator<V, T>{
	
	V authenticate(T parameter);
	
}