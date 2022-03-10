package com.gitofolio.api.service.auth.oauth.applications.github;

import com.gitofolio.api.service.auth.oauth.applications.OauthApplication;
import com.gitofolio.api.service.auth.oauth.Authenticator;
import com.gitofolio.api.service.user.dtos.UserDTO;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

@Component
public class GithubApplication implements OauthApplication{
	
	private final String name;
	private final String url;
	private final String testUrl;
	private final Authenticator<UserDTO, String> githubAuthenticator;
	
	@Override
	public String getUrl(){
		return this.url;
	}
	
	@Override
	public String getUrlWithQueryString(String queryString){
		return getRealUrl()+queryString;
		// return getTestUrl()+queryString;
	}
	
	private String getRealUrl(){
		return this.url;
	}
	
	private String getTestUrl(){
		return this.testUrl;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public Authenticator getAuthenticator(){
		return this.githubAuthenticator;
	}
	
	@Autowired
	public GithubApplication(@Qualifier("githubAuthenticator") Authenticator<UserDTO, String> githubAuthenticator){
		this.githubAuthenticator = githubAuthenticator;
		this.name = "github";
		this.url = "https://github.com/login/oauth/authorize?client_id=b86796ef36e991c00490&redirect_uri=https://api.gitofolio.com/oauth/github";
		this.testUrl = "https://github.com/login/oauth/authorize?client_id=b86796ef36e991c00490&redirect_uri=https://api-server-gitofolio-qfnxv.run.goorm.io/oauth/github";
	}
	
}