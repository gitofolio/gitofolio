package com.gitofolio.api.service.auth.oauth;

public interface TokenReceiver<V, T>{
	
	V receiveToken(T parameter);
	
}
