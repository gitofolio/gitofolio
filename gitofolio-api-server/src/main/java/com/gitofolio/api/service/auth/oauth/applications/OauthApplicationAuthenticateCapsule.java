package com.gitofolio.api.service.auth.oauth.applications;

import org.springframework.http.*;

public interface OauthApplicationAuthenticateCapsule{
	
	String authenticateUri();
	HttpMethod authenticateMethod();
	HttpEntity authenticateHttpEntity(String token);
	
}