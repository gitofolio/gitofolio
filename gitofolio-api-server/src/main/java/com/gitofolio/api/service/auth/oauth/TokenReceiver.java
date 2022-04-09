package com.gitofolio.api.service.auth.oauth;

import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationCapsule;

public interface TokenReceiver{
	
	String receiveToken(String code, OauthApplicationCapsule oauthApplicationCapsule);
	
}