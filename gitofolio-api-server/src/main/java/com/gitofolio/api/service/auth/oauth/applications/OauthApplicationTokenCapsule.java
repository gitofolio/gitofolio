package com.gitofolio.api.service.auth.oauth.applications;

import org.springframework.http.*;

public interface OauthApplicationTokenCapsule{
	
	String tokenUri();
	HttpMethod tokenHttpMethod();
	HttpEntity tokenHttpEntity(String code);
	String tokenFieldName();
	
}