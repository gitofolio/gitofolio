package com.gitofolio.api.service.auth.oauth;

import com.gitofolio.api.service.user.dtos.UserDTO;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplicationCapsule;

public interface Authenticator{
	
	UserDTO authenticate(String code, OauthApplicationCapsule applicationCapsule);
	
}