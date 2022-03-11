package com.gitofolio.api.service.auth.oauth.applications.kakao;

import com.gitofolio.api.service.auth.oauth.Authenticator;
import com.gitofolio.api.service.auth.oauth.applications.OauthApplication;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

public class KakaoApplication implements OauthApplication{
	
	private final String name;
	private final String url;
	private final String testUrl;
	private final Authenticator<UserDTO, String> kakaoAuthenticator;
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public String getUrl(){
		// return this.url;
		return this.testUrl;
	}
	
	@Override
	public String getUrlWithQueryString(String queryString){
		return this.getUrl()+ queryString;
	}
	
	@Override
	public Authenticator getAuthenticator(){
		return this.kakaoAuthenticator;
	}
	
	@Autowired
	public KakaoApplication(@Qualifier("kakaoAuthenticator") Authenticator<UserDTO, String> kakaoAuthenticator){
		this.name = "kakao";
		this.url = "https://kauth.kakao.com/oauth/authorize?client_id=c2b88ff4c3d8216fa35b25f20cd784f1&redirect_uri=https://api.gitofolio.com/oauth/kakao&response_type=code&state=";
		this.testUrl = "https://kauth.kakao.com/oauth/authorize?client_id=c2b88ff4c3d8216fa35b25f20cd784f1&redirect_uri=https://api-server-gitofolio-qfnxv.run.goorm.io/oauth/kakao&response_type=code&state=";
		this.kakaoAuthenticator = kakaoAuthenticator;
	}
	
}