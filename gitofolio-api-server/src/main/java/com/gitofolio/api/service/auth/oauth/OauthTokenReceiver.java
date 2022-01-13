package com.gitofolio.api.service.auth.oauth;

public interface OauthTokenReceiver<V, T>{
	
	V receiveToken(T parameter);
	
}
