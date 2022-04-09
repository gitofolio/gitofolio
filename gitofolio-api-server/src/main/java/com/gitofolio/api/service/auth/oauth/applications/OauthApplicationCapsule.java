package com.gitofolio.api.service.auth.oauth.applications;

import org.springframework.http.*;

public interface OauthApplicationCapsule extends OauthApplicationTokenCapsule, OauthApplicationAuthenticateCapsule{
	
	String userNameFieldName();
	String profileUriFieldName();
	
}