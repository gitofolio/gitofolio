package com.gitofolio.api.service.auth.oauth.applications;

import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationCapsule;

public interface OauthApplication{
	
	String getName();
	String getUrl();
	String getUrlWithQueryString(String queryString);
	OauthApplicationCapsule getOauthApplicationCapsule();
	
} 